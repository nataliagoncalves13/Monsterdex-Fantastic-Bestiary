package com.monsterdex.monsterdex.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UnsplashService {

    @Value("${unsplash.api.key:}")
    private String apiKey;

    @Value("${unsplash.api.url:https://api.unsplash.com}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public UnsplashService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Busca uma imagem no Unsplash baseada na query (nome da criatura)
     */
    public String buscarImagemPorNome(String query) {

        // Verifica se a chave está configurada ou se é o valor padrão
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("your_unsplash_access_key")) {
            return null; 
        }
        
        try {
            Objects.requireNonNull(apiUrl, "URL da API não pode ser nula");

            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/search/photos")
                    .queryParam("query", query)
                    .queryParam("per_page", 1)
                    .queryParam("client_id", apiKey)
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);

            if (response != null && !response.isBlank()) {
                JsonNode root = objectMapper.readTree(response);
                JsonNode results = root.get("results");

                if (results != null && results.isArray() && results.size() > 0) {
                    JsonNode firstResult = results.get(0);
                    JsonNode urls = firstResult.get("urls");

                    if (urls != null && urls.has("regular")) {
                        String imageUrl = urls.get("regular").asText();
                        if (imageUrl != null && !imageUrl.isBlank()) {
                            return imageUrl;
                        }
                    }
                }
            }
            return null;

        } catch (Exception e) {
            // AQUI: Usamos apenas 'Exception' genérico. 
            // Isso IMPEDE que o aviso de 'multicatch' apareça, pois só existe um bloco.
            System.err.println("Aviso: Falha ao buscar imagem no Unsplash (" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * Busca imagem combinando nome e tipo
     */
    public String buscarImagemPorNomeETipo(String nome, String tipo) {
       
        String query = nome != null ? nome : "";
        if (tipo != null && !tipo.isBlank()) {
            query = (query + " " + tipo).trim();
        }

        String imagemUrl = buscarImagemPorNome(query);

        if ((imagemUrl == null || imagemUrl.isBlank()) && tipo != null && !tipo.isBlank()) {
            imagemUrl = buscarImagemPorNome(tipo);
        }

        return imagemUrl;
    }
}