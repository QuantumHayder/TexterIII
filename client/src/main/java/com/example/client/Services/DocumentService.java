package com.example.client.Services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.client.DTO.DocumentRequestDTO;
import com.example.client.DTO.DocumentResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocumentService {
    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DocumentResponseDTO registerToDocument(String documentCode, int userId) throws Exception {
        DocumentRequestDTO requestDTO = new DocumentRequestDTO(userId);
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/register/" + documentCode))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response status: " + response.statusCode());             
        System.out.println("Response body: " + response.body());                     
    
        if (response.statusCode() != 200) {
            throw new IllegalStateException("Failed to register: " + response.body());
        }

        DocumentResponseDTO dto = objectMapper.readValue(response.body(), DocumentResponseDTO.class);
        System.out.println("Deserialized DTO: " + dto);                             
        return dto;
    }

    public DocumentResponseDTO createDocument(int ownerId) throws Exception {
        DocumentRequestDTO requestDTO = new DocumentRequestDTO(ownerId);
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/create"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Failed to create document: " + response.body());
        }

        return objectMapper.readValue(response.body(), DocumentResponseDTO.class);
    }
}
