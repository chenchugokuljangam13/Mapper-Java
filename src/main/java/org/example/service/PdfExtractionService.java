package org.example.service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PdfExtractionService {

    public Map<String, Object> extractFormFields(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF file cannot be null or empty");
        }

        try (InputStream inputStream = file.getInputStream();
             PdfDocument pdfDoc = new PdfDocument(new PdfReader(inputStream))) {
            
            return extractAndGroupFieldsFromPdf(pdfDoc);
        }
    }

    private Map<String, Object> extractAndGroupFieldsFromPdf(PdfDocument pdfDoc) throws IOException {
        Map<String, String> rawFormData = new HashMap<>();
        
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
        if (form != null) {
            Map<String, PdfFormField> fields = form.getFormFields();
            for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue().getValueAsString();
                if ("--".equals(fieldValue)) {
                    continue;
                }
                rawFormData.put(fieldName, fieldValue);
            }
        } else {
            throw new IllegalArgumentException("No AcroForm found in PDF file");
        }
        
        return groupFieldsByPrefix(rawFormData);
    }
    
    private Map<String, Object> groupFieldsByPrefix(Map<String, String> rawData) {
        Map<String, Object> groupedData = new LinkedHashMap<>();
        Map<String, Map<String, String>> groups = new LinkedHashMap<>();
        
        for (Map.Entry<String, String> entry : rawData.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            
            if (fieldName.contains("_")) {
                String prefix = fieldName.substring(0, fieldName.indexOf("_"));
                String suffix = fieldName.substring(fieldName.indexOf("_") + 1);
                
                groups.computeIfAbsent(prefix, k -> new LinkedHashMap<>()).put(suffix, fieldValue);
            } else {
                groupedData.put(fieldName, fieldValue);
            }
        }

        // System.out.print("----groups----");
        // System.out.printf("groups: %s%n", groups);
        // System.out.print("----groupedData----");
        // System.out.printf("groupedData: %s%n", groupedData);
        for (Map.Entry<String, Map<String, String>> group : groups.entrySet()) {
            Map<String, String> consolidatedGroup = consolidateDuplicateValues(group.getValue());
            if (!consolidatedGroup.isEmpty()) {
                groupedData.put(group.getKey(), consolidatedGroup);
            }
        }
        // System.out.print("----groupedData----");
        // System.out.printf("groupedData: %s%n", groupedData);
        return groupedData;
        // groupedData.putAll(groups);
        // return groupedData;
    }
    
    private Map<String, String> consolidateDuplicateValues(Map<String, String> groupFields) {
        Map<String, String> consolidated = new LinkedHashMap<>();
        Map<String, String> valueToKey = new HashMap<>();
        
        // First pass: collect all unique values and their preferred keys
        for (Map.Entry<String, String> entry : groupFields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // Remove numeric suffixes for base key (Position.1 -> Position)
            String baseKey = key.replaceAll("\\.\\d+$", "");
            
            if (valueToKey.containsKey(value)) {
                // If we already have this value, prefer the base key (without numbers)
                String existingKey = valueToKey.get(value);
                // System.out.printf("existingKey: %s%n", existingKey);
                if (baseKey.length() < existingKey.length() || !existingKey.contains(".")) {
                    valueToKey.put(value, baseKey);
                }
            } else {
                valueToKey.put(value, baseKey);
            }
        }
        // System.out.printf("value to key: %s%n", valueToKey);
        // Second pass: build the consolidated map
        for (Map.Entry<String, String> entry : valueToKey.entrySet()) {
            String value = entry.getKey();
            String key = entry.getValue();
            System.out.printf("value: %s%n", value);
            System.out.printf("key: %s%n", key);
            consolidated.put(key, value);
        }
        
        return consolidated;
    }
}