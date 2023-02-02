package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;

public class SpecifiedDaysAlreadyDaysOffException extends RuntimeException {
    public SpecifiedDaysAlreadyDaysOffException() {
        super(Translator.toLocale("leave-request.specified-days-already-days-off"));
    }
}
