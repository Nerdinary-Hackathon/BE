package com.devpath.domain.user.contorller;

import com.devpath.domain.user.dto.CardPrevDto;
import com.devpath.domain.user.dto.MyCardDto;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.dto.UserProfileResponse;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.service.UserService;
import com.devpath.global.apiPayload.ApiResponse;
import com.devpath.global.apiPayload.code.status.GeneralSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userProfileCommandService;

    @Override
    public ApiResponse<MyCardDto> getMyCard() {
        return null;
    }

    @Override
    public ApiResponse<CardPrevDto> getCards() {
        return null;
    }

    @Override
    @PostMapping("/profile")
    public ApiResponse<UserProfileResponse> createProfile(
            @Valid @RequestBody UserProfileRequest request
    ) {
        User user = userProfileCommandService.createProfile(request);
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, UserProfileResponse.from(user));
    }
}

