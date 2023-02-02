package com.ydursun.demo.service;

import com.ydursun.demo.dto.request.LoginRequest;
import com.ydursun.demo.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}
