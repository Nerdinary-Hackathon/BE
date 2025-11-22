package com.devpath.domain.user.contorller;

import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.dto.UserProfileResponse;
import com.devpath.global.apiPayload.ApiResponse;
import com.devpath.global.dto.CursorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "사용자 명함 관리 API")
public interface UserControllerDocs {

        @Operation(summary = "명함 조회", description = "유저 아이디로 명함 상세 정보를 조회합니다")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "명함 조회 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "COMMON_404: 검색 결과가 없습니다.")
        })
        ApiResponse<MyCardRes> getCard(String userId);

        @Operation(summary = "명함 목록 조회", description = "최신순으로 교환한 명함 목록을 조회합니다. (직군을 선택하여 필터링 가능)")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "명함 목록 조회 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "COMMON_404: 검색 결과가 없습니다.")
        })
        ApiResponse<CursorResponseDto<CardPrevRes>> getCards(String userId, String cursor, Integer size,
                        JobGroup jobGroup);

        @Operation(summary = "명함 교환", description = "명함 코드를 입력하면 명함이 교환됩니다.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "명함 교환 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "FOLLOW_400: 이미 추가한 명함입니다"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "COMMON_404: 검색 결과가 없습니다.")
        })
        ResponseEntity<ApiResponse<Void>> exchangeCard(String userId, String cardCode);

        @Operation(summary = "프로필 작성", description = "사용자의 프로필 정보를 작성합니다. 모든 필드는 필수이며, 기술 스택은 최소 1개 이상 선택해야 합니다.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "프로필 작성 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "USER_409: 이미 사용 중인 이메일입니다")
        })
        ResponseEntity<ApiResponse<UserProfileResponse>> createProfile(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "프로필 작성 정보", required = true, content = @Content(schema = @Schema(implementation = UserProfileRequest.class))) UserProfileRequest request);

}
