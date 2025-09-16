package org.example.service;

import org.example.mapper.VitalsMapper;
import org.example.mapper.PreferencesMapper;
import org.example.mapper.NeurologicalMapper;
import org.example.mapper.DetailMapper;
import org.example.mapper.SummaryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataMappingService {

    private final VitalsMapper vitalsMapper;
    private final PreferencesMapper preferencesMapper;
    private final NeurologicalMapper neurologicalMapper;
    private final DetailMapper detailMapper;
    private final SummaryMapper summaryMapper;


    public DataMappingService() {
        this.vitalsMapper = new VitalsMapper();
        this.preferencesMapper = new PreferencesMapper();
        this.neurologicalMapper = new NeurologicalMapper();
        this.detailMapper = new DetailMapper();
        this.summaryMapper = new SummaryMapper();
    }

    public Map<String, Object> transformPdfData(Map<String, String> pdfData) {

        Map<String, Object> transformedData = new HashMap<>();
        
        // Create a map of all mappers with their section names
        Map<String, Map<String, Object>> allMappers = new HashMap<>();
        allMappers.put("vitals", vitalsMapper.getVitalsMapping(pdfData));
        allMappers.put("preferences", preferencesMapper.getPreferencesMapping(pdfData));
        allMappers.put("neurological", neurologicalMapper.getNeurologicalMapping());
        allMappers.put("details", detailMapper.getDetailsMapping());
        allMappers.put("summary", summaryMapper.getSummaryMapping());
        
        // Loop through all mappers and apply transformations
        for (Map.Entry<String, Map<String, Object>> mapperEntry : allMappers.entrySet()) {
            String sectionName = mapperEntry.getKey();
            Map<String, Object> mapping = mapperEntry.getValue();
            transformDataSection(pdfData, transformedData, mapping, sectionName);
        }

        // Handle unmapped fields (optional)
        addUnmappedFields(pdfData, transformedData, allMappers.values().toArray(new Map[0]));

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
            // this is for the preferences where card name is assessmentWith we will get yes of off
            if ("preferences".equals(sectionName) && "assessmentWith".equals(pdfKey) && mappedValue instanceof Map){
                List<String> assessmentWithList = new ArrayList<>();
                Map<String, Object> assessmentMap = (Map<String, Object>) mappedValue;
                for (Map.Entry<String, Object> assessmentEntry : assessmentMap.entrySet()){
                    String assessmentMapperKey = assessmentEntry.getKey();
                    Object assessmentMapperValue = assessmentEntry.getValue();
                    String assessmentPDFValue = pdfData.get(assessmentMapperKey);
                    if ("Yes".equals(assessmentPDFValue)){
                        assessmentWithList.add(assessmentMapperValue.toString());
                    }
                }
                sectionData.put(pdfKey, assessmentWithList);
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
