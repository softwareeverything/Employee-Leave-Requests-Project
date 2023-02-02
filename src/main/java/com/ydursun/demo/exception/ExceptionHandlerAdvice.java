package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;
import com.ydursun.demo.dto.response.EndpointResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.ydursun")
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("#################### HttpMessageNotReadableException ####################");
        EndpointResponse response = getGeneralResponseFromRequest(status, ex, request);
        return ResponseEntity.status(status).body(response);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("#################### MethodArgumentNotValidException ####################");

        String fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        EndpointResponse response = getGeneralResponseFromRequest(status, ex, request);
        response.setError(fieldErrors);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<EndpointResponse> handleExceptions(Throwable ex, WebRequest request) {
        log.error("#################### Throwable ####################");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        EndpointResponse response = getGeneralResponseFromRequest(httpStatus, ex, request);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<EndpointResponse> handleExceptions(BadCredentialsException ex, WebRequest request) {
        log.error("#################### BadCredentialsException ####################");
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        EndpointResponse response = getGeneralResponseFromRequest(httpStatus, ex, request);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<EndpointResponse> handleExceptions(GeneralException ex, WebRequest request) {
        log.error("#################### GeneralException ####################");
        EndpointResponse response = getGeneralResponseFromRequest(ex.getHttpStatus(), ex, request);
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EndpointResponse> handleExceptions(ConstraintViolationException ex, WebRequest request) {
        log.error("#################### ConstraintViolationException ####################");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        EndpointResponse response = getGeneralResponseFromRequest(httpStatus, ex, request);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<EndpointResponse> handleExceptions(UsernameNotFoundException ex, WebRequest request) {
        log.error("#################### UsernameNotFoundException ####################");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        String errorMessage = Translator.toLocale("user.not-found");
        EndpointResponse response = getGeneralResponseFromRequest(httpStatus, errorMessage, request);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<EndpointResponse> handleExceptions(ExpiredJwtException ex, WebRequest request) {
        log.error("#################### ExpiredJwtException ####################");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        EndpointResponse response = getGeneralResponseFromRequest(httpStatus, ex, request);
        return ResponseEntity.status(httpStatus).body(response);
    }

    private EndpointResponse getGeneralResponseFromRequest(HttpStatus httpStatus, Throwable ex, WebRequest request) {
        ex.printStackTrace();
        return getGeneralResponseFromRequest(httpStatus, ex.getMessage(), request);
    }

    private EndpointResponse getGeneralResponseFromRequest(HttpStatus httpStatus,
                                                           String exceptionMessage,
                                                           WebRequest request) {
        log.error("Error: {}", exceptionMessage);

        return new EndpointResponse(httpStatus.value(),
                exceptionMessage,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                null);
    }

}
