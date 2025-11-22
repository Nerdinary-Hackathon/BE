package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.Follow;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import com.devpath.domain.user.enums.TechStackName;
import com.devpath.domain.user.repository.FollowRepository;
import com.devpath.domain.user.repository.UserRepository;
import com.devpath.global.apiPayload.exception.handler.GlobalHandler;
import com.devpath.global.dto.CursorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserProfileRequest userProfileRequest;
    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        userProfileRequest = createUserProfileRequest();
        testUser = createTestUser(1L, "test@example.com");
        testUser2 = createTestUser(2L, "test2@example.com");
    }

    @Test
    @DisplayName("프로필 생성 성공")
    void createProfile_Success() {
        // given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User result = userService.createProfile(userProfileRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("프로필 생성 실패 - 이메일 중복")
    void createProfile_DuplicateEmail() {
        // given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createProfile(userProfileRequest))
                .isInstanceOf(GlobalHandler.class);
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("내 명함 조회 성공")
    void getMyCardRes_Success() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        // when
        MyCardRes result = userService.getMyCardRes(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo(testUser.getNickname());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("내 명함 조회 실패 - 사용자 없음")
    void getMyCardRes_UserNotFound() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getMyCardRes(1L))
                .isInstanceOf(GlobalHandler.class);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("명함 목록 조회 성공")
    void getCardPrevRes_Success() {
        // given
        Long userId = 1L;
        Long cursor = null;
        Integer size = 10;
        JobGroup jobGroup = null;

        Follow follow1 = createFollow(1L, testUser, testUser2);
        List<Follow> follows = Arrays.asList(follow1);
        Slice<Follow> followSlice = new SliceImpl<>(follows, PageRequest.of(0, size), false);

        when(followRepository.findNextByCursor(anyLong(), any(), any(), any(Pageable.class)))
                .thenReturn(followSlice);

        // when
        CursorResponseDto<CardPrevRes> result = userService.getCardPrevRes(userId, cursor, size, jobGroup);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getData().getContent()).hasSize(1);
        assertThat(result.getNextCursor()).isNull();
        verify(followRepository, times(1)).findNextByCursor(anyLong(), any(), any(), any(Pageable.class));
    }

    @Test
    @DisplayName("명함 교환 성공")
    void exchangeCard_Success() {
        // given
        Long userId = 1L;
        Long cardCode = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(cardCode)).thenReturn(Optional.of(testUser2));
        when(followRepository.existsByUser_IdAndFollower_Id(userId, cardCode)).thenReturn(false);
        when(followRepository.save(any(Follow.class))).thenReturn(new Follow());

        // when
        userService.exchangeCard(userId, cardCode);

        // then
        verify(userRepository, times(2)).findById(anyLong());
        verify(followRepository, times(1)).existsByUser_IdAndFollower_Id(userId, cardCode);
        verify(followRepository, times(2)).save(any(Follow.class));
    }

    @Test
    @DisplayName("명함 교환 실패 - 이미 팔로우 중")
    void exchangeCard_AlreadyFollowing() {
        // given
        Long userId = 1L;
        Long cardCode = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(cardCode)).thenReturn(Optional.of(testUser2));
        when(followRepository.existsByUser_IdAndFollower_Id(userId, cardCode)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.exchangeCard(userId, cardCode))
                .isInstanceOf(GlobalHandler.class);
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    @DisplayName("명함 교환 실패 - 사용자 없음")
    void exchangeCard_UserNotFound() {
        // given
        Long userId = 1L;
        Long cardCode = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(cardCode)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.exchangeCard(userId, cardCode))
                .isInstanceOf(GlobalHandler.class);
        verify(followRepository, never()).save(any(Follow.class));
    }

    private UserProfileRequest createUserProfileRequest() {
        return new UserProfileRequest();
    }

    private User createTestUser(Long id, String email) {
        return User.builder()
                .id(id)
                .name("Test User")
                .nickname("testuser")
                .email(email)
                .phone("010-1234-5678")
                .link("https://example.com")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();
    }

    private Follow createFollow(Long id, User user, User follower) {
        return Follow.builder()
                .id(id)
                .user(user)
                .follower(follower)
                .build();
    }
}
