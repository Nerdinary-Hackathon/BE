package com.devpath.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class CursorResponseDto<T> {

    @Schema(description = "데이터 리스트")
    private List<T> content;

    @Schema(description = "현재 페이지의 데이터 개수", example = "10")
    private int pageSize;

    @Schema(description = "다음 페이지를 위한 커서 값 (응답의 마지막 아이템의 식별자)", example = "123")
    private String nextCursor;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    public CursorResponseDto(Slice<T> slice, String nextCursor) {
        this.content = slice.getContent();
        this.pageSize = slice.getNumberOfElements();
        this.nextCursor = nextCursor;
        this.hasNext = slice.hasNext();
    }
}