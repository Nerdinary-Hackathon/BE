package com.devpath.domain.user.controller;

import com.devpath.domain.user.dto.AuthResponse;
import com.devpath.domain.user.dto.LoginRequest;
import com.devpath.domain.user.dto.RegisterRequest;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.service.UserService;
import com.devpath.global.apiPayload.ApiResponse;
import com.devpath.global.apiPayload.code.status.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED,user.getUserId().toString());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 토큰을 발급받습니다.")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ApiResponse.onSuccess(GeneralSuccessCode._OK,response);
    }
}
