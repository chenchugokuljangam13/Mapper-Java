package org.example.mapper;
import java.util.HashMap;
import java.util.Map;

public class NeurologicalMapper {

    private Map<String, Object> createNeurologicalMapping() {
        Map<String, Object> neurologicalMap = new HashMap<>();

        // Decline fields
        neurologicalMap.put("Neurological_Decline_Reason", "");
        neurologicalMap.put("pt_declines_Asses", "declinesAssessment"); 
        // You may need to handle {"CL010.227": {"declined": true, "noChanges": ""}} dynamically.

        // Orientation mapping (CL010.21)
        neurologicalMap.put("Oriented_Time", "oriented.time");
        neurologicalMap.put("Oriented_Place", "oriented.place");
        neurologicalMap.put("Oriented_Person", "oriented.person");
        neurologicalMap.put("Oriented_Situation", "oriented.situation");
        neurologicalMap.put("Oriented_Unable", "unableToAssessOriented.unableToAssess");
        neurologicalMap.put("Disoriented_Time", "disoriented.time");
        neurologicalMap.put("Disoriented_Place", "disoriented.place");
        neurologicalMap.put("Disoriented_Person", "disoriented.person");
        neurologicalMap.put("Disoriented_Situation", "disoriented.situation");
        neurologicalMap.put("Disoriented _Unable", "unableToAssessDisoriented.unableToAssess");

        // Anxiety mapping (CL010.20)
        neurologicalMap.put("pt_Neurological", "patientExperiencesAnxiety"); // yes/no -> boolean
        neurologicalMap.put("Anxiety_Score", "score");
        neurologicalMap.put("Anxiety_symptom_impact", "rankSymptomImpact");
        neurologicalMap.put("Anxiety_impact_area", "explainSymptomImpact"); 
        neurologicalMap.put("Anxiety_impact_area_Other", "otherSymptomArea");
        neurologicalMap.put("Anxiety_impact_area_Explanation", "explanation");

        // Agitation mapping (CL010.223)
        neurologicalMap.put("Pt_Experiences_Agitation", "patientExperiencesAgitation");
        neurologicalMap.put("Agitation_Score", "score");
        neurologicalMap.put("Agitation_symptom_impact", "rankSymptomImpact");
        neurologicalMap.put("Agitation_Pt_impact_area", "explainSymptomImpact");
        neurologicalMap.put("Agitation_Pt_impact_area_Other", "otherSymptomArea");
        neurologicalMap.put("Agitation_Pt_impact_area_Explanation", "explanation");

        // Confusion mapping (CL010.26)
        neurologicalMap.put("Pt_confusion", "patientExperiencesConfusion");

        // Depression mapping (CL010.27)
        neurologicalMap.put("Pt_Depression", "patientExperiencesDepression");

        // Headache mapping (CL010.28)
        neurologicalMap.put("Nature", "nature");
        neurologicalMap.put("Pt_Headaches", "patientExperiencesHeadaches");
        neurologicalMap.put("Onset_Date_af_date", "onsetDate.date");
        neurologicalMap.put("Location_Unilateral", "location.unilateral");
        neurologicalMap.put("Location_Bilateral", "location.bilateral");
        neurologicalMap.put("Location_Fronto_Temporal", "location.frontoTemporal");
        neurologicalMap.put("Location_Occipital", "location.occipital");
        neurologicalMap.put("Other_Location", "");
        neurologicalMap.put("Pain_Characteristics", "painCharacteristics");
        neurologicalMap.put("Severity", "severity");
        neurologicalMap.put("Most_Recent_af_date", "mostRecent.date");
        neurologicalMap.put("Associated_Nausea", "associatedNausea");
        neurologicalMap.put("Radiation", "radiation");
        neurologicalMap.put("Vomiting", "associatedVomiting");
        neurologicalMap.put("Vomiting_Onset_Date", "associatedVomitingDate.date");
        neurologicalMap.put("Duration_Number", "durationNumber");
        neurologicalMap.put("Time", "durationTime");
        neurologicalMap.put("Frequency_times", "frequency.numerator");
        neurologicalMap.put("Frequency", "frequency.denominator");
        neurologicalMap.put("Vomiting_Frequency", "frequency.timeUnit");
        neurologicalMap.put("Head_Inury", "recentHeadInjury");
        neurologicalMap.put("Head_Injury_Onset_Date", "recentHeadInjuryDate.date");
        neurologicalMap.put("Concussion", "recentConcussion");
        neurologicalMap.put("Head_injury_Date_af_date", "recentConcussionDate.date"); // renamed as discussed
     
        // Other issues (CL010.32)
        neurologicalMap.put("Recent_Concussion", "otherIssues[]");
        // notes 
        neurologicalMap.put("Neurological_Notes", "category");
        neurologicalMap.put("Summary", "description");

        return neurologicalMap;
    }

    public Map<String, Object> getNeurologicalMapping() {
        return createNeurologicalMapping();
    }
}
