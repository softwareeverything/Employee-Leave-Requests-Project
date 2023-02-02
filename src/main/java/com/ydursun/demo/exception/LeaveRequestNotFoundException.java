package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;

public class LeaveRequestNotFoundException extends RuntimeException {
    public LeaveRequestNotFoundException() {
        super(Translator.toLocale("leave-request.not-found"));
    }
}
