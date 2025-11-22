package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.global.apiPayload.exception.handler.GlobalHandler;
import com.devpath.global.dto.CursorResponseDto;

public interface UserService {
    /**
     * 사용자의 본인 명함 상세 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID (PK)
     * @return 본인 명함 정보가 담긴 DTO (MyCardRes)
     */
    MyCardRes getMyCardRes(String userId);

    /**
     * 사용자가 보유한 명함(팔로우) 목록을 커서 기반 페이징으로 조회합니다.
     * 특정 직군(JobGroup)으로 필터링하여 조회할 수 있습니다.
     *
     * @param userId   사용자 ID
     * @param cursor   마지막으로 조회한 명함의 ID (첫 조회 시 null)
     * @param size     페이지 당 조회할 명함 개수
     * @param jobGroup 필터링할 직군 (전체 조회 시 null)
     * @return 다음 커서 정보와 명함 목록이 담긴 DTO
     */
    CursorResponseDto<CardPrevRes> getCardPrevRes(String userId, String cursor, Integer size, JobGroup jobGroup);

    /**
     * 다른 사용자와 명함을 교환합니다.
     * 서로의 명함을 저장하며, 이미 교환한 경우 예외가 발생합니다.
     *
     * @param userId   요청하는 사용자 ID
     * @param cardCode 교환할 상대방의 명함 코드 (User ID)
     */
    void exchangeCard(String userId, String cardCode);

    /**
     * 새로운 사용자 프로필을 생성합니다.
     * 이메일 중복 여부를 확인하고, 중복이 없을 경우 사용자를 DB에 저장합니다.
     *
     * @param request 사용자 생성 요청 정보 (이메일, 이름 등)
     * @return 생성된 User 엔티티
     * @throws GlobalHandler 이미 존재하는 이메일일 경우 발생
     */
    User createProfile(UserProfileRequest request);
}
