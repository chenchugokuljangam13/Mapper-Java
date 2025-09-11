package org.example.service;

import org.example.mapper.VitalsMapper;
import org.example.mapper.NeurologicalMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataMappingService {

    private final VitalsMapper vitalsMapper;
    private final NeurologicalMapper neurologicalMapper;

    public DataMappingService() {
        this.vitalsMapper = new VitalsMapper();
        this.neurologicalMapper = new NeurologicalMapper();
    }

    public Map<String, Object> transformPdfData(Map<String, String> pdfData) {
        Map<String, Object> transformedData = new HashMap<>();

        Map<String, Object> vitalsMapping = vitalsMapper.getVitalsMapping(pdfData);
        Map<String, Object> neurologicalMapping = neurologicalMapper.getNeurologicalMapping();

        // Apply mappings
        transformDataSection(pdfData, transformedData, vitalsMapping, "vitals");
        transformDataSection(pdfData, transformedData, neurologicalMapping, "neurological");

        // Handle unmapped fields
        addUnmappedFields(pdfData, transformedData, vitalsMapping, neurologicalMapping);

        return transformedData;
    }

    @SuppressWarnings("unchecked")
    private void transformDataSection(Map<String, String> pdfData, Map<String, Object> transformedData, Map<String, Object> mapping, String sectionName) {

        Map<String, Object> sectionData = new HashMap<>();

        for (Map.Entry<String, Object> entry : mapping.entrySet()) {
            String pdfKey = entry.getKey();
            Object mappedValue = entry.getValue();

            if (mappedValue instanceof String) {
                // Simple mapping
                if (pdfData.containsKey(pdfKey)) {
                    setNestedValue(sectionData, (String) mappedValue, pdfData.get(pdfKey));
                }
            } else if (mappedValue instanceof Map) {
                // Nested mapping
                Map<String, Object> nestedMapping = (Map<String, Object>) mappedValue;
                Map<String, Object> nestedData = new HashMap<>();

                for (Map.Entry<String, Object> nestedEntry : nestedMapping.entrySet()) {
                    String nestedPdfKey = nestedEntry.getKey();
                    Object nestedMappedValue = nestedEntry.getValue();

                    if (nestedMappedValue instanceof String && pdfData.containsKey(nestedPdfKey)) {
                        setNestedValue(nestedData, (String) nestedMappedValue, pdfData.get(nestedPdfKey));
                    }
                }

                if (!nestedData.isEmpty()) {
                    sectionData.put(pdfKey, nestedData);
                }
            }
        }

        if (!sectionData.isEmpty()) {
            transformedData.put(sectionName, sectionData);
        }
    }

    private void setNestedValue(Map<String, Object> data, String key, String value) {
        if (key.contains(".")) {
            String[] parts = key.split("\\.", 2);
            String currentKey = parts[0];
            String remainingKey = parts[1];

            @SuppressWarnings("unchecked")
            Map<String, Object> nestedMap = (Map<String, Object>) data.computeIfAbsent(currentKey, k -> new HashMap<String, Object>());
            setNestedValue(nestedMap, remainingKey, value);
        } else {
            data.put(key, value);
        }
    }

    private void addUnmappedFields(Map<String, String> pdfData, Map<String, Object> transformedData, Map<String, Object>... mappings) {

        Map<String, String> unmappedFields = new HashMap<>();

        for (Map.Entry<String, String> entry : pdfData.entrySet()) {
            String pdfKey = entry.getKey();
            boolean isMapped = false;

            for (Map<String, Object> mapping : mappings) {
                if (isMappedInSection(pdfKey, mapping)) {
                    isMapped = true;
                    break;
                }
            }

            if (!isMapped) {
                unmappedFields.put(pdfKey, entry.getValue());
            }
        }

        if (!unmappedFields.isEmpty()) {
            transformedData.put("unmapped", unmappedFields);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isMappedInSection(String pdfKey, Map<String, Object> mapping) {
        if (mapping.containsKey(pdfKey)) {
            return true;
        }

        for (Object value : mapping.values()) {
            if (value instanceof Map) {
                if (isMappedInSection(pdfKey, (Map<String, Object>) value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
