package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;

public class RequestedDaysBiggerRemainingDaysException extends RuntimeException {
    public RequestedDaysBiggerRemainingDaysException() {
        super(Translator.toLocale("leave-request.requested-days-bigger-remaining-days"));
    }
}
