package com.devpath.domain.user.converter;

import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.entity.User;

public class UserConverter {
    public static MyCardRes toMyCardRes(User user) {
        return MyCardRes.builder()
                .profileImg(user.getProfileImageUrl())
                .nickName(user.getNickname())
                .jobGroup(user.getJobGroup().toString())
                .build();
    }

    public static CardPrevRes toCardPrevRes(User user) {
        return CardPrevRes.builder()
                .profileImg(user.getProfileImageUrl())
                .jobGroup(user.getJobGroup().toString())
                .build();
    }
}
