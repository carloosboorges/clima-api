package dev.carlosborges.climaapi.controller;

import dev.carlosborges.climaapi.model.WeatherResponse;
import dev.carlosborges.climaapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Corrigido: Usando @PathVariable para pegar o parâmetro da URL
    @GetMapping("/{city}")
    public WeatherResponse getWeather(@PathVariable String city) throws JSONException, UnsupportedEncodingException {
        String decodedCity = URLDecoder.decode(city, "UTF-8");
        return weatherService.getWeather(decodedCity);
    }
}
