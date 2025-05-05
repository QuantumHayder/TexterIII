package com.example.client.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

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

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Failed to register: " + response.body());
        }

        return objectMapper.readValue(response.body(), DocumentResponseDTO.class);
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

    
    
    // SH added for client document download
    public String downloadDocument(UUID docId) throws IOException {
    String url = "http://localhost:8080/api/document/download/" + docId;
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
        response.append(line).append("\n");
    }
    in.close();

    return response.toString().trim();
     }


}
