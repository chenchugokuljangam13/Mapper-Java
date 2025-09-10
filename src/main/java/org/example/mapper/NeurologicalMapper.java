package main.java.org.example.mapper;

import java.util.Map;
import java.util.HashMap;

public class NeurologicalMapper {
     private Map<String, Object> createNeurologicalMapping() {
          Map<String, Object> neurologicalMap = new HashMap<>();
          
          // Orientation mapping
          Map<String, String> orientationMap = new HashMap<>();
          orientationMap.put("Oriented_Time", "oriented.time");
          orientationMap.put("Oriented_Place", "oriented.place");
          orientationMap.put("Oriented_Person", "oriented.person");
          orientationMap.put("Oriented_Situation", "oriented.situation");
          orientationMap.put("Oriented_Unable", "unableToAssessOriented.unableToAssess");
          orientationMap.put("Disoriented_Time", "disoriented.time");
          orientationMap.put("Disoriented_Place", "disoriented.place");
          orientationMap.put("Disoriented_Person", "disoriented.person");
          orientationMap.put("Disoriented_Situation", "disoriented.situation");
          orientationMap.put("Disoriented_Unable", "unableToAssessDisoriented.unableToAssess");
          neurologicalMap.put("Orientation", orientationMap);

          // Anxiety mapping
          Map<String, String> anxietyMap = new HashMap<>();
          anxietyMap.put("pt_Neurological", "patientExperiencesAnxiety");
          anxietyMap.put("Anxiety_Score", "score");
          anxietyMap.put("Anxiety_symptom_impact", "rankSymptomImpact");
          anxietyMap.put("Anxiety_impact_area_Other", "otherSymptomArea");
          anxietyMap.put("Anxiety_impact_area_Explanation", "explanation");
          neurologicalMap.put("Anxiety", anxietyMap);

          // Agitation mapping
          Map<String, String> agitationMap = new HashMap<>();
          agitationMap.put("Pt_Experiences_Agitation", "patientExperiencesAgitation");
          agitationMap.put("Agitation_Score", "score");
          agitationMap.put("Agitation_symptom_impact", "rankSymptomImpact");
          agitationMap.put("Agitation_Pt_impact_area_Other", "otherSymptomArea");
          agitationMap.put("Agitation_Pt_impact_area_Explanation", "explanation");
          neurologicalMap.put("Agitation", agitationMap);

          // Headache mapping
          Map<String, String> headacheMap = new HashMap<>();
          headacheMap.put("Nature", "nature");
          headacheMap.put("Pt_Headaches", "patientExperiencesHeadaches");
          headacheMap.put("Onset_Date_af_date", "onsetDate.date");
          headacheMap.put("Location_Unilateral", "location.unilateral");
          headacheMap.put("Location_Bilateral", "location.bilateral");
          headacheMap.put("Location_Fronto_Temporal", "location.frontoTemporal");
          headacheMap.put("Location_Occipital", "location.occipital");
          headacheMap.put("Pain_Characteristics", "painCharacteristics");
          headacheMap.put("Severity", "severity");
          headacheMap.put("Most_Recent_af_date", "mostRecent.date");
          neurologicalMap.put("Headache", headacheMap);

          // Other conditions
          neurologicalMap.put("Pt_confusion", "patientExperiencesConfusion");
          neurologicalMap.put("Pt_Depression", "patientExperiencesDepression");
          neurologicalMap.put("Recent_Concussion", "otherIssues[]");

          return neurologicalMap;
     }

     public Map<String, Object> getNeurologicalMapping() {
          return createNeurologicalMapping();
     }
}
