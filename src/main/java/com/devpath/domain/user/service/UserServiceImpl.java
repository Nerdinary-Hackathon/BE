package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.UserProfileRequest;

import com.devpath.domain.user.converter.UserConverter;
import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.entity.Follow;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.repository.FollowRepository;
import com.devpath.domain.user.repository.UserRepository;
import com.devpath.global.apiPayload.code.status.GeneralErrorCode;
import com.devpath.global.apiPayload.exception.handler.GlobalHandler;
import com.devpath.global.dto.CursorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    @Transactional
    public User createProfile(UserProfileRequest request) {

        // 이메일 중복 검증
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GlobalHandler(GeneralErrorCode.DUPLICATE_EMAIL);
        }

        // User 엔티티 생성
        User user = UserConverter.toUser(request);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public MyCardRes getMyCardRes(Long userId) {
        return UserConverter.toMyCardRes(checkUser(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponseDto<CardPrevRes> getCardPrevRes(Long userId, Long cursor, Integer size,
            JobGroup jobGroup) {

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        Slice<Follow> followSlice = followRepository.findNextByCursor(userId, cursor, jobGroup, pageable);

        Slice<CardPrevRes> result = followSlice.map(f -> UserConverter.toCardPrevRes(f.getFollower()));

        String nextCursor = followSlice.hasNext()
                ? String.valueOf(followSlice.getContent().get(followSlice.getNumberOfElements() - 1).getId())
                : null;

        return new CursorResponseDto<>(result, nextCursor);
    }

    @Transactional
    public void exchangeCard(Long userId, Long cardCode) {

        User user = checkUser(userId);
        User friend = checkUser(cardCode);

        if (followRepository.existsByUser_IdAndFollower_Id(userId, cardCode)) {
            throw new GlobalHandler(GeneralErrorCode.FOLLOW_ALREADY_EXISTED);
        }

        followRepository.save(Follow.builder()
                .user(user)
                .follower(friend)
                .build());

        followRepository.save(Follow.builder()
                .user(friend)
                .follower(user)
                .build());
    }

    /**
     * 사용자를 조회합니다.
     * @param uid 사용자 ID
     * @return User
     */
    private User checkUser(Long uid) {
        return userRepository.findById(uid).orElseThrow(() -> new GlobalHandler(GeneralErrorCode._NO_RESULTS_FOUND));
    }
}
