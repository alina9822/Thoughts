package com.alibu.chatapp.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private static final Logger logger = LoggerFactory.getLogger(OllamaService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public String sendOllamaRequest(String prompt) {
        String url = "http://localhost:11434/api/generate";  // Ollama API endpoint

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3.2");
        requestBody.put("prompt", prompt);  // Use the dynamic prompt here
        requestBody.put("stream", false);

        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // Set content type to JSON

        // Create the request entity (body + headers)
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the request and receive the response
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Check for errors
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Failed to call Ollama API: {}", response.getStatusCode());
            return "Error occurred while calling the API";
        }

        // Return the response body
        return parseResponse(response.getBody());
    }

    private String parseResponse(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return "Received empty response";
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.path("response").asText();
        } catch (Exception e) {
            logger.error("Error occurred while processing the response", e);
            return "Error occurred while processing the response";
        }
    }
}
