package com.devpath.domain.user.enums;

import com.devpath.global.apiPayload.code.status.GeneralErrorCode;
import com.devpath.global.apiPayload.exception.handler.GlobalHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TechStackName {

    REACT("React"),
    VUE("Vue.js"),
    NextJS("Next.js"),
    TypeScript("TypeScript"),
    Flutter("Flutter"),

    Java("Java"),
    Spring("Spring"),
    Python("Python"),
    Django("Django"),
    NodeJS("Node.js"),

    Figma("Figma"),
    Photoshop("Photoshop"),
    Sketch("Sketch"),
    THREE_D("3D"),
    Illustrator("Illustrator"),

    AWS("AWS"),
    Docker("Docker"),
    Kubernetes("Kubernetes"),
    Git("Git");

    private final String description;

    TechStackName(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static TechStackName from(String value) {
        for (TechStackName techStack : TechStackName.values()) {
            if (techStack.description.equals(value)) {
                return techStack;
            }
        }
        throw new GlobalHandler(GeneralErrorCode.TECH_STACK_NOT_FOUND);
    }
}
