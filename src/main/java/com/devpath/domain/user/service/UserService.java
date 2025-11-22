package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.AuthResponse;
import com.devpath.domain.user.dto.LoginRequest;
import com.devpath.domain.user.dto.RegisterRequest;
import com.devpath.domain.user.repository.UserRepository;
import com.devpath.domain.user.entity.Role;
import com.devpath.domain.user.entity.User;
import com.devpath.global.apiPayload.code.BaseSuccessCode;
import com.devpath.global.apiPayload.code.status.GeneralErrorCode;
import com.devpath.global.apiPayload.exception.GeneralException;
import com.devpath.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new GeneralException(GeneralErrorCode.UNEXPECTED_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        // Refresh Token implementation can be added here

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken("not-implemented-yet")
                .expiresIn(3600000) // 1 hour
                .build();
    }
}
