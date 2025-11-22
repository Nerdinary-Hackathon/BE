package com.devpath.domain.user.repository;

import com.devpath.domain.user.entity.Follow;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("FollowRepository 테스트")
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("User 1")
                .nickname("user1")
                .email("user1@example.com")
                .phone("010-1111-1111")
                .link("https://example1.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();

        user2 = User.builder()
                .name("User 2")
                .nickname("user2")
                .email("user2@example.com")
                .phone("010-2222-2222")
                .link("https://example2.com")
                .jobGroup(JobGroup.FRONTEND)
                .level(Level.SENIOR)
                .build();

        user3 = User.builder()
                .name("User 3")
                .nickname("user3")
                .email("user3@example.com")
                .phone("010-3333-3333")
                .link("https://example3.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();

        user1 = entityManager.persistAndFlush(user1);
        user2 = entityManager.persistAndFlush(user2);
        user3 = entityManager.persistAndFlush(user3);
    }

    @Test
    @DisplayName("팔로우 저장 테스트")
    void save_Success() {
        // given
        Follow follow = Follow.builder()
                .user(user1)
                .follower(user2)
                .build();

        // when
        Follow savedFollow = followRepository.save(follow);

        // then
        assertThat(savedFollow.getId()).isNotNull();
        assertThat(savedFollow.getUser().getId()).isEqualTo(user1.getId());
        assertThat(savedFollow.getFollower().getId()).isEqualTo(user2.getId());
    }

    @Test
    @DisplayName("팔로우 존재 여부 확인 - 존재함")
    void existsByUserIdAndFollowerId_True() {
        // given
        Follow follow = Follow.builder()
                .user(user1)
                .follower(user2)
                .build();
        entityManager.persistAndFlush(follow);

        // when
        boolean exists = followRepository.existsByUser_IdAndFollower_Id(user1.getId(), user2.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("팔로우 존재 여부 확인 - 존재하지 않음")
    void existsByUserIdAndFollowerId_False() {
        // when
        boolean exists = followRepository.existsByUser_IdAndFollower_Id(user1.getId(), user2.getId());

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("커서 기반 팔로우 목록 조회 - 필터 없음")
    void findNextByCursor_NoFilter() {
        // given
        Follow follow1 = Follow.builder()
                .user(user1)
                .follower(user2)
                .build();
        Follow follow2 = Follow.builder()
                .user(user1)
                .follower(user3)
                .build();

        entityManager.persistAndFlush(follow1);
        entityManager.persistAndFlush(follow2);

        // when
        Slice<Follow> result = followRepository.findNextByCursor(
                user1.getId(),
                null,
                null,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // then
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("커서 기반 팔로우 목록 조회 - JobGroup 필터")
    void findNextByCursor_WithJobGroupFilter() {
        // given
        Follow follow1 = Follow.builder()
                .user(user1)
                .follower(user2) // FRONTEND
                .build();
        Follow follow2 = Follow.builder()
                .user(user1)
                .follower(user3) // BACKEND
                .build();

        entityManager.persistAndFlush(follow1);
        entityManager.persistAndFlush(follow2);

        // when
        Slice<Follow> result = followRepository.findNextByCursor(
                user1.getId(),
                null,
                JobGroup.BACKEND,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFollower().getJobGroup()).isEqualTo(JobGroup.BACKEND);
    }

    @Test
    @DisplayName("커서 기반 팔로우 목록 조회 - 커서 적용")
    void findNextByCursor_WithCursor() {
        // given
        Follow follow1 = Follow.builder()
                .user(user1)
                .follower(user2)
                .build();
        Follow follow2 = Follow.builder()
                .user(user1)
                .follower(user3)
                .build();

        follow1 = entityManager.persistAndFlush(follow1);
        follow2 = entityManager.persistAndFlush(follow2);

        Long cursor = follow2.getId();

        // when
        Slice<Follow> result = followRepository.findNextByCursor(
                user1.getId(),
                cursor,
                null,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isLessThan(cursor);
    }
}
