package com.devpath.global.s3.service;

import com.devpath.global.s3.dto.S3Dto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("S3Service 테스트")
class S3ServiceTest {

    @Mock
    private S3Presigner s3Presigner;

    @InjectMocks
    private S3Service s3Service;

    private static final String TEST_BUCKET = "test-bucket";
    private static final String TEST_PREFIX = "profile";
    private static final String TEST_FILENAME = "test-image.jpg";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucket", TEST_BUCKET);
    }

    @Test
    @DisplayName("PreSigned URL 생성 성공")
    void getPreSignedUrl_Success() throws Exception {
        // given
        URL mockUrl = new URL("https://test-bucket.s3.amazonaws.com/profile/test-key");
        PresignedPutObjectRequest mockPresignedRequest = mock(PresignedPutObjectRequest.class);

        when(mockPresignedRequest.url()).thenReturn(mockUrl);
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(mockPresignedRequest);

        // when
        S3Dto.PreSignedUrlResponse response = s3Service.getPreSignedUrl(TEST_PREFIX, TEST_FILENAME);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPreSignedUrl()).isNotNull();
        assertThat(response.getPreSignedUrl()).contains("test-bucket.s3.amazonaws.com");
        assertThat(response.getKey()).isNotNull();
        assertThat(response.getKey()).startsWith(TEST_PREFIX + "/");
        assertThat(response.getKey()).contains(TEST_FILENAME);

        verify(s3Presigner, times(1)).presignPutObject(any(PutObjectPresignRequest.class));
    }

    @Test
    @DisplayName("PreSigned URL - UUID가 포함된 키 생성 확인")
    void getPreSignedUrl_KeyContainsUUID() throws Exception {
        // given
        URL mockUrl = new URL("https://test-bucket.s3.amazonaws.com/profile/test-key");
        PresignedPutObjectRequest mockPresignedRequest = mock(PresignedPutObjectRequest.class);

        when(mockPresignedRequest.url()).thenReturn(mockUrl);
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(mockPresignedRequest);

        // when
        S3Dto.PreSignedUrlResponse response1 = s3Service.getPreSignedUrl(TEST_PREFIX, TEST_FILENAME);
        S3Dto.PreSignedUrlResponse response2 = s3Service.getPreSignedUrl(TEST_PREFIX, TEST_FILENAME);

        // then
        assertThat(response1.getKey()).isNotEqualTo(response2.getKey());
        assertThat(response1.getKey()).matches(TEST_PREFIX + "/[a-f0-9-]+_" + TEST_FILENAME);
        assertThat(response2.getKey()).matches(TEST_PREFIX + "/[a-f0-9-]+_" + TEST_FILENAME);

        verify(s3Presigner, times(2)).presignPutObject(any(PutObjectPresignRequest.class));
    }

    @Test
    @DisplayName("PreSigned URL - 다른 prefix로 생성")
    void getPreSignedUrl_DifferentPrefix() throws Exception {
        // given
        String customPrefix = "documents";
        String customFilename = "report.pdf";
        URL mockUrl = new URL("https://test-bucket.s3.amazonaws.com/documents/test-key");
        PresignedPutObjectRequest mockPresignedRequest = mock(PresignedPutObjectRequest.class);

        when(mockPresignedRequest.url()).thenReturn(mockUrl);
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(mockPresignedRequest);

        // when
        S3Dto.PreSignedUrlResponse response = s3Service.getPreSignedUrl(customPrefix, customFilename);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getKey()).startsWith(customPrefix + "/");
        assertThat(response.getKey()).contains(customFilename);

        verify(s3Presigner, times(1)).presignPutObject(any(PutObjectPresignRequest.class));
    }
}
