package com.devpath.domain.user.enums;

import lombok.Getter;

@Getter
public enum JobGroup {
    PM("기획(PM)"),
    DESIGNER("디자인"),
    WEB("웹"),
    BACKEND("백엔드"),
    ANDROID("안드로이드"),
    IOS("iOS");

    private final String description;

    JobGroup(String description) {
        this.description = description;
    }

}
