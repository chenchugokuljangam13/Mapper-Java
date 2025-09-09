package org.example;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filename = "src/main/resources/sample-vitals-2.pdf"; // Path to your interactive PDF

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(filename))) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
            if (form != null) {
                Map<String, PdfFormField> fields = form.getFormFields();
                for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue().getValueAsString();
                    System.out.println(fieldName + " : " + fieldValue);
                }
            } else {
                System.out.println("No AcroForm found in PDF.");
            }
        } catch (IOException e) {
            System.err.println("Error reading PDF: " + e.getMessage());
        }
    }
}



//package org.example;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
//import org.apache.pdfbox.pdmodel.interactive.form.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        System.out.println("Starting PDFBox standalone example...");
//
//        try (PDDocument document = PDDocument.load(new File("src/main/resources/sample-vitals-1.pdf"))) {
//            System.out.println("Loaded PDF with " + document.getNumberOfPages() + " pages.");
//
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//
//            if (acroForm != null) {
//                List<PDField> fields = acroForm.getFields();
//                System.out.println("Form Fields:");
//                for (PDField field : fields) {
//                    String fieldName = field.getFullyQualifiedName();
//                    String fieldValue = field.getValueAsString();
//                    String fieldType = field.getClass().getSimpleName();
//                    System.out.println(fieldName + " (" + fieldType + "): " + fieldValue);
//
////                    if (field instanceof PDCheckBox) {
////                        String fieldName = field.getFullyQualifiedName();
////                        String fieldValue = field.getValueAsString();
////                        String fieldType = field.getClass().getSimpleName();
////                        System.out.println(fieldName + " (" + fieldType + "): " + fieldValue);
////                        // Check if this checkbox belongs to a radio button group
////                        // Radio buttons have kids representing individual options
////                        if (field.getWidgets() != null && !field.getWidgets().isEmpty()) {
////                            System.out.println("check box");
////                            for (PDAnnotationWidget widget : field.getWidgets()) {
////                                System.out.println(widget.getContents());
////
////                            }
////                        }
////                    }
//                }
//            } else {
//                System.out.println("No AcroForm found in the PDF.");
//            }
//
//        } catch (IOException e) {
//            System.err.println("Failed to load PDF: " + e.getMessage());
//        }
//    }
//}


//
//package org.example;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
//import org.apache.pdfbox.pdmodel.interactive.form.PDField;
//import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Main {
//
//    public static void main(String[] args) throws IOException {
//        try (PDDocument document = PDDocument.load(new File("src/main/resources/sample3.pdf"))) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//
//            if (acroForm != null) {
//                Map<String, String> radioGroups = new HashMap<>();
//
//                for (PDField field : acroForm.getFields()) {
//                    if (field instanceof PDCheckBox) {
//                        String fieldName = field.getFullyQualifiedName();
//                        String value = field.getValueAsString();
//
//                        // Assuming radio groups share a prefix in field name, e.g. "Group1.Option1"
//                        String groupName = extractGroupName(fieldName);
//
//                        if ("On".equalsIgnoreCase(value) || isValidRadioValue(value)) {
//                            radioGroups.put(groupName, value);
//                        }
//                    }
//                }
//
//                for (Map.Entry<String, String> entry : radioGroups.entrySet()) {
//                    System.out.println("Radio group: " + entry.getKey() + " selected value: " + entry.getValue());
//                }
//            }
//        }
//    }
//
//    // Example: extract group name by taking prefix before dot, change as per your field name convention
//    private static String extractGroupName(String fieldName) {
//        int dotIndex = fieldName.indexOf('.');
//        if (dotIndex > 0) {
//            return fieldName.substring(0, dotIndex);
//        }
//        return fieldName;
//    }
//
//    // Check if a value looks like a radio export value (digits or specific)
//    private static boolean isValidRadioValue(String value) {
//        return value != null && value.matches("\\d+"); // digits only, adjust for your values
//    }
//}