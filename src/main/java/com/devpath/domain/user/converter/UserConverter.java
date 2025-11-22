package com.devpath.domain.user.converter;

import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.TechStack;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.TechStackName;

import java.util.List;

public class UserConverter {
    public static MyCardRes toMyCardRes(User user) {
        List<TechStack> techStacks = user.getTechStacks();
        return MyCardRes.builder()
                .profileImg(user.getProfileImageUrl())
                .nickName(user.getNickname())
                .jobGroup(user.getJobGroup().toString())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .link(user.getLink())
                .level(user.getLevel().toString())
                .techStacks(techStacks.stream().map(TechStack::getTechStackName).map(TechStackName::name).toList())
                .build();
    }

    public static CardPrevRes toCardPrevRes(User user) {
        return CardPrevRes.builder()
                .userId(user.getId())
                .profileImg(user.getProfileImageUrl())
                .jobGroup(user.getJobGroup().toString())
                .nickName(user.getNickname())
                .build();
    }

    public static User toUser(UserProfileRequest request) {
        User user =  User.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .link(request.getLink())
                .jobGroup(request.getJobGroup())
                .level(request.getLevel())
                .build();

        // techStackName 리스트를 TechStack으로 변환
        request.getTechStackNames().stream()
                .map(techStackName -> TechStack.create(user, techStackName))
                .forEach(user::addTechStack);

        return user;
    }
}
