package com.devpath.domain.user.entity;

import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import jakarta.persistence.*;
import lombok.*;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private JobGroup jobGroup;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "link")
    private String link;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phone;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechStack> techStacks = new ArrayList<>();

    public void addTechStack(TechStack techStack) {
        techStacks.add(techStack);
        techStack.setUser(this);
    }
}
