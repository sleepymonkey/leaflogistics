package com.leaflogistics.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);


    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ApiError> handleServiceException(RuntimeException ex, WebRequest request) {
        log.error("exception occurred: {}", ex.getLocalizedMessage());

        ApiError errorDetails = new ApiError(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ApiError> unknownError(Exception ex, WebRequest request) {
        log.error("exception occurred: {}", ex.getLocalizedMessage());

        ApiError errorDetails = new ApiError("unknown error: " + ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    public static class ApiError {
        private String errorMsg;


        public ApiError() {}

        public ApiError(String errMsg) {
            this.errorMsg = errMsg;
        }


        @JsonProperty("error_msg")
        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }


}