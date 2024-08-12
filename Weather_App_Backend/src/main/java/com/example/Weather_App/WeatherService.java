package com.example.Weather_App;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final RestTemplate restTemplate;
    private final String apiKey = "75c953dd04724b0f8c8d0901df1077f7";
    private final String apiUrl = "http://api.weatherbit.io/v2.0/current";

    public WeatherService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Weather getWeather(String location) {
        String url = String.format("%s?city=%s&key=%s", apiUrl, location, apiKey);
        String response = restTemplate.getForObject(url, String.class);
        log.info("Response from API: {}", response);

        ObjectMapper mapper = new ObjectMapper();
        Weather weather = null;
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode data = root.path("data");
            if (data.isArray()) {
                for (JsonNode node : data) {
                    weather = mapper.treeToValue(node, Weather.class);
                }
            }
        } catch (IOException e) {
            log.error("Error mapping API response to Weather object", e);
        }

        return weather;
    }

}