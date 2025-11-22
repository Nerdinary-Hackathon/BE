package com.devpath.domain.user.enums;

import lombok.Getter;

@Getter
public enum TechStackName {

    REACT("React"),
    VUEJS("Vue.js"),
    NEXTJS("Next.js"),
    TYPESCRIPT("TypeScript"),
    FLUTTER("Flutter"),

    JAVA("Java"),
    SPRING("Spring"),
    PYTHON("Python"),
    DJANGO("Django"),
    NODEJS("Node.js"),

    FIGMA("Figma"),
    PHOTOSHOP("Photoshop"),
    SKETCH("Sketch"),
    THREE_D("3D"),
    ILLUSTRATOR("Illustrator"),

    AWS("AWS"),
    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    GIT("Git");

    private final String description;

    TechStackName(String description) {
        this.description = description;
    }

}
