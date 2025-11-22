package com.devpath.domain.user.repository;

import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();
    }

    @Test
    @DisplayName("사용자 저장 테스트")
    void save_Success() {
        // when
        User savedUser = userRepository.save(testUser);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getNickname()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    void findById_Success() {
        // given
        User savedUser = entityManager.persistAndFlush(testUser);

        // when
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재함")
    void existsByEmail_True() {
        // given
        entityManager.persistAndFlush(testUser);

        // when
        boolean exists = userRepository.existsByEmail("test@example.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하지 않음")
    void existsByEmail_False() {
        // when
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void delete_Success() {
        // given
        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        // when
        userRepository.delete(savedUser);
        entityManager.flush();

        // then
        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();
    }

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void findAll_Success() {
        // given
        User user1 = entityManager.persistAndFlush(testUser);
        User user2 = User.builder()
                .name("Test User 2")
                .nickname("testuser2")
                .email("test2@example.com")
                .phone("010-9876-5432")
                .link("https://example2.com")
                .jobGroup(JobGroup.FRONTEND)
                .level(Level.SENIOR)
                .build();
        entityManager.persistAndFlush(user2);

        // when
        var users = userRepository.findAll();

        // then
        assertThat(users).hasSize(2);
    }
}
