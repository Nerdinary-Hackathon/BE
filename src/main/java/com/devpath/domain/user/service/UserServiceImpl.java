package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.TechStack;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.repository.UserRepository;
import com.devpath.global.apiPayload.code.status.GeneralErrorCode;
import com.devpath.global.apiPayload.exception.handler.GlobalHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
