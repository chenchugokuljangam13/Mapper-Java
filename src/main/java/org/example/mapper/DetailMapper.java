package org.example.mapper;

import java.util.HashMap;
import java.util.Map;

public class DetailMapper {

    private Map<String, Object> createDetailsMapping() {
        Map<String, Object> detailsMap = new HashMap<>();

        // Declines
        detailsMap.put("Visit_Type", "visitType");
        detailsMap.put("Patient_ID", "patID");
        detailsMap.put("Patient_Name", "patName");
        detailsMap.put("End_Time", "endTime");
        detailsMap.put("Start_Time", "startTime");
        detailsMap.put("Date", "date");
        
        return detailsMap;
    }

    public Map<String, Object> getDetailsMapping() {
        return createDetailsMapping();
    }
}
