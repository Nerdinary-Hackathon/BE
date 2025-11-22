package com.devpath.domain.user.enums;

import com.devpath.global.apiPayload.code.status.GeneralErrorCode;
import com.devpath.global.apiPayload.exception.handler.GlobalHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static JobGroup from(String value) {
        for (JobGroup jobGroup : JobGroup.values()) {
            if (jobGroup.description.equals(value) || jobGroup.name().equals(value)) {
                return jobGroup;
            }
        }
        throw new GlobalHandler(GeneralErrorCode.JOB_GROUP_NOT_FOUND);
    }
}
