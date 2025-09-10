import java.util.Map;
import java.util.HashMap;

public class VitalsMapper {
     private Map<String, Object> createVitalsMapping() {
          Map<String, Object> vitalsMap = new HashMap<>();
          // Blood Pressure mapping
          Map<String, String> bpMap = new HashMap<>();
          bpMap.put("Vitals_Diastolic", "diastolic");
          bpMap.put("Vitals_Side", "location");
          bpMap.put("Vitals_Position", "location");
          bpMap.put("Vitals_Sysolic", "systolic");
          vitalsMap.put("Blood_Pressure", bpMap);
          // Temperature mapping
          Map<String, String> tempMap = new HashMap<>();
          tempMap.put("Temp_Route", "route");
          tempMap.put("Temperature", "temperature");
          vitalsMap.put("Temp", tempMap);
          // Pulse mapping
          Map<String, String> pulseMap = new HashMap<>();
          pulseMap.put("Heart_Rate", "heartRate");
          pulseMap.put("Rhythm", "rhythm");
          pulseMap.put("Rhythm_Other", "rhythm");
          pulseMap.put("Strengh", "strength");
          pulseMap.put("Strength_Other", "strength");
          pulseMap.put("Pulse_Locantion", "location");
          pulseMap.put("Location_Other", "location");
          vitalsMap.put("Pulse", pulseMap);
          // Add other mappings similarly
          vitalsMap.put("Height_ft", "inches");
          vitalsMap.put("Hight_in", "inches");
          // Add COVID related fields
          vitalsMap.put("COVID_Inter_travel", "familyEngagedInInternationalTravel");
          vitalsMap.put("COVID_Inter_travel_Comment", "familyEngagedInInternationalTravelText");
          // ... add other COVID mappings
          return vitalsMap;
     }
     public Map<String, Object> getVitalsMapping() {
          return createVitalsMapping();

     }
}
     