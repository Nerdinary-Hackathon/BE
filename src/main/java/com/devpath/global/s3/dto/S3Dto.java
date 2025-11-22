package com.devpath.global.s3.dto;

import lombok.Builder;
import lombok.Getter;

public class S3Dto {

    @Getter
    @Builder
    public static class PreSignedUrlResponse {
        private String preSignedUrl;
        private String key;
    }
}
