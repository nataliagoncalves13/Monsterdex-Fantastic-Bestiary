package com.monsterdex.monsterdex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

/**
 * Serviço para buscar imagens no Unsplash.
 * Ajustes:
 * - Usa UriComponentsBuilder para montar a URL (escapa query corretamente).
 * - Valida explicitamente apiUrl e apiKey antes do uso para eliminar avisos de null-safety.
 * - Converte o resultado do RestTemplate.getForObject em String seguro (sem risco de null no momento da atribuição),
 *   evitando avisos de "Null type safety" do analisador.
 */
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
     * @param query Nome da criatura para buscar imagem
     * @return URL da imagem (regular) ou null se não encontrar / em caso de chave não configurada
     */
    public String buscarImagemPorNome(String query) {
        // validações explícitas para satisfazer o analisador de null-safety
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("your_unsplash_access_key")) {
            System.out.println("⚠️ Chave da API do Unsplash não configurada. Usando imagem padrão.");
            return null;
        }
        // apiUrl pode ser injetado via @Value; validamos antes do uso
        Objects.requireNonNull(apiUrl, "unsplash.api.url não pode ser null");

        try {
            // Monta e escapa corretamente a URL de consulta
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/search/photos")
                    .queryParam("query", query)
                    .queryParam("per_page", 1)
                    .queryParam("client_id", apiKey)
                    .build()
                    .toUriString();

            // Converte o possível resultado null em "" no momento da atribuição para evitar avisos de null-safety do analisador.
            String response = Objects.toString(restTemplate.getForObject(url, String.class), "");

            if (!response.isBlank()) {
                JsonNode root = objectMapper.readTree(response);
                JsonNode results = root.get("results");

                if (results != null && results.isArray() && results.size() > 0) {
                    JsonNode firstResult = results.get(0);
                    JsonNode urls = firstResult.get("urls");

                    if (urls != null && urls.has("regular")) {
                        String imageUrl = urls.get("regular").asText(null);
                        if (imageUrl != null && !imageUrl.isBlank()) {
                            return imageUrl;
                        }
                    }
                }
            }

            // Não foi encontrada imagem
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