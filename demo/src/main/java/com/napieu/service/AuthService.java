package com.napieu.service;

import com.napieu.dto.AuthResponse;
import com.napieu.dto.LoginRequest;
import com.napieu.model.User;
import com.napieu.repository.UserRepository;
import com.napieu.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(accessToken, refreshToken, user.getUsername(), user.getRole());
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            String newAccessToken = tokenProvider.generateAccessToken(authentication);

            return new AuthResponse(newAccessToken, refreshToken, user.getUsername(), user.getRole());
        }

        throw new RuntimeException("Invalid refresh token");
    }
}