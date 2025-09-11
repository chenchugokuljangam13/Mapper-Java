package org.example.mapper;

import java.util.HashMap;
import java.util.Map;

public class VitalsMapper {

    private Map<String, Object> createVitalsMapping() {
        Map<String, Object> vitalsMap = new HashMap<>();

        // Declines
        vitalsMap.put("Declines_Vitals", "declined"); 
        vitalsMap.put("Decline_Reason", "declinedReason"); 
        // Note: In API it's "noChanges", in PDF it's "Decline_Reason".
        // Adjust as per requirement.

        // Blood Pressure mapping
        Map<String, Object> bloodPressure = new HashMap<>();
        bloodPressure.put("Vitals_Diastolic", "diastolic");
        bloodPressure.put("Vitals_Side", "location");
        bloodPressure.put("Vitals_Position", "position");
        bloodPressure.put("Vitals_Sysolic", "systolic");
        bloodPressure.put("Vitals_Location_side", "location_side");
        vitalsMap.put("Blood_Pressure", bloodPressure);

        // Temperature mapping
        Map<String, Object> temperature = new HashMap<>();
        temperature.put("Temp_Route", "route");
        temperature.put("Temperature", "temperature");
        vitalsMap.put("Temperature", temperature);

        // Pulse mapping
        Map<String, Object> pulse = new HashMap<>();
        pulse.put("Heart_Rate", "heartRate");
        pulse.put("Rhythm", "rhythm");
        pulse.put("Rhythm_Other", "rhythm");
        pulse.put("Strengh", "strength");
        pulse.put("Strength_Other", "strength");
        pulse.put("Pulse_Locantion", "location");
        pulse.put("Location_Other", "location");
        vitalsMap.put("Pulse", pulse);

        // Respiratory Rate mapping
        Map<String, Object> respRate = new HashMap<>();
        respRate.put("Respiration_Type", "respirationType");
        respRate.put("Respiratory_Rate", "respiratoryRate");
        respRate.put("02Saturation", "o2saturation");
        respRate.put("Respiration_Other", "respirationType");
        vitalsMap.put("Respiratory_Rate", respRate);

        // Height / Weight mapping
        vitalsMap.put("Height_ft", "inchesF");
        vitalsMap.put("Hight_in", "inchesI");

        Map<String, Object> weight = new HashMap<>();
        weight.put("Weight_lb", "lbs");
        weight.put("Weight_kg", "kg");
        vitalsMap.put("Weight", weight);

        // MUAC mapping
        Map<String, Object> muac = new HashMap<>();
        muac.put("MUAC_right_unable", "rightArmOther");
        muac.put("MUAC_right_In", "rightArm.inches");
        muac.put("MUAC_right_cm", "rightArm.cm");
        muac.put("MUAC_left_unable", "leftArmOther");
        muac.put("MUAC_left_In", "leftArm.inches");
        muac.put("MUAC_left_cm", "leftArm.cm");
        vitalsMap.put("MUAC", muac);

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

        // Notes mapping (confirm with API contract)
        Map<String, Object> notes = new HashMap<>();
        notes.put("Vitals_Summary", "description");
        notes.put("Vitals_Notes", "category");
        vitalsMap.put("Notes", notes);

        return vitalsMap;
    }

    public Map<String, Object> getVitalsMapping() {
        return createVitalsMapping();
    }
}
