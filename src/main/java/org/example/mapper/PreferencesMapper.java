package org.example.mapper;
import java.util.HashMap;
import java.util.Map;

public class PreferencesMapper {

    private Map<String, Object> createPreferencesMapping(Map<String, String> pdfData) {
          Map<String, Object> preferencesMap = new HashMap<>();
          
          // For Assessment With
          Map<String, Object> assessmentWithMap = new HashMap<>();
          assessmentWithMap.put("Assessment_W_Family", "family");
          assessmentWithMap.put("Assessment_W_Pt", "patientResponsibleParty");
          assessmentWithMap.put("Assessment_W_Caregiver", "caregiver");
          preferencesMap.put("assessmentWith", assessmentWithMap);
          
          // For Patient preference for CPR
          Map<String, Object> cprDnrDiscussionMap = new HashMap<>();
          cprDnrDiscussionMap.put("Pt_want_CPR", "patientWantCpr");
          if ("Yes".equals(pdfData.get("DNR"))){    
               cprDnrDiscussionMap.put("DNR_located", "outHospitalDnrLocation");
               cprDnrDiscussionMap.put("DNR_Date_Signed", "inPatientDnrDateSigned.date");
               cprDnrDiscussionMap.put("DNR_Name_physician", "inPatientDnrPhysician");
               
          };
          cprDnrDiscussionMap.put("DNR", "outOfHospitalDnr");
          cprDnrDiscussionMap.put("Understand_CPR", "patientUnderstandCpr");
          cprDnrDiscussionMap.put("Code_status", "codeStatus");
          preferencesMap.put("cprDnrDiscussion", cprDnrDiscussionMap);

          // For Physician Orders for Life-Sustaining Treatment (POLST)
          Map<String, Object> polstMap = new HashMap<>();
          if ("Yes".equals(pdfData.get("POLST"))){
               polstMap.put("POLST_Location", "polstLocation");
               polstMap.put("POST_Name_physician", "nameOfPhysician");
               polstMap.put("POST_Date_Signed", "dateSigned.date");
          }
          polstMap.put("POLST", "patientHasPolst");
          preferencesMap.put("hasPhysicianOrdersLifeSustainingTreatment", polstMap);

          // For Medical Orders for Scope of Treatment (MOST)
          Map<String, Object> mostMap = new HashMap<>();
          if ("Yes".equals(pdfData.get("MOST"))){
               mostMap.put("MOST_Location", "mostLocation");
               mostMap.put("MOST_Name_physician", "nameOfPhysician");
               mostMap.put("Date_Signed", "dateSigned.date");
          }
          mostMap.put("MOST", "patientHasMost");
          preferencesMap.put("hasMedicalOrdersForScopeOfTreatment", mostMap);

          // For Further Hospitalizations
          Map<String, Object> furtherHospitalizationsMap = new HashMap<>();
          if ("Yes".equals(pdfData.get("Further_Hospital"))){
               furtherHospitalizationsMap.put("Further_Hospital", "patientWantsFurtherHospitalizations");
          }
          furtherHospitalizationsMap.put("further_hospital_pt_want", "whatFurtherHospitalizationsPatientWants");
          preferencesMap.put("furtherHospitalizations", furtherHospitalizationsMap);

          // For Spiritual/Existential Concerns
          Map<String, Object> spiritualConcernsMap = new HashMap<>();
          if ("Yes".equals(pdfData.get("Spiritual_Existential_Concerns"))){
               spiritualConcernsMap.put("Spiritual_Existential_Concerns_Explanation", "patientSpiritualExistential");
          }
          spiritualConcernsMap.put("Spiritual_Existential_Concerns", "hasPatientSpiritualConcerns");
          preferencesMap.put("spiritualConcerns", spiritualConcernsMap);

          // For Signs of Imminent Death
          Map<String, Object> imminentDeathMap = new HashMap<>();
          if ("Yes".equals(pdfData.get("Imminent_Death"))){
               imminentDeathMap.put("Imminent_Death_Explanation", "signsOfImminentDeathExplanation");
          }
          imminentDeathMap.put("Imminent_Death", "patientShowingSignsOfImminentDeath");
          preferencesMap.put("imminentDeathMap", imminentDeathMap);

          // For Preferences Notes:
          Map<String, Object> notes = new HashMap<>();
          notes.put("Preferences_Notes", "description");
          preferencesMap.put("notes", notes);


          return preferencesMap;
    }

    public Map<String, Object> getPreferencesMapping(Map<String, String> pdfData) {
          return createPreferencesMapping(pdfData);
    }
}
