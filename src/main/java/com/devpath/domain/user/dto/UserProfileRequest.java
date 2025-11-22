package com.devpath.domain.user.dto;

import com.devpath.domain.user.enums.JobGroup;
import com.devpath.domain.user.enums.Level;
import com.devpath.domain.user.enums.TechStackName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserProfileRequest {

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    @NotBlank(message = "전화번호는 필수입니다")
    private String phone;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "링크는 필수입니다")
    private String link;

    @NotNull(message = "직무를 선택해주세요")
    private JobGroup jobGroup;

    @NotNull(message = "경력을 선택해주세요")
    private Level level;

    @Size(min = 1, max = 3, message = "기술 스택은 1개 이상 3개 이하로 선택해야 합니다")
    private List<TechStackName> techStackNames;
}
