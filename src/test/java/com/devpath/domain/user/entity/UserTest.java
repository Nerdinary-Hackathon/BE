package com.devpath.domain.user.entity;

import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import com.devpath.domain.user.enums.TechStackName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 엔티티 테스트")
class UserTest {

    @Test
    @DisplayName("User 빌더로 생성")
    void createUser_WithBuilder() {
        // when
        User user = User.builder()
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .profileImageUrl("https://example.com/profile.jpg")
                .build();

        // then
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getNickname()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPhone()).isEqualTo("010-1234-5678");
        assertThat(user.getLink()).isEqualTo("https://example.com");
        assertThat(user.getJobGroup()).isEqualTo(JobGroup.BACKEND);
        assertThat(user.getLevel()).isEqualTo(Level.JUNIOR);
        assertThat(user.getProfileImageUrl()).isEqualTo("https://example.com/profile.jpg");
    }

    @Test
    @DisplayName("TechStack 추가")
    void addTechStack_Success() {
        // given
        User user = User.builder()
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();

        TechStack techStack = TechStack.create(user, TechStackName.JAVA);

        // when
        user.addTechStack(techStack);

        // then
        assertThat(user.getTechStacks()).hasSize(1);
        assertThat(user.getTechStacks().get(0)).isEqualTo(techStack);
        assertThat(techStack.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("여러 TechStack 추가")
    void addMultipleTechStacks_Success() {
        // given
        User user = User.builder()
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();

        TechStack techStack1 = TechStack.create(user, TechStackName.JAVA);
        TechStack techStack2 = TechStack.create(user, TechStackName.SPRING);
        TechStack techStack3 = TechStack.create(user, TechStackName.MYSQL);

        // when
        user.addTechStack(techStack1);
        user.addTechStack(techStack2);
        user.addTechStack(techStack3);

        // then
        assertThat(user.getTechStacks()).hasSize(3);
        assertThat(user.getTechStacks())
                .extracting(TechStack::getTechStackName)
                .containsExactly(TechStackName.JAVA, TechStackName.SPRING, TechStackName.MYSQL);
    }
}
