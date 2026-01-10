package com.remake.poki.handler;

import com.github.f4b6a3.ulid.UlidCreator;
import com.remake.poki.handler.exceptions.BadRequestException;
import com.remake.poki.handler.exceptions.BusinessException;
import com.remake.poki.handler.exceptions.ForbiddenException;
import com.remake.poki.handler.exceptions.NotFoundException;
import com.remake.poki.handler.exceptions.ServiceException;
import com.remake.poki.handler.exceptions.UnauthorizedException;
import com.remake.poki.handler.exceptions.ValidationException;
import com.remake.poki.handler.responses.ErrorMessage;
import com.remake.poki.handler.responses.ErrorResponse;
import com.remake.poki.i18n.I18nKeys;
import com.remake.poki.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.error("Unexpected error. errorId={}, uri={}", errorId, request.getRequestURI(), ex);
        ErrorMessage errorMessage = new ErrorMessage(Utils.getMessage(I18nKeys.ERROR_SYSTEM_UNEXPECTED));
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.info("NoResourceFound. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("MethodNotSupported. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("BadRequest. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.info("NotFound. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.error("ServiceException. errorId={}, uri={}", errorId, request.getRequestURI(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("MethodArgumentNotValid. errorId={}, uri={}", errorId, request.getRequestURI());
        List<ErrorMessage> listErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorMessage(fe.getField(), fe.getDefaultMessage()))
                .sorted(Comparator.comparing((ErrorMessage e) -> e.getErrorKey() == null ? "" : e.getErrorKey())
                        .thenComparing(e -> e.getMessage() == null ? "" : e.getMessage()))
                .toList();
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, listErrorMessage);
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("ValidationException. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("Unauthorized. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("Forbidden. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        String errorId = UlidCreator.getUlid().toString();
        log.warn("BusinessException. errorId={}, uri={}, msg={}", errorId, request.getRequestURI(), ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(request.getRequestURI(), errorId, errorMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

