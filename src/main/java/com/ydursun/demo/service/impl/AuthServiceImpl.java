package com.ydursun.demo.service.impl;

import com.ydursun.demo.dto.request.LoginRequest;
import com.ydursun.demo.dto.response.LoginResponse;
import com.ydursun.demo.service.AuthService;
import com.ydursun.demo.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        int accessTokenExpiresInSeconds = tokenService.getAccessTokenExpiresInMilliseconds() / 1000;
        int refreshTokenExpiresInSeconds = tokenService.getRefreshTokenExpiresInMilliseconds() / 1000;

        LoginResponse response = new LoginResponse();
        response.setAccessToken(tokenService.generateAccessToken(request.getEmail()));
        response.setExpiresIn(accessTokenExpiresInSeconds);
        response.setRefreshToken(tokenService.generateRefreshToken(request.getEmail()));
        response.setRefreshExpiresIn(refreshTokenExpiresInSeconds);

        return response;
    }
    /*
    @Override
    public LoginResponse refreshToken(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        int accessTokenExpiresInSeconds = tokenService.getAccessTokenExpiresInMilliseconds() / 1000;
        int refreshTokenExpiresInSeconds = tokenService.getRefreshTokenExpiresInMilliseconds() / 1000;

        LoginResponse response = new LoginResponse();
        response.setAccessToken(tokenService.generateAccessToken(request.getEmail()));
        response.setExpiresIn(accessTokenExpiresInSeconds);
        response.setRefreshToken(tokenService.generateRefreshToken(request.getEmail()));
        response.setRefreshExpiresIn(refreshTokenExpiresInSeconds);

        return response;
    }
    */
}
