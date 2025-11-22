package com.devpath.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardPrevRes {
    private String profileImg;
    private String nickName;
    private String jobGroup;
}
