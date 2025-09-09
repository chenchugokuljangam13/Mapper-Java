package org.example;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.util.Map;

public class PdfFormFiller {
    public static void main(String[] args) {
        String inputFile = "src/main/resources/vitals.pdf";
        String outputFile = "src/main/resources/vitals_filled.pdf";

        try {
            // Create a new PDF with filled data
            fillPdfWithData(inputFile, outputFile);
            System.out.println("PDF filled successfully: " + outputFile);
            
            // Now read the filled PDF to verify the data
            System.out.println("\nReading the filled PDF:");
            readFilledPdf(outputFile);
            
        } catch (IOException e) {
            System.err.println("Error processing PDF: " + e.getMessage());
        }
    }

    private static void fillPdfWithData(String inputFile, String outputFile) throws IOException {
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(inputFile), new com.itextpdf.kernel.pdf.PdfWriter(outputFile))) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            
            if (form != null) {
                // Fill in sample patient data
                setFieldValue(form, "Patient ID", "PAT-001");
                setFieldValue(form, "Date", "09/08/2025");
                setFieldValue(form, "Patient Name", "John Doe");
                setFieldValue(form, "Start Time", "09:00 AM");
                setFieldValue(form, "Visit Type", "Regular Checkup");
                setFieldValue(form, "End Time", "09:30 AM");
                
                // Fill in vital signs
                setFieldValue(form, "Temperature (°F)", "98.6");
                setFieldValue(form, "Heart Rate (bpm)", "72");
                setFieldValue(form, "Vitals_Sysolic", "120");
                setFieldValue(form, "Vitals_Diastolic", "80");
                setFieldValue(form, "Respiratory_Rate", "16");
                setFieldValue(form, "O₂Saturation", "98");
                
                // Fill in physical measurements
                setFieldValue(form, "Height_ft", "5");
                setFieldValue(form, "Hight_in", "10");
                setFieldValue(form, "Weight_lb", "180");
                setFieldValue(form, "Weight_kg", "81.6");
                
                // Fill in some notes
                setFieldValue(form, "Vitals_Notes", "Patient appears healthy. All vitals within normal range.");
                setFieldValue(form, "Vitals_Summary", "Normal vital signs. Patient scheduled for follow-up in 6 months.");
                
                // Set some radio button/checkbox values
                setFieldValue(form, "COVID_contact", "Choice1");
                setFieldValue(form, "Temp_Route", "Oral");
                
                // Don't flatten so we can still read the form fields
                // form.flattenFields();
            }
        }
    }
    
    private static void setFieldValue(PdfAcroForm form, String fieldName, String value) {
        PdfFormField field = form.getField(fieldName);
        if (field != null) {
            field.setValue(value);
            System.out.println("Set " + fieldName + " = " + value);
        } else {
            System.out.println("Field not found: " + fieldName);
        }
    }
    
    private static void readFilledPdf(String filename) throws IOException {
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename))) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
            if (form != null) {
                Map<String, PdfFormField> fields = form.getFormFields();
                for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue().getValueAsString();
                    if (fieldValue != null && !fieldValue.trim().isEmpty()) {
                        System.out.println(fieldName + " : " + fieldValue);
                    }
                }
            } else {
                System.out.println("No AcroForm found in PDF.");
            }
        }
    }
}