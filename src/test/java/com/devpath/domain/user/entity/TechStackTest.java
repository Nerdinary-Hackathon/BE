package com.devpath.domain.user.entity;

import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import com.devpath.domain.user.enums.TechStackName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TechStack 엔티티 테스트")
class TechStackTest {

    @Test
    @DisplayName("TechStack 빌더로 생성")
    void createTechStack_WithBuilder() {
        // given
        User user = createTestUser();

        // when
        TechStack techStack = TechStack.builder()
                .user(user)
                .techStackName(TechStackName.JAVA)
                .build();

        // then
        assertThat(techStack).isNotNull();
        assertThat(techStack.getUser()).isEqualTo(user);
        assertThat(techStack.getTechStackName()).isEqualTo(TechStackName.JAVA);
    }

    @Test
    @DisplayName("TechStack create 메서드로 생성")
    void createTechStack_WithCreateMethod() {
        // given
        User user = createTestUser();

        // when
        TechStack techStack = TechStack.create(user, TechStackName.SPRING);

        // then
        assertThat(techStack).isNotNull();
        assertThat(techStack.getUser()).isEqualTo(user);
        assertThat(techStack.getTechStackName()).isEqualTo(TechStackName.SPRING);
    }

    @Test
    @DisplayName("TechStack setUser 메서드")
    void setUser_Success() {
        // given
        User user1 = createTestUser();
        User user2 = User.builder()
                .name("User 2")
                .nickname("user2")
                .email("user2@example.com")
                .phone("010-9876-5432")
                .link("https://example2.com")
                .jobGroup(JobGroup.FRONTEND)
                .level(Level.SENIOR)
                .build();

        TechStack techStack = TechStack.builder()
                .user(user1)
                .techStackName(TechStackName.JAVA)
                .build();

        // when
        techStack.setUser(user2);

        // then
        assertThat(techStack.getUser()).isEqualTo(user2);
    }

    @Test
    @DisplayName("다양한 TechStackName으로 생성")
    void createTechStack_VariousNames() {
        // given
        User user = createTestUser();

        // when
        TechStack javaStack = TechStack.create(user, TechStackName.JAVA);
        TechStack springStack = TechStack.create(user, TechStackName.SPRING);
        TechStack mysqlStack = TechStack.create(user, TechStackName.MYSQL);

        // then
        assertThat(javaStack.getTechStackName()).isEqualTo(TechStackName.JAVA);
        assertThat(springStack.getTechStackName()).isEqualTo(TechStackName.SPRING);
        assertThat(mysqlStack.getTechStackName()).isEqualTo(TechStackName.MYSQL);
    }

    private User createTestUser() {
        return User.builder()
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();
    }
}
