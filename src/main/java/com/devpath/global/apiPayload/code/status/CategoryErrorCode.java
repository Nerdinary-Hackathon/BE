package com.devpath.global.apiPayload.code.status;

import com.devpath.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode implements BaseErrorCode {
    CATEGORY_NOT_EXISTED(HttpStatus.NOT_FOUND,"CATEGORY_400","존재하지 않는 카테고리입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
