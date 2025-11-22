package com.devpath.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = true) // Nullable for OAuth2 users
    private String passwordHash;

    private String nickname;

    @Column(name = "github_username")
    private String githubUsername;

    @Column(columnDefinition = "TEXT")
    private String githubTokenEncrypted;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN

    private String provider; // google, github

    private String providerId; // sub, id

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String passwordHash, String nickname, Role role, String provider, String providerId,
            String profileImageUrl) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.role = role != null ? role : Role.USER;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateGithubInfo(String githubUsername, String githubTokenEncrypted) {
        this.githubUsername = githubUsername;
        this.githubTokenEncrypted = githubTokenEncrypted;
    }
}
