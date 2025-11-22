package com.devpath.domain.user.entity;

import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Follow 엔티티 테스트")
class FollowTest {

    @Test
    @DisplayName("Follow 빌더로 생성")
    void createFollow_WithBuilder() {
        // given
        User user = createTestUser(1L, "user1@example.com", "user1");
        User follower = createTestUser(2L, "user2@example.com", "user2");

        // when
        Follow follow = Follow.builder()
                .user(user)
                .follower(follower)
                .build();

        // then
        assertThat(follow).isNotNull();
        assertThat(follow.getUser()).isEqualTo(user);
        assertThat(follow.getFollower()).isEqualTo(follower);
    }

    @Test
    @DisplayName("Follow 관계 확인")
    void followRelationship_Check() {
        // given
        User user1 = createTestUser(1L, "user1@example.com", "user1");
        User user2 = createTestUser(2L, "user2@example.com", "user2");

        // when
        Follow follow = Follow.builder()
                .user(user1)
                .follower(user2)
                .build();

        // then
        assertThat(follow.getUser().getNickname()).isEqualTo("user1");
        assertThat(follow.getFollower().getNickname()).isEqualTo("user2");
    }

    @Test
    @DisplayName("양방향 Follow 관계")
    void bidirectionalFollow_Check() {
        // given
        User user1 = createTestUser(1L, "user1@example.com", "user1");
        User user2 = createTestUser(2L, "user2@example.com", "user2");

        // when
        Follow follow1 = Follow.builder()
                .user(user1)
                .follower(user2)
                .build();

        Follow follow2 = Follow.builder()
                .user(user2)
                .follower(user1)
                .build();

        // then
        assertThat(follow1.getUser()).isEqualTo(user1);
        assertThat(follow1.getFollower()).isEqualTo(user2);
        assertThat(follow2.getUser()).isEqualTo(user2);
        assertThat(follow2.getFollower()).isEqualTo(user1);
    }

    private User createTestUser(Long id, String email, String nickname) {
        return User.builder()
                .id(id)
                .name("Test User")
                .nickname(nickname)
                .email(email)
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();
    }
}
