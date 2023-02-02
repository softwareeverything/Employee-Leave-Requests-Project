package com.ydursun.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RefreshTokenRequest {

    @NotEmpty(message = "{validation.email.not-empty}")
    @Email(message = "{validation.email.not-valid-format}")
    private String email;

    @NotEmpty(message = "{validation.password.not-empty}")
    @Size(min = 5, message = "{validation.password.min}")
    @Size(max = 35, message = "{validation.password.max}")
    private String password;

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
