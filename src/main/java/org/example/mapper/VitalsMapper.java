package org.example.mapper;

import java.util.HashMap;
import java.util.Map;

public class VitalsMapper {

    private Map<String, Object> createVitalsMapping(Map<String, String> pdfData) {
        Map<String, Object> vitalsMap = new HashMap<>();

        if ("Yes".equals(pdfData.get("Declines_Vitals"))) {
            vitalsMap.put("decline", createDeclineMapping(pdfData, true));
        } else {
            vitalsMap.put("decline", createDeclineMapping(pdfData, false));
            vitalsMap.put("bloodPressure", createBloodPressureMapping());
            vitalsMap.put("temperatureCardTitle", createTemperatureMapping());
            vitalsMap.put("pulse", createPulseMapping(pdfData));
            vitalsMap.put("respiratoryRate", createRespiratoryRateMapping(pdfData));
            vitalsMap.putAll(createHeightWeightMapping());
            vitalsMap.put("muac", createMuacMapping(pdfData));
            vitalsMap.put("covidScreening", createCovidMappings());
        }

        vitalsMap.put("notes", createNotesMapping());
        return vitalsMap;
    }

    private Map<String, Object> createDeclineMapping(Map<String, String> pdfData, boolean isDeclined) {
        Map<String, Object> declineCard = new HashMap<>();
        declineCard.put("Declines_Vitals", "declined");
        if (isDeclined) {
            declineCard.put("Decline_Reason", "declinedReason");
        }
        return declineCard;
    }

    private Map<String, Object> createBloodPressureMapping() {
        Map<String, Object> bloodPressure = new HashMap<>();
        bloodPressure.put("Vitals_Diastolic", "diastolic");
        bloodPressure.put("Vitals_Side", "location");
        bloodPressure.put("Vitals_Position", "position");
        bloodPressure.put("Vitals_Sysolic", "systolic");
        bloodPressure.put("Vitals_Location_side", "location_side");
        return bloodPressure;
    }

    private Map<String, Object> createTemperatureMapping() {
        Map<String, Object> temperature = new HashMap<>();
        temperature.put("Temp_Route", "route");
        temperature.put("Temperature", "temperature");
        return temperature;
    }

    private Map<String, Object> createPulseMapping(Map<String, String> pdfData) {
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
            pulse.put("Strengh", "strength"); // Note: keeping original spelling for compatibility
        }

        if (pdfData.get("Location_Other") != null && !pdfData.get("Location_Other").toString().trim().isEmpty()) {
            pulse.put("Location_Other", "location");
        } else {
            pulse.put("Pulse_Locantion", "location"); // Note: keeping original spelling for compatibility
        }

        return pulse;
    }

    private Map<String, Object> createRespiratoryRateMapping(Map<String, String> pdfData) {
        Map<String, Object> respRate = new HashMap<>();

        if (pdfData.get("Respiration_Other") != null && !pdfData.get("Respiration_Other").toString().trim().isEmpty()) {
            respRate.put("Respiration_Other", "respirationType");
        } else {
            respRate.put("Respiration_Type", "respirationType");
        }

        respRate.put("Respiratory_Rate", "respiratoryRate");
        respRate.put("02Saturation", "o2saturation");
        return respRate;
    }

    private Map<String, Object> createHeightWeightMapping() {
        Map<String, Object> mappings = new HashMap<>();

        // Height mapping
        Map<String, Object> height = new HashMap<>();
        height.put("Height_ft", "inchesF");
        height.put("Hight_in", "inchesI"); // Note: keeping original spelling for compatibility
        mappings.put("height", height);

        // Weight mapping
        Map<String, Object> weight = new HashMap<>();
        weight.put("Weight_lb", "lbs");
        weight.put("Weight_kg", "kg");
        mappings.put("weight", weight);

        return mappings;
    }

    private Map<String, Object> createMuacMapping(Map<String, String> pdfData) {
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

        return muac;
    }

    private Map<String, Object> createCovidMappings() {
        Map<String, Object> covidMappings = new HashMap<>();

        covidMappings.put("COVID_Inter_travel", "familyEngagedInInternationalTravel");
        covidMappings.put("COVID_Inter_travel_Comment", "familyEngagedInInternationalTravelText");
        covidMappings.put("COVID_res_ illness", "familyHaveRespiratoryIllness");
        covidMappings.put("COVID_res_illness_Comment", "familyHaveRespiratoryIllnessText");
        covidMappings.put("COVID_signs_symptoms", "familyWithSignsOfCovid");
        covidMappings.put("COVID_signs_symptoms _Comment", "familyWithSignsOfCovidText");
        covidMappings.put("COVID_contact", "familyHadContactWithSomeoneUnderCovidInvestigation");
        covidMappings.put("COVID_contact_Comment", "familyHadContactWithSomeoneUnderCovidInvestigationText");
        covidMappings.put("COVID_Com_transmission", "patientResidesInTransmissionArea");
        covidMappings.put("COVID_Com_transmission_Comments", "patientResidesInTransmissionAreaText");

        return covidMappings;
    }

    private Map<String, Object> createNotesMapping() {
        Map<String, Object> notes = new HashMap<>();
        notes.put("Vitals_Notes", "description");
        return notes;
    }

    public Map<String, Object> getVitalsMapping(Map<String, String> pdfData) {
        return createVitalsMapping(pdfData);
    }
}
