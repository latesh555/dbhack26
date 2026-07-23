package com.regintel.ai.regulation.service;

import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.regulation.dto.RegulationDto;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.entity.RegulationStatus;
import com.regintel.ai.regulation.repository.RegulationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegulationService {

    private static final int PREVIEW_LENGTH = 500;

    private final RegulationRepository regulationRepository;
    private final DocumentTextExtractionService documentTextExtractionService;

    @Transactional
    public RegulationDto.Response create(RegulationDto.Request request) {
        log.info("Creating regulation: {}", request.getTitle());
        Regulation regulation = Regulation.builder()
                .title(request.getTitle())
                .source(request.getSource())
                .jurisdiction(request.getJurisdiction())
                .documentType(request.getDocumentType())
                .rawContent(request.getRawContent())
                .status(request.getStatus() != null ? request.getStatus() : RegulationStatus.DRAFT)
                .effectiveDate(request.getEffectiveDate())
                .build();
        return toResponse(regulationRepository.save(regulation));
    }

    @Transactional
    public RegulationDto.UploadResponse createFromUpload(
            MultipartFile file,
            String title,
            String source,
            String jurisdiction,
            String documentType,
            LocalDate effectiveDate) {
        log.info("Creating regulation from uploaded file: {}", file.getOriginalFilename());
        String extractedText = documentTextExtractionService.extractText(file);
        String mediaType = documentTextExtractionService.detectMediaType(file);

        Regulation regulation = Regulation.builder()
                .title(title)
                .source(source)
                .jurisdiction(jurisdiction)
                .documentType(documentType)
                .rawContent(extractedText)
                .status(RegulationStatus.DRAFT)
                .effectiveDate(effectiveDate)
                .build();

        Regulation saved = regulationRepository.save(regulation);
        return toUploadResponse(saved, mediaType);
    }

    @Transactional(readOnly = true)
    public List<RegulationDto.Response> findAll() {
        return regulationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RegulationDto.Response findById(UUID id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public RegulationDto.Response update(UUID id, RegulationDto.Request request) {
        log.info("Updating regulation: {}", id);
        Regulation regulation = getEntity(id);
        regulation.setTitle(request.getTitle());
        regulation.setSource(request.getSource());
        regulation.setJurisdiction(request.getJurisdiction());
        regulation.setDocumentType(request.getDocumentType());
        regulation.setRawContent(request.getRawContent());
        if (request.getStatus() != null) {
            regulation.setStatus(request.getStatus());
        }
        regulation.setEffectiveDate(request.getEffectiveDate());
        return toResponse(regulationRepository.save(regulation));
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting regulation: {}", id);
        if (!regulationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Regulation", id);
        }
        regulationRepository.deleteById(id);
    }

    public Regulation getEntity(UUID id) {
        return regulationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regulation", id));
    }

    private RegulationDto.Response toResponse(Regulation regulation) {
        return RegulationDto.Response.builder()
                .id(regulation.getId())
                .title(regulation.getTitle())
                .source(regulation.getSource())
                .jurisdiction(regulation.getJurisdiction())
                .documentType(regulation.getDocumentType())
                .rawContent(regulation.getRawContent())
                .status(regulation.getStatus())
                .effectiveDate(regulation.getEffectiveDate())
                .createdAt(regulation.getCreatedAt())
                .updatedAt(regulation.getUpdatedAt())
                .build();
    }

    private RegulationDto.UploadResponse toUploadResponse(Regulation regulation, String mediaType) {
        String rawContent = regulation.getRawContent() != null ? regulation.getRawContent() : "";
        String preview = rawContent.length() > PREVIEW_LENGTH
                ? rawContent.substring(0, PREVIEW_LENGTH) + "..."
                : rawContent;

        return RegulationDto.UploadResponse.builder()
                .id(regulation.getId())
                .title(regulation.getTitle())
                .source(regulation.getSource())
                .jurisdiction(regulation.getJurisdiction())
                .documentType(regulation.getDocumentType())
                .rawContent(rawContent)
                .status(regulation.getStatus())
                .effectiveDate(regulation.getEffectiveDate())
                .detectedMediaType(mediaType)
                .extractedTextLength(rawContent.length())
                .extractedTextPreview(preview)
                .createdAt(regulation.getCreatedAt())
                .updatedAt(regulation.getUpdatedAt())
                .build();
    }
}
