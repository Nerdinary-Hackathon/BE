package com.devpath.domain.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Level {
    JOB_SEEKING("취업준비중"),
    ENTRY("신입(1년 미만)"),
    JUNIOR("주니어(1~3년)"),
    SENIOR("시니어(3년 이상)");

    private final String description;

    Level(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static Level from(String value) {
        for (Level level : Level.values()) {
            if (level.description.equals(value) || level.name().equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown level: " + value);
    }
}
