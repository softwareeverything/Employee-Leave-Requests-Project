package com.ydursun.demo.config;

import com.ydursun.demo.service.TokenService;
import com.ydursun.demo.service.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserManagementService userManagementService;

    public JwtTokenFilter(TokenService tokenService,
                          UserManagementService userManagementService) {
        this.tokenService = tokenService;
        this.userManagementService = userManagementService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String bearer = "Bearer ";

        if (authHeader != null && authHeader.contains(bearer) && SecurityContextHolder.getContext().getAuthentication() == null) {
            final String token = authHeader.substring(bearer.length());
            tokenService.getUserEmailFromAccessToken(token)
                    .ifPresent(username -> authenticateUserOnSpringSecurity(request, username));

        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserOnSpringSecurity(HttpServletRequest request, String username) {
        UserDetails userDetails = userManagementService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

}
