package com.ydursun.demo.service;

import java.util.Optional;

public interface TokenService {

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    Optional<String> getUserEmailFromAccessToken(String token);

    Optional<String> getUserEmailFromRefreshToken(String token);

    int getAccessTokenExpiresInMilliseconds();

    int getRefreshTokenExpiresInMilliseconds();

    Optional<String> getTokenFromHeaderToken(String authHeader);

}
