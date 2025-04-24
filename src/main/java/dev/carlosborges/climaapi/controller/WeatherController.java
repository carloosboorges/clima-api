package dev.carlosborges.climaapi.controller;  // Pacote onde a classe de controlador vai ficar

import dev.carlosborges.climaapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<String> getWeather(@PathVariable String city) {
        String weatherData = weatherService.getWeather(city);
        return ResponseEntity.ok(weatherData);  // Retorna a resposta da API
    }
}
