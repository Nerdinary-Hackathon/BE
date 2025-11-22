package com.devpath.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyCardRes {
    private String profileImg;
    private String nickName;
    private String jobGroup;
    private List<String> techStacks;
    private String level;
    private String phoneNumber;
    private String email;
    private String link;
}
