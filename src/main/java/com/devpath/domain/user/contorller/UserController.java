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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devpath.global.dto.CursorResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    /**
     * 사용자의 명함 정보를 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return ApiResponse<MyCardRes>
     */
    @GetMapping("/{user-Id}")
    public ApiResponse<MyCardRes> getCard(@PathVariable("user-Id") Long userId) {
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, userService.getMyCardRes(userId));
    }

    /**
     * 사용자들의 명함 목록을 조회합니다.
     * 
     * @param userId   사용자 ID
     * @param cursor   커서
     * @param size     페이지 사이즈
     * @param jobGroup 직군
     * @return ApiResponse<CursorResponseDto<CardPrevRes>>
     */
    @Override
    @GetMapping("/cards")
    public ApiResponse<CursorResponseDto<CardPrevRes>> getCards(@RequestHeader Long userId,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) JobGroup jobGroup) {
        return ApiResponse.onSuccess(GeneralSuccessCode._OK,
                userService.getCardPrevRes(userId, cursor, size, jobGroup));
    }

    /**
     * 명함을 교환합니다.
     * 
     * @param userId   사용자 ID
     * @param cardCode 교환할 명함 코드
     * @return ResponseEntity<ApiResponse<Void>>
     */
    @PostMapping("/exchange-card")
    @Override
    public ResponseEntity<ApiResponse<Void>> exchangeCard(@RequestHeader Long userId, @RequestParam Long cardCode) {
        userService.exchangeCard(userId, cardCode);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.onSuccess(GeneralSuccessCode._CREATED, null));
    }

    /**
     * 사용자 프로필을 생성합니다.
     * 
     * @param request 프로필 생성 요청 DTO
     * @return ResponseEntity<ApiResponse<UserProfileResponse>>
     */
    @Override
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> createProfile(
            @Valid @RequestBody UserProfileRequest request) {
        User user = userService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.onSuccess(GeneralSuccessCode._CREATED, UserProfileResponse.from(user)));
    }
}
