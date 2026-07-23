package com.regintel.ai.common.exception;

import com.regintel.ai.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.regintel.ai.llm.exception.LlmException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::mapFieldError)
                .toList();
        log.warn("Validation failed: {} field error(s)", fieldErrors.size());
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler(LlmException.class)
    public ResponseEntity<ErrorResponse> handleLlmException(
            LlmException ex, HttpServletRequest request) {
        log.warn("LLM error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("Upload size exceeded: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Uploaded file exceeds the maximum allowed size of 25MB",
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request.getRequestURI(),
                null
        );
    }

    private ErrorResponse.FieldError mapFieldError(FieldError fieldError) {
        return ErrorResponse.FieldError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue())
                .build();
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, String message, String path, List<ErrorResponse.FieldError> fieldErrors) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
        return ResponseEntity.status(status).body(error);
    }
}
