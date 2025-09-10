package org.example.service;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfExtractionService {
  public Map<String, String> extractFormFields(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException(
          "PDF file cannot be null or empty, Please upload a valid PDF");
    }

    try (InputStream inputStream = file.getInputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(inputStream))) {

      return extractFieldsFromPdf(pdfDoc);
    }
  }

  private Map<String, String> extractFieldsFromPdf(PdfDocument pdfDoc) throws IOException {
    Map<String, String> rawData = new HashMap<>();

    PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
    if (form != null) {
      Map<String, PdfFormField> fields = form.getFormFields();
      for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
        String fieldName = entry.getKey();
        String fieldValue = entry.getValue().getValueAsString();
        if (!fieldValue.equals("--")) {
          rawData.put(fieldName, fieldValue);
        }
      }
    } else {
      throw new IllegalArgumentException("No AcroForm found in PDF file");
    }
    // return rawData;
    return consolidateDuplicateFields(rawData);
  }

  private Map<String, String> consolidateDuplicateFields(Map<String, String> rawData) {
    Map<String, String> consolidated = new HashMap<>();
    
    for (Map.Entry<String, String> entry : rawData.entrySet()) {
      String originalKey = entry.getKey();
      String value = entry.getValue();
      String baseKey = originalKey.replaceAll("\\.\\d+$", "");
      // Only add if we don't already have this base key
      if (!consolidated.containsKey(baseKey) || consolidated.get(baseKey).isEmpty()) {
            consolidated.put(baseKey, value);
      }
    }
    for (Map.Entry<String, String> entry : rawData.entrySet()) {
      String originalKey = entry.getKey();
      String value = entry.getValue();
      // If this key doesn't have a numeric suffix and we haven't added its base version
      if (!originalKey.matches(".*\\.\\d+$") && !consolidated.containsKey(originalKey)) {
        consolidated.put(originalKey, value);
      }
    }
    
    return consolidated;
  }
}