package com.regintel.ai.regulation.service;

import com.regintel.ai.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
public class DocumentTextExtractionService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "doc", "docx", "txt", "rtf", "odt", "html", "htm", "xml", "csv"
    );

    private static final long MAX_BYTES = 25L * 1024 * 1024;

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new BusinessException("File exceeds maximum size of 25MB");
        }

        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
        validateExtension(filename);

        try {
            byte[] bytes = file.getBytes();
            String mediaType = tika.detect(bytes, filename);
            String text = tika.parseToString(bytes);
            if (text == null || text.isBlank()) {
                throw new BusinessException("Could not extract text from file: " + filename);
            }
            log.info("Extracted {} characters from uploaded file: {} ({})", text.length(), filename, mediaType);
            return text.trim();
        } catch (IOException | TikaException ex) {
            log.error("Failed to extract text from file: {}", filename, ex);
            throw new BusinessException("Failed to extract text from uploaded file: " + ex.getMessage());
        }
    }

    public String detectMediaType(MultipartFile file) {
        try {
            return tika.detect(file.getBytes(), file.getOriginalFilename());
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }

    private void validateExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            throw new BusinessException("Unsupported file type. Allowed: PDF, DOC, DOCX, TXT, RTF, ODT, HTML, XML, CSV");
        }
        String extension = filename.substring(dot + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("Unsupported file type: ." + extension
                    + ". Allowed: PDF, DOC, DOCX, TXT, RTF, ODT, HTML, XML, CSV");
        }
    }
}
