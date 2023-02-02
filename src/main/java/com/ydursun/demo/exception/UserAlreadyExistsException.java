package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super(Translator.toLocale("user.already-exists"));
    }
}
