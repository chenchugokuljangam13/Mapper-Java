package org.example.mapper;

import java.util.HashMap;
import java.util.Map;

public class VitalsMapper {

    private Map<String, Object> createVitalsMapping(Map<String, String> pdfData) {
        Map<String, Object> vitalsMap = new HashMap<>();
        if ("Yes".equals(pdfData.get("Declines_Vitals"))){
            // Declines
            Map<String, Object> declineCard = new HashMap<>();
            declineCard.put("Declines_Vitals", "declined"); 
            declineCard.put("Decline_Reason", "declinedReason"); 
            vitalsMap.put("decline", declineCard);
        }
        else {
            // Declines
            Map<String, Object> declineCard = new HashMap<>();
            declineCard.put("Declines_Vitals", "declined"); 
            vitalsMap.put("decline", declineCard);

            // Blood Pressure mapping
            Map<String, Object> bloodPressure = new HashMap<>();
            bloodPressure.put("Vitals_Diastolic", "diastolic");
            bloodPressure.put("Vitals_Side", "location");
            bloodPressure.put("Vitals_Position", "position");
            bloodPressure.put("Vitals_Sysolic", "systolic");
            bloodPressure.put("Vitals_Location_side", "location_side");
            vitalsMap.put("bloodPressure", bloodPressure);

            // Temperature mapping
            Map<String, Object> temperature = new HashMap<>();
            temperature.put("Temp_Route", "route");
            temperature.put("Temperature", "temperature");
            vitalsMap.put("temperatureCardTitle", temperature);

            // Pulse mapping
            Map<String, Object> pulse = new HashMap<>();
            pulse.put("Heart_Rate", "heartRate");
            if (pdfData.get("Rhythm_Other") != null && !pdfData.get("Rhythm_Other").toString().trim().isEmpty()) {
                pulse.put("Rhythm_Other", "rhythm");
            } else {
                pulse.put("Rhythm", "rhythm");
            }
            if (pdfData.get("Strength_Other") != null && !pdfData.get("Strength_Other").toString().trim().isEmpty()) {
                pulse.put("Strength_Other", "strength");
            } else {
                pulse.put("Strengh", "strength"); // fixed spelling (was "Strengh")
            }
            if (pdfData.get("Location_Other") != null && !pdfData.get("Location_Other").toString().trim().isEmpty()) {
                pulse.put("Location_Other", "location");
            } else {
                pulse.put("Pulse_Locantion", "location"); // fixed spelling (was "Locantion")
            }
            vitalsMap.put("pulse", pulse);

            // respiratoryRate Mapping
            Map<String, Object> respRate = new HashMap<>();
            if (pdfData.get("Respiration_Other") != null && !pdfData.get("Respiration_Other").toString().trim().isEmpty()) {
                respRate.put("Respiration_Other", "respirationType");
            }
            else{
                respRate.put("Respiration_Type", "respirationType");
            }
            respRate.put("Respiratory_Rate", "respiratoryRate");
            respRate.put("02Saturation", "o2saturation");
            vitalsMap.put("respiratoryRate", respRate);

            // Height 
            Map<String, Object> height = new HashMap<>();
            height.put("Height_ft", "inchesF");
            height.put("Hight_in", "inchesI");
            // current.put("Height_ft", "inchesF");
            // current.put("Hight_in", "inchesI");
            // height.put("height", current);
            vitalsMap.put("height", height);

            // Weight
            Map<String, Object> weight = new HashMap<>();
            weight.put("Weight_lb", "lbs");
            weight.put("Weight_kg", "kg");
            vitalsMap.put("weight", weight);

            // MUAC mapping
            Map<String, Object> muac = new HashMap<>();
            if ("Off".equals(pdfData.get("MUAC_right_unable"))) {
                muac.put("MUAC_right_In", "rightArm.inches");
                muac.put("MUAC_right_cm", "rightArm.cm");
            }
                muac.put("MUAC_right_unable", "rightArmOther");
            if ("Off".equals(pdfData.get("MUAC_left_unable"))) {
                muac.put("MUAC_left_In", "leftArm.inches");
                muac.put("MUAC_left_cm", "leftArm.cm");
            }
            muac.put("MUAC_left_unable", "leftArmOther");
            vitalsMap.put("muac", muac);


            // COVID-related mappings
            vitalsMap.put("COVID_Inter_travel", "familyEngagedInInternationalTravel");
            vitalsMap.put("COVID_Inter_travel_Comment", "familyEngagedInInternationalTravelText");
            vitalsMap.put("COVID_res_ illness", "familyHaveRespiratoryIllness");
            vitalsMap.put("COVID_res_illness_Comment", "familyHaveRespiratoryIllnessText");
            vitalsMap.put("COVID_signs_symptoms", "familyWithSignsOfCovid");
            vitalsMap.put("COVID_signs_symptoms _Comment", "familyWithSignsOfCovidText");
            vitalsMap.put("COVID_contact", "familyHadContactWithSomeoneUnderCovidInvestigation");
            vitalsMap.put("COVID_contact_Comment", "familyHadContactWithSomeoneUnderCovidInvestigationText");
            vitalsMap.put("COVID_Com_transmission", "patientResidesInTransmissionArea");
            vitalsMap.put("COVID_Com_transmission_Comments", "patientResidesInTransmissionAreaText");

            // summary
            vitalsMap.put("Vitals_Summary", "summary");
        }
        // Notes mapping (confirm with API contract)
        Map<String, Object> notes = new HashMap<>();
        notes.put("Vitals_Notes", "description");
        vitalsMap.put("notes", notes);

        return vitalsMap;
    }

    public Map<String, Object> getVitalsMapping(Map<String, String> pdfData) {
        return createVitalsMapping(pdfData);
    }
}
