package com.devpath.domain.user.contorller;

import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.dto.UserProfileResponse;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.service.UserService;
import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.global.apiPayload.ApiResponse;
import com.devpath.global.apiPayload.code.status.GeneralSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.devpath.global.dto.CursorResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;

    @GetMapping("/{user-Id}")
    public ApiResponse<MyCardRes> getCard(@PathVariable("user-Id")String userId) {
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, userService.getMyCardRes(userId));
    }

    @Override
    @GetMapping("/cards")
    public ApiResponse<CursorResponseDto<CardPrevRes>> getCards(@RequestHeader String userId,
                                                                @RequestParam(required = false) String cursor,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                @RequestParam(required = false) JobGroup jobGroup) {
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, userService.getCardPrevRes(userId, cursor, size, jobGroup));
    }

    @PostMapping("/exchange-card")
    @Override
    public ApiResponse<String> exchangeCard(@RequestHeader String userId, @RequestParam String cardCode) {
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, userService.exchangeCard(userId, cardCode));
    }

    @Override
    @PostMapping("/profile")
    public ApiResponse<UserProfileResponse> createProfile(
            @Valid @RequestBody UserProfileRequest request
    ) {
        User user = userService.createProfile(request);
        return ApiResponse.onSuccess(GeneralSuccessCode._CREATED, UserProfileResponse.from(user));
    }
}
