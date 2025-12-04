package com.monsterdex.monsterdex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Mapa de tipos de habitat para localidades conhecidas (fallback)
    private static final Map<String, String> HABITAT_LOCATIONS = Map.ofEntries(
        Map.entry("floresta", "Amazon"),
        Map.entry("deserto", "Cairo"),
        Map.entry("montanha", "Denver"),
        Map.entry("caverna", "Lascaux"),
        Map.entry("oceano", "Miami"),
        Map.entry("pântano", "New Orleans"),
        Map.entry("vulcão", "Hawaii"),
        Map.entry("planície", "Kansas"),
        Map.entry("tundra", "Alaska"),
        Map.entry("savana", "Serengeti")
    );

    public WeatherService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Busca informações de clima baseado no habitat
     * @param habitat Nome do habitat (ex: "floresta", "deserto")
     * @return String com informações de clima ou null
     */
    public String buscarClimaDoHabitat(String habitat) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweather_api_key")) {
            System.out.println("⚠️ Chave da API OpenWeatherMap não configurada. Usando dados padrão.");
            return gerarClimaFicticio(habitat);
        }

        try {
            // Obtém a localidade correspondente ao habitat
            String localizacao = HABITAT_LOCATIONS.getOrDefault(habitat.toLowerCase(), habitat);
            
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric&lang=pt_br", 
                apiUrl, localizacao, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response != null) {
                JsonNode root = objectMapper.readTree(response);
                
                // Extrai informações importantes
                String descricao = root.path("weather").get(0).path("description").asText("desconhecido");
                double temperatura = root.path("main").path("temp").asDouble(0);
                int umidade = root.path("main").path("humidity").asInt(0);
                double velocidadeVento = root.path("wind").path("speed").asDouble(0);
                
                return formatarClima(descricao, temperatura, umidade, velocidadeVento);
            }
            
            System.out.println("Nenhum dado climático encontrado para: " + habitat);
            return gerarClimaFicticio(habitat);
            
        } catch (RestClientException e) {
            System.err.println("Erro ao chamar API OpenWeatherMap: " + e.getMessage());
            return gerarClimaFicticio(habitat);
        } catch (Exception e) {
            System.err.println("Erro ao processar resposta do OpenWeatherMap: " + e.getMessage());
            return gerarClimaFicticio(habitat);
        }
    }

    /**
     * Formata as informações de clima em uma string legível
     */
    private String formatarClima(String descricao, double temperatura, int umidade, double velocidadeVento) {
        return String.format(
            "Clima: %s | Temperatura: %.1f°C | Umidade: %d%% | Vento: %.1f m/s",
            capitalizar(descricao),
            temperatura,
            umidade,
            velocidadeVento
        );
    }

    /**
     * Gera um clima fictício baseado no tipo de habitat (quando API não está disponível)
     */
    private String gerarClimaFicticio(String habitat) {
        String habitatLower = habitat.toLowerCase();
        
        Map<String, String> climas = Map.ofEntries(
            Map.entry("floresta", "Clima: Tropical úmido | Temperatura: 25°C | Umidade: 85% | Neblina frequente"),
            Map.entry("deserto", "Clima: Árido quente | Temperatura: 35°C | Umidade: 20% | Sem precipitação"),
            Map.entry("montanha", "Clima: Temperado frio | Temperatura: 10°C | Umidade: 60% | Possibilidade de neve"),
            Map.entry("caverna", "Clima: Constante | Temperatura: 8°C | Umidade: 95% | Ambiente úmido"),
            Map.entry("oceano", "Clima: Marítimo | Temperatura: 20°C | Umidade: 75% | Ventos salinos"),
            Map.entry("pântano", "Clima: Tropical húmido | Temperatura: 28°C | Umidade: 90% | Chuvas frequentes"),
            Map.entry("vulcão", "Clima: Quente extremo | Temperatura: 40°C | Umidade: 30% | Fumarolas ativas"),
            Map.entry("planície", "Clima: Continental | Temperatura: 18°C | Umidade: 50% | Ventos constantes"),
            Map.entry("tundra", "Clima: Ártico | Temperatura: -15°C | Umidade: 40% | Solo congelado"),
            Map.entry("savana", "Clima: Tropical seco | Temperatura: 30°C | Umidade: 35% | Estações bem definidas")
        );
        
        return climas.getOrDefault(habitatLower, "Clima: Desconhecido | Temperatura: 20°C | Umidade: 50%");
    }

    /**
     * Capitaliza a primeira letra de uma string
     */
    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
