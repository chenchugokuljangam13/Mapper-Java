package org.example.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class PdfUtils {

    public static boolean isValidPdfFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        
        File file = new File(filePath);
        return file.exists() && file.isFile() && filePath.toLowerCase().endsWith(".pdf");
    }

    public static boolean isValidPdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        
        return "application/pdf".equals(contentType) && 
               originalFilename != null && 
               originalFilename.toLowerCase().endsWith(".pdf");
    }

    public static String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "unknown";
        }
        
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static long getFileSizeInMB(MultipartFile file) {
        return file.getSize() / (1024 * 1024);
    }

    public static boolean isFileSizeValid(MultipartFile file, long maxSizeMB) {
        return getFileSizeInMB(file) <= maxSizeMB;
    }
}