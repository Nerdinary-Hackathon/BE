package com.devpath.domain.user.enums;

public enum Level {
    JOB_SEEKING("취업준비중"),
    ENTRY("신입(1년 미만)"),
    JUNIOR("주니어(1~3년)"),
    SENIOR("시니어(3년 이상)");

    private final String description;

    Level(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
