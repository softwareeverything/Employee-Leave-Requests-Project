package com.ydursun.demo.exception;

import org.springframework.http.HttpStatus;

public class GeneralException extends Exception {

    private final HttpStatus httpStatus;
    private final String title;
    private final String message;


    public GeneralException(HttpStatus httpStatus, String title, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.title = title;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
