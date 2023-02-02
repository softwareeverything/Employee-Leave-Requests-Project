package com.ydursun.demo.dto.request;


import com.ydursun.demo.model.enums.Role;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

public class CreateUserRequest {
    @NotEmpty(message = "{validation.email.not-empty}")
    @Email(message = "{validation.email.not-valid-format}")
    private String email;
    @NotEmpty(message = "{validation.password.not-empty}")
    @Size(min = 5, message = "{validation.password.min}")
    @Size(max = 35, message = "{validation.password.max}")
    private String password;

    private Role role;

    @NotNull(message = "{validation.dates.not-null}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startedAt;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

}
