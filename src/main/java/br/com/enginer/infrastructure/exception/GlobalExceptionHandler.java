package br.com.enginer.infrastructure.exception;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - Validação (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildError(HttpStatus.BAD_REQUEST, message, req.getRequestURI(), ex);
    }

    // 422 - Regras de negócio
    @ExceptionHandler(CheckedException.class)
    public ResponseEntity<ApiError> handleCheckedException(CheckedException ex, HttpServletRequest req) {
        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), req.getRequestURI(), ex);
    }

    // 403 - Acesso negado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI(), ex);
    }

    // 500 - Erro genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest req) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado no servidor", req.getRequestURI(), ex);
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message, String path, Exception ex) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Erro capturado: {} - {}", status, message, ex);
        }

        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        return ResponseEntity.status(status).body(error);
    }
}