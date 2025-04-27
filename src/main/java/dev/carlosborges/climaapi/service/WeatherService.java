package dev.carlosborges.climaapi.service;

import dev.carlosborges.climaapi.exception.CidadeNaoEncontradaException;
import dev.carlosborges.climaapi.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;  // A chave da API que você configurou no application.yml
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather";  // A URL base da API

    public WeatherResponse getWeather(String city) throws JSONException {
        // Construa a URL completa com a cidade, a chave da API e as unidades desejadas
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", URLEncoder.encode(city, StandardCharsets.UTF_8))   // Nome da cidade
                .queryParam("appid", apiKey)  // Sua chave da API
                .queryParam("units", "metric")  // Temperatura em Celsius
                .queryParam("lang", "pt_br")
                .toUriString();

        // Usando RestTemplate para fazer a requisição para a API
        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.getForObject(url, String.class);  // Realiza a requisição GET

            // Se a resposta não for nula, processa o JSON
            if (response == null) {
                throw new CidadeNaoEncontradaException("Erro ao buscar clima para a cidade: " + city);
            }

            JSONObject json = new JSONObject(response);

            // Verifica se o JSON contém o nome da cidade
            if (!json.has("name")) {
                throw new CidadeNaoEncontradaException("Cidade não encontrada: " + city);
            }

            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setCidade(json.getString("name"));
            weatherResponse.setPais(json.getJSONObject("sys").getString("country"));
            weatherResponse.setTemperatura(json.getJSONObject("main").getDouble("temp"));
            weatherResponse.setSensacaoTermica(json.getJSONObject("main").getDouble("feels_like"));
            weatherResponse.setDescricao(json.getJSONArray("weather").getJSONObject(0).getString("description"));

            return weatherResponse;  // Retorna a resposta da API

        } catch (HttpClientErrorException e) {
            // Tratando o erro 404 (cidade não encontrada)
            if (e.getStatusCode().value() == 404) {
                throw new CidadeNaoEncontradaException("Cidade não encontrada: " + city);
            }
            // Lança outra exceção para outros erros
            throw new RuntimeException("Erro ao comunicar com a API de clima: " + e.getMessage());
        } catch (Exception e) {
            // Outros erros podem ser capturados aqui
            throw new RuntimeException("Erro inesperado ao processar a solicitação de clima: " + e.getMessage());
        }
    }
}
