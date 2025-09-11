package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateService {

    private final ObjectMapper objectMapper;
    private final Map<String, JsonNode> templateCache;
    private final RestTemplate restTemplate;
    
    @Value("${api.base.url:https://api.dev.curantissolutions.com}")
    private String apiBaseUrl;
    
    @Value("${api.auth.token:}")
    private String authToken;
    
    @Value("${api.auth.mode:}")
    private String authMode;

    public TemplateService() {
        this.objectMapper = new ObjectMapper();
        this.templateCache = new HashMap<>();
        this.restTemplate = new RestTemplate();
    }
    
    public Map<String, Object> getTempFromAPI(String templateName, String assessment) {
        try {
            String url = apiBaseUrl + "/visits/" + assessment + "/template/" + templateName;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authToken);
            headers.set("x-auth-mode", authMode);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            
            if (responseBody != null && responseBody.containsKey("tmplJson")) {
                Map<String, Object> tmplJson = (Map<String, Object>) responseBody.get("tmplJson");
                if (tmplJson != null && tmplJson.containsKey("value")) {
                    String jsonValue = (String) tmplJson.get("value");
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(jsonValue, Map.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse tmplJson.value", e);
                    }
                }
            }
            
            throw new RuntimeException("tmplJson.value not found in response");
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch template: " + templateName, e);
        }
    }

    public Map<String, Object> processApiMapperService(Map<String, Object> mappedData, String assessment) {
        // Load templates based on sections in mappedData
        templateLoader(mappedData, assessment);
        
        // Map the data using loaded templates
        return templateDataMapper(mappedData);
    }
    
    private void templateLoader(Map<String, Object> mappedData, String assessment) {
        // Go through each section in mappedData and load templates
        for (String sectionType : mappedData.keySet()) {
            // Skip unmapped section and sections already loaded
            if (!"unmapped".equals(sectionType) && !templateCache.containsKey(sectionType)) {
                try {
                    // Call template API using assessment id and section type
                    Map<String, Object> templateData = getTempFromAPI(sectionType, assessment);
                    JsonNode template = objectMapper.valueToTree(templateData);
                    templateCache.put(sectionType, template);
                } catch (Exception e) {
                    System.err.println("Failed to load template for section: " + sectionType + " - " + e.getMessage());
                }
            }
        }
        System.err.println(templateCache);
    }
    
    private Map<String, Object> templateDataMapper(Map<String, Object> mappedData) {
        Map<String, Object> result = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : mappedData.entrySet()) {
            String sectionType = entry.getKey();
            Object sectionData = entry.getValue();
            
            if (templateCache.containsKey(sectionType) && sectionData instanceof Map) {
                JsonNode template = templateCache.get(sectionType);
                Map<String, Object> mappedSection = mapSectionToCards(
                    (Map<String, Object>) sectionData, 
                    template,
                    sectionType
                );
                result.put(sectionType, mappedSection);
            } else {
                result.put(sectionType, sectionData);
            }
        }
        
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapSectionToCards(Map<String, Object> sectionData, JsonNode template, String sectionType) {
        Map<String, Object> result = new HashMap<>();
        result.put("sectionId", sectionType);
        result.put("cards", new HashMap<>());

        JsonNode cards = template.get("cards");
        Map<String, Object> cardsData = (Map<String, Object>) result.get("cards");

        if (cards != null && cards.isArray()) {
            for (JsonNode card : cards) {
                String cardId = card.get("id").asText();
                Map<String, Object> cardKeyValues = new HashMap<>();
                cardKeyValues.put("cardId", cardId);
                cardKeyValues.put("keyValues", new HashMap<>());

                if (card.has("inputs")) {
                    JsonNode inputs = card.get("inputs");
                    Map<String, Object> keyValues = (Map<String, Object>) cardKeyValues.get("keyValues");
                    
                    inputs.fieldNames().forEachRemaining(inputName -> {
                        if (sectionData.containsKey(inputName)) {
                            keyValues.put(inputName, sectionData.get(inputName));
                        }
                    });
                    
                    // Special handling for blood pressure location
                    if ("Blood_Pressure".equals(cardId) && 
                        sectionData.containsKey("location_side") && 
                        sectionData.containsKey("location")) {
                        String combinedLocation = sectionData.get("location") + " " + sectionData.get("location_side");
                        keyValues.put("location", combinedLocation);
                    }
                }

                // Always add the card, even if keyValues is empty
                cardsData.put(cardId, cardKeyValues);
            }
        }

        if (sectionData.containsKey("unmappedFields")) {
            result.put("unmappedFields", sectionData.get("unmappedFields"));
        }

        return result;
    }

    public JsonNode getTemplate(String templateId) {
        return templateCache.get(templateId);
    }

    public Map<String, JsonNode> getAllTemplates() {
        return new HashMap<>(templateCache);
    }

    public boolean hasTemplate(String templateId) {
        return templateCache.containsKey(templateId);
    }
}