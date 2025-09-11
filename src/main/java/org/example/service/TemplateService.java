package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateService {

    private final ObjectMapper objectMapper;
    private final Map<String, JsonNode> templateCache;

    public TemplateService() {
        this.objectMapper = new ObjectMapper();
        this.templateCache = new HashMap<>();
        loadTemplates();
    }

    private void loadTemplates() {
        String[] templateFiles = {
            "vitals-template.json",
            "neurological-template.json"
        };

        for (String templateFile : templateFiles) {
            try (InputStream inputStream = getClass().getResourceAsStream("/util/templates/" + templateFile)) {
                if (inputStream != null) {
                    JsonNode template = objectMapper.readTree(inputStream);
                    String templateId = template.get("id").asText();
                    templateCache.put(templateId, template);
                }
            } catch (IOException e) {
                System.err.println("Failed to load template: " + templateFile + " - " + e.getMessage());
            }
        }
    }

    public Map<String, Object> mapDataToTemplate(Map<String, Object> transformedData) {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : transformedData.entrySet()) {
            String sectionName = entry.getKey();
            Object sectionData = entry.getValue();

            if (templateCache.containsKey(sectionName) && sectionData instanceof Map) {
                JsonNode template = templateCache.get(sectionName);
                Map<String, Object> mappedSection = mapSectionToTemplate(
                    (Map<String, Object>) sectionData, 
                    template
                );
                result.put(sectionName, mappedSection);
            } else {
                result.put(sectionName, sectionData);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapSectionToTemplate(Map<String, Object> sectionData, JsonNode template) {
        Map<String, Object> mappedSection = new HashMap<>();
        mappedSection.put("id", template.get("id").asText());
        mappedSection.put("cards", new HashMap<>());

        JsonNode cards = template.get("cards");
        Map<String, Object> cardData = (Map<String, Object>) mappedSection.get("cards");

        if (cards != null && cards.isArray()) {
            for (JsonNode card : cards) {
                String cardId = card.get("id").asText();
                Map<String, Object> cardValues = new HashMap<>();

                if (card.has("inputs")) {
                    JsonNode inputs = card.get("inputs");
                    inputs.fieldNames().forEachRemaining(inputName -> {
                        if (sectionData.containsKey(inputName)) {
                            cardValues.put(inputName, sectionData.get(inputName));
                        }
                    });
                }

                if (!cardValues.isEmpty()) {
                    cardData.put(cardId, cardValues);
                }
            }
        }

        if (sectionData.containsKey("unmappedFields")) {
            mappedSection.put("unmappedFields", sectionData.get("unmappedFields"));
        }

        return mappedSection;
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