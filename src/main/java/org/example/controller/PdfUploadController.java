package org.example.controller;

import org.example.service.PdfExtractionService;
import org.example.service.DataMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*")
public class PdfUploadController {

    @Autowired
    private PdfExtractionService pdfExtractionService;
    
    @Autowired
    private DataMappingService dataMappingService;

    @PostMapping("/upload")
     public ResponseEntity<?> uploadPdf(@RequestParam( value = "file", required = false) MultipartFile file) {
        try {
            if (file == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "No file provided. Please send a multipart/form-data request with 'file'  parameter"));}
            
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please select a PDF file to upload"));
            }

            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only PDF files are allowed"));
            }

            Map<String, String> extractedData = pdfExtractionService.extractFormFields(file);
            Map<String, Object> mappedData = dataMappingService.transformPdfData(extractedData);
            
            return ResponseEntity.ok(Map.of(
                "message", "PDF processed successfully",
                "filename", file.getOriginalFilename(),
                "rawDataFieldsCount", extractedData.size(),
                "patientId", extractedData.get("Patient ID") != null ? extractedData.get("Patient ID") : "",
                "rawData", extractedData,
                "mappedData", mappedData,
                "mappedDataFieldsCount", mappedData.size()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to process PDF: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "PDF service is running"));
    }
}