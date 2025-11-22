package com.devpath.domain.user.controller;

import com.devpath.domain.user.contorller.UserController;
import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import com.devpath.domain.user.enums.TechStackName;
import com.devpath.domain.user.service.UserService;
import com.devpath.global.dto.CursorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private MyCardRes myCardRes;
    private User testUser;

    @BeforeEach
    void setUp() {
        myCardRes = MyCardRes.builder()
                .profileImg("https://example.com/profile.jpg")
                .nickName("testuser")
                .jobGroup("BACKEND")
                .techStacks(Arrays.asList("JAVA", "SPRING"))
                .level("JUNIOR")
                .phoneNumber("010-1234-5678")
                .email("test@example.com")
                .link("https://example.com")
                .build();

        testUser = User.builder()
                .id(1L)
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
    @DisplayName("명함 조회 API 테스트")
    void getCard_Success() throws Exception {
        // given
        Long userId = 1L;
        when(userService.getMyCardRes(userId)).thenReturn(myCardRes);

        // when & then
        mockMvc.perform(get("/api/v1/users/{user-Id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.nickName").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(userService, times(1)).getMyCardRes(userId);
    }

    @Test
    @DisplayName("명함 목록 조회 API 테스트")
    void getCards_Success() throws Exception {
        // given
        Long userId = 1L;
        CardPrevRes cardPrevRes = CardPrevRes.builder()
                .userId(2L)
                .profileImg("https://example.com/profile2.jpg")
                .nickName("testuser2")
                .jobGroup("FRONTEND")
                .build();

        CursorResponseDto<CardPrevRes> response = new CursorResponseDto<>(
                new SliceImpl<>(Arrays.asList(cardPrevRes), PageRequest.of(0, 10), false),
                null
        );

        when(userService.getCardPrevRes(anyLong(), any(), anyInt(), any())).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/users/cards")
                        .header("userId", userId)
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.data.content[0].nickName").value("testuser2"));

        verify(userService, times(1)).getCardPrevRes(anyLong(), any(), anyInt(), any());
    }

    @Test
    @DisplayName("명함 교환 API 테스트")
    void exchangeCard_Success() throws Exception {
        // given
        Long userId = 1L;
        Long cardCode = 2L;

        doNothing().when(userService).exchangeCard(userId, cardCode);

        // when & then
        mockMvc.perform(post("/api/v1/users/exchange-card")
                        .header("userId", userId)
                        .param("cardCode", String.valueOf(cardCode)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true));

        verify(userService, times(1)).exchangeCard(userId, cardCode);
    }

    @Test
    @DisplayName("프로필 생성 API 테스트")
    void createProfile_Success() throws Exception {
        // given
        UserProfileRequest request = new UserProfileRequest();
        when(userService.createProfile(any(UserProfileRequest.class))).thenReturn(testUser);

        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/v1/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true));

        verify(userService, times(1)).createProfile(any(UserProfileRequest.class));
    }

    @Test
    @DisplayName("프로필 생성 API 테스트 - 유효성 검증 실패")
    void createProfile_ValidationFail() throws Exception {
        // given - 빈 요청 객체 (필수 필드가 누락됨)
        UserProfileRequest request = new UserProfileRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/v1/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, never()).createProfile(any(UserProfileRequest.class));
    }
}
