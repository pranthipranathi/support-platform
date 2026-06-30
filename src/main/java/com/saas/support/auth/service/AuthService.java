package com.saas.support.auth.service;

import com.saas.support.auth.dto.LoginRequest;
import com.saas.support.auth.dto.LoginResponse;
import com.saas.support.auth.dto.RefreshTokenRequest;
import com.saas.support.common.exception.BusinessException;
import com.saas.support.tenant.context.TenantContext;
import com.saas.support.user.entity.User;
import com.saas.support.user.repository.UserRepository;
import com.saas.support.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        String tenantId = TenantContext.getTenantId();

        String accessToken = jwtUtil.generateAccessToken(user, tenantId);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        log.info("User logged in: {} for tenant: {}", user.getEmail(), tenantId);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtUtil.extractUsername(request.getRefreshToken());
        String tenantId = TenantContext.getTenantId();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!jwtUtil.isTokenValid(request.getRefreshToken(), user)) {
            throw new BusinessException("Invalid refresh token");
        }

        String accessToken = jwtUtil.generateAccessToken(user, tenantId);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(86400000)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}