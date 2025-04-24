package dev.carlosborges.climaapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;  // A chave da API que você configurou no application.yml
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather";  // A URL base da API

    public String getWeather(String city) {
        // Construa a URL completa com a cidade, a chave da API e as unidades desejadas
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", URLEncoder.encode(city, StandardCharsets.UTF_8))   // Nome da cidade
                .queryParam("appid", apiKey)  // Sua chave da API
                .queryParam("units", "metric")  // Temperatura em Celsius
                .toUriString();

        // Usando RestTemplate para fazer a requisição para a API
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);  // Realiza a requisição GET
        return response;  // Retorna a resposta da API
    }
}
