package com.ecommerce.microcommerce.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest webRequest) {

        String errorMessage = "";
        Throwable cause = ex.getCause();
        if (cause != null && !ObjectUtils.isEmpty(cause.getMessage())) {
            errorMessage = cause.getMessage();
            Throwable subcause = cause.getCause();
            if (subcause != null && !ObjectUtils.isEmpty(subcause.getMessage())) errorMessage = subcause.getMessage();
        }
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

}
