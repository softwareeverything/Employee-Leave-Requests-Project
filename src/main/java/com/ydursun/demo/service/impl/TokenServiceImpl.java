package com.ydursun.demo.service.impl;

import com.ydursun.demo.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.token.issuer.domain}")
    private String issuerDomain;

    @Value("${jwt.token.access.secret}")
    private String accessTokenSecretKey;

    @Value("${jwt.token.access.expire.milliseconds}")
    private int accessTokenExpireMilliseconds;

    @Value("${jwt.token.refresh.secret}")
    private String refreshTokenSecretKey;

    @Value("${jwt.token.refresh.expire.milliseconds}")
    private int refreshTokenExpireMilliseconds;

    @Override
    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenExpireMilliseconds, accessTokenSecretKey);
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenExpireMilliseconds, refreshTokenSecretKey);
    }

    @Override
    public Optional<String> getUserEmailFromAccessToken(String token) {
        return getUserEmailFromToken(token, accessTokenSecretKey);
    }

    @Override
    public Optional<String> getUserEmailFromRefreshToken(String token) {
        return getUserEmailFromToken(token, refreshTokenSecretKey);
    }

    @Override
    public int getAccessTokenExpiresInMilliseconds() {
        return accessTokenExpireMilliseconds;
    }

    @Override
    public int getRefreshTokenExpiresInMilliseconds() {
        return refreshTokenExpireMilliseconds;
    }

    @Override
    public Optional<String> getTokenFromHeaderToken(String authHeader) {
        final String bearer = "Bearer ";

        if (authHeader != null && authHeader.contains(bearer)) {
            return Optional.of(authHeader.substring(bearer.length()));
        }

        return Optional.empty();
    }

    private String generateToken(String userId, long addExpirationMilliseconds, String secretKey) {
        long currentTimeMillis = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuer(issuerDomain)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + addExpirationMilliseconds))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    private Optional<String> getUserEmailFromToken(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Optional.of(claims.getSubject());
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
