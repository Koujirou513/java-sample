package com.example.myapp.service;

import java.time.LocalDate;
import com.example.myapp.dto.PredictionResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class WeatherApiService {
    
    @Value("${python.api.base-url:http://localhost:8000}")
    private String pythonApiBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Python APIから天気情報を取得
     */
    public Integer getWeatherCondition(LocalDate date) {
        try {
            String url = pythonApiBaseUrl + "/weather/" + date.toString();
            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("condition").asInt();
        } catch (Exception e) {
            // Python APIが利用できない場合のデフォルト値を返す
            return null;
        }
    }

    /**
     * Python APIから需要予測を取得
     */
    public PredictionResult getDemandPrediction(LocalDate date) {
        try {
            String url = pythonApiBaseUrl + "/predict/" + date.toString();
            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response); // レスポンスをJsonNodeに変換

            PredictionResult result = new PredictionResult();
            result.setTargetDate(date);

            JsonNode predictions = jsonNode.get("predictions"); 
            if (predictions != null && predictions.isObject()) {
                predictions.fieldNames().forEachRemaining(fieldName -> {
                    result.addItemPrediction(
                        Long.parseLong(fieldName),
                        predictions.get(fieldName).asInt()
                    );
                });
            }
            return result;
        } catch (Exception e) {
            // Python APIが利用できない場合のデフォルト値を返す
            return null;
        }

    }
}
