package com.ydursun.demo.exception;

import com.ydursun.demo.config.Translator;

public class UserAlreadyHasAWaitingLeaveRequestException extends RuntimeException {
    public UserAlreadyHasAWaitingLeaveRequestException() {
        super(Translator.toLocale("leave-request.user-already-has-waiting-leave-request"));
    }
}
