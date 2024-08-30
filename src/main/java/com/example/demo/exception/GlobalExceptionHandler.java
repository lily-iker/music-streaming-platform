package com.example.demo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse createErrorResponse(HttpStatus status, String message, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(new Date())
                .status(status.value())
                .error(status.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return createErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDataException(InvalidDataException e, WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(DataInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataInUseException(DataInUseException e, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e,
                                                                        WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, "Database constraint violation", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                               WebRequest request) {
        String message = "Invalid value! ";
        if (e.getMessage().contains("Gender")){
            message += "Gender can only be: male, female, other";
        }
        else {
            // Optional: if more enum required
        }

        return createErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getCause() + e.getMessage(), request);
    }

}
