package com.devpath.domain.user.converter;

import com.devpath.domain.user.dto.CardPrevRes;
import com.devpath.domain.user.dto.MyCardRes;
import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.TechStack;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import com.devpath.domain.user.enums.TechStackName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserConverter 테스트")
class UserConverterTest {

    @Test
    @DisplayName("User를 MyCardRes로 변환")
    void toMyCardRes_Success() {
        // given
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .profileImageUrl("https://example.com/profile.jpg")
                .jobGroup(JobGroup.BACKEND)
                .level(Level.JUNIOR)
                .build();

        TechStack techStack1 = TechStack.create(user, TechStackName.JAVA);
        TechStack techStack2 = TechStack.create(user, TechStackName.SPRING);
        user.addTechStack(techStack1);
        user.addTechStack(techStack2);

        // when
        MyCardRes result = UserConverter.toMyCardRes(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(result.getLink()).isEqualTo("https://example.com");
        assertThat(result.getProfileImg()).isEqualTo("https://example.com/profile.jpg");
        assertThat(result.getJobGroup()).isEqualTo("BACKEND");
        assertThat(result.getLevel()).isEqualTo("JUNIOR");
        assertThat(result.getTechStacks()).hasSize(2);
        assertThat(result.getTechStacks()).contains("JAVA", "SPRING");
    }

    @Test
    @DisplayName("User를 CardPrevRes로 변환")
    void toCardPrevRes_Success() {
        // given
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .nickname("testuser")
                .email("test@example.com")
                .phone("010-1234-5678")
                .link("https://example.com")
                .profileImageUrl("https://example.com/profile.jpg")
                .jobGroup(JobGroup.FRONTEND)
                .level(Level.SENIOR)
                .build();

        // when
        CardPrevRes result = UserConverter.toCardPrevRes(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getNickName()).isEqualTo("testuser");
        assertThat(result.getProfileImg()).isEqualTo("https://example.com/profile.jpg");
        assertThat(result.getJobGroup()).isEqualTo("FRONTEND");
    }

    @Test
    @DisplayName("UserProfileRequest를 User로 변환")
    void toUser_Success() {
        // given
        UserProfileRequest request = createUserProfileRequest();

        // when
        User result = UserConverter.toUser(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getNickname()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getPhone()).isEqualTo("010-1234-5678");
        assertThat(result.getLink()).isEqualTo("https://example.com");
        assertThat(result.getJobGroup()).isEqualTo(JobGroup.BACKEND);
        assertThat(result.getLevel()).isEqualTo(Level.JUNIOR);
        assertThat(result.getTechStacks()).hasSize(2);
    }

    @Test
    @DisplayName("UserProfileRequest를 User로 변환 - 기술스택 포함")
    void toUser_WithTechStacks() {
        // given
        UserProfileRequest request = createUserProfileRequest();

        // when
        User result = UserConverter.toUser(request);

        // then
        assertThat(result.getTechStacks()).isNotEmpty();
        assertThat(result.getTechStacks()).hasSize(2);
        assertThat(result.getTechStacks())
                .extracting(TechStack::getTechStackName)
                .containsExactlyInAnyOrder(TechStackName.JAVA, TechStackName.SPRING);
    }

    private UserProfileRequest createUserProfileRequest() {
        return new UserProfileRequest() {
            @Override
            public String getName() {
                return "Test User";
            }

            @Override
            public String getNickname() {
                return "testuser";
            }

            @Override
            public String getEmail() {
                return "test@example.com";
            }

            @Override
            public String getPhone() {
                return "010-1234-5678";
            }

            @Override
            public String getLink() {
                return "https://example.com";
            }

            @Override
            public JobGroup getJobGroup() {
                return JobGroup.BACKEND;
            }

            @Override
            public Level getLevel() {
                return Level.JUNIOR;
            }

            @Override
            public List<TechStackName> getTechStackNames() {
                return Arrays.asList(TechStackName.JAVA, TechStackName.SPRING);
            }
        };
    }
}
