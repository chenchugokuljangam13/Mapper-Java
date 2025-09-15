package org.example.mapper;

import java.util.HashMap;
import java.util.Map;

public class SummaryMapper {

    private Map<String, Object> createSummaryMapping() {
        Map<String, Object> summaryMap = new HashMap<>();

        Map<String, Object> narratives = new HashMap<>();

        // Declines
        narratives.put("Vitals_Summary", "description");
        narratives.put("Vitals_Origin", "origin");
        summaryMap.put("narratives", narratives);
        return summaryMap;
    }

    public Map<String, Object> getSummaryMapping() {
        return createSummaryMapping();
    }
}
