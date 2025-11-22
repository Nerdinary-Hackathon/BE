package com.devpath.domain.user.contorller;

import com.devpath.domain.user.dto.CardPrevDto;
import com.devpath.domain.user.dto.MyCardDto;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.dto.UserProfileResponse;
import com.devpath.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "사용자 명함 관리 API")
public interface UserControllerDocs {

    @Operation(
            summary = "내 명함 조회",
            description = "현재 로그인한 사용자의 명함 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "명함 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    ApiResponse<MyCardDto> getMyCard();

    @Operation(
            summary = "명함 목록 조회",
            description = "최신순으로 교환한 명함 목록을 조회합니다. (직군을 선택하여 필터링 가능)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "명함 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    ApiResponse<CardPrevDto> getCards();

    @Operation(
            summary = "프로필 작성",
            description = "사용자의 프로필 정보를 작성합니다. 모든 필드는 필수이며, 기술 스택은 최소 1개 이상 선택해야 합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 작성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임 또는 이메일")
    })
    ApiResponse<UserProfileResponse> createProfile(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "프로필 작성 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserProfileRequest.class))
            )
            UserProfileRequest request
    );

}

