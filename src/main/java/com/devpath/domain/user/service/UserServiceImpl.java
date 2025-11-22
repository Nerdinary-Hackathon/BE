package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.TechStack;
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
        User user = User.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .link(request.getLink())
                .jobGroup(request.getJobGroup())
                .level(request.getLevel())
                .build();

        // TechStack 엔티티들 생성 및 연결
        request.getTechStackNames().forEach(techStackName -> {
            TechStack techStack = TechStack.builder()
                    .user(user)
                    .techStackName(techStackName)
                    .build();
            user.addTechStack(techStack);
        });

        // User 저장 (TechStack은 cascade로 함께 저장됨)
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public MyCardRes getMyCardRes(String userId) {
        Long uid = Long.valueOf(userId);
        return UserConverter.toMyCardRes(checkUser(uid));
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponseDto<CardPrevRes> getCardPrevRes(String userId, String cursor, Integer size, JobGroup jobGroup) {

        Long uid = Long.valueOf(userId);
        Long cursorId = cursor != null ? Long.valueOf(cursor) : null;

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        Slice<Follow> followSlice = followRepository.findNextByCursor(uid, cursorId, jobGroup, pageable);

        Slice<CardPrevRes> result = followSlice.map(f -> UserConverter.toCardPrevRes(f.getFollower()));

        String nextCursor = followSlice.hasNext()
                ? String.valueOf(followSlice.getContent().get(followSlice.getNumberOfElements() - 1).getId())
                : null;

        return new CursorResponseDto<>(result, nextCursor);
    }

    @Transactional
    public void exchangeCard(String userId, String cardCode) {
        Long uid = Long.valueOf(userId);
        Long fid = Long.valueOf(cardCode);

        User user = checkUser(uid);
        User friend = checkUser(fid);

        if(followRepository.existsByUser_IdAndFollower_Id(uid, fid)){
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

    private User checkUser(Long uid) {
        return userRepository.findById(uid).orElseThrow(() -> new GlobalHandler(GeneralErrorCode._NO_RESULTS_FOUND));
    }
}
