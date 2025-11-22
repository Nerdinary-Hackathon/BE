package com.devpath.domain.user.enums;

import lombok.Getter;

@Getter
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

}
