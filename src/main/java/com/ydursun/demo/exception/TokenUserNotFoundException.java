package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;

public class TokenUserNotFoundException extends RuntimeException {
    public TokenUserNotFoundException() {
        super(Translator.toLocale("token.user.not-found"));
    }
}
