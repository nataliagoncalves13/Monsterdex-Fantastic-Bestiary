package com.monsterdex.monsterdex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UnsplashService {

    @Value("${unsplash.api.key}")
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
     * @param query Nome da criatura para buscar imagem
     * @return URL da imagem ou null se não encontrar
     */
    public String buscarImagemPorNome(String query) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_unsplash_access_key")) {
            System.out.println("⚠️ Chave da API do Unsplash não configurada. Usando imagem padrão.");
            return null;
        }

        try {
            String url = String.format("%s/search/photos?query=%s&per_page=1&client_id=%s", 
                apiUrl, query, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response != null) {
                JsonNode root = objectMapper.readTree(response);
                JsonNode results = root.get("results");
                
                if (results != null && results.isArray() && results.size() > 0) {
                    JsonNode firstResult = results.get(0);
                    JsonNode urls = firstResult.get("urls");
                    
                    if (urls != null && urls.has("regular")) {
                        return urls.get("regular").asText();
                    }
                }
            }
            
            System.out.println("Nenhuma imagem encontrada para: " + query);
            return null;
            
        } catch (RestClientException e) {
            System.err.println("Erro ao chamar API do Unsplash: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao processar resposta do Unsplash: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca imagem combinando nome e tipo da criatura para melhor resultado
     * @param nome Nome da criatura
     * @param tipo Tipo da criatura
     * @return URL da imagem ou null se não encontrar
     */
    public String buscarImagemPorNomeETipo(String nome, String tipo) {
        // Tenta buscar com nome + tipo primeiro
        String query = nome + " " + tipo;
        String imagemUrl = buscarImagemPorNome(query);
        
        // Se não encontrar, tenta apenas com o tipo
        if (imagemUrl == null && tipo != null && !tipo.isEmpty()) {
            imagemUrl = buscarImagemPorNome(tipo);
        }
        
        return imagemUrl;
    }
}
