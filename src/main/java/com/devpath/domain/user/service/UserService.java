package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.global.dto.CursorResponseDto;

public interface UserService {
    MyCardRes getMyCardRes(String userId);
    CursorResponseDto<CardPrevRes> getCardPrevRes(String userId, String cursor, Integer size, JobGroup jobGroup);

    Void exchangeCard(String userId, String cardCode);
    User createProfile(UserProfileRequest request);
}
