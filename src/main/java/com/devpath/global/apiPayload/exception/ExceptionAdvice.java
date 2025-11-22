package com.devpath.global.apiPayload.exception;

import com.devpath.global.apiPayload.ApiResponse;
import com.devpath.global.apiPayload.code.BaseErrorCode;
import com.devpath.global.apiPayload.code.status.GeneralErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(e, GeneralErrorCode.valueOf(errorMessage), HttpHeaders.EMPTY, request);
    }

    // @validation 예외처리

    /*
       @param headers, status, request -> 'Not annotated parameter overrides @NonNullApi parameter'
       -> ResponseEntityExceptionHandler를 포함한 스프링 6의 많은 코어 패키지들은 package-info.java 파일을 통해 @NonNullApi 어노테이션이 붙어있습니다.
          이는 "이 패키지의 모든 파라미터와 반환 값은 기본적으로 @NonNull(널 불허)이다"라고 선언하는 것입니다.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, GeneralErrorCode.valueOf("_BAD_REQUEST"), request, errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        /*
 todo : e.printStackTrace() vs. log.error()

e.printStackTrace()는 운영 환경에서 절대 사용하면 안 됩니다.
이유는 '장애 추적'이 불가능하기 때문입니다.

1.  **기록 대상 (가장 치명적)**
    * `e.printStackTrace()`: System.err (표준 에러)로 출력합니다.
        -> 콘솔에만 찍힐 뿐, 설정된 로그 파일(/logs/error.log 등)에 기록이 남지 않습니다.
        -> 서버 장애 발생 시, 원인을 찾을 방법이 사라집니다.
    * `log.error()`: Logback 같은 로깅 프레임워크가 관리합니다.
        -> 설정된 파일, DB, 외부 모니터링 툴 등으로 로그를 '반드시' 전송합니다.

2.  **성능 및 제어**
    * `e.printStackTrace()`: 동기(Blocking) I/O입니다. 성능 저하를 유발하며 제어가 불가능합니다.
    * `log.error()`: 비동기 로깅을 지원하며, yml/properties를 통해 로그 레벨(ERROR, WARN, INFO)을 완벽하게 제어할 수 있습니다.

3.  **문맥 정보**
    * `e.printStackTrace()`: 스택 트레이스만 덩그러니 출력됩니다.
    * `log.error("500 Error", e)`: "500 Error" 같은 '문맥 메시지'와 함께 스택 트레이스를 기록할 수 있어, 로그 분석이 훨씬 용이합니다.

결론: 서버에 기록을 남기고 장애를 추적하려면 반드시 log.error()를 사용해야 합니다.
*/
        log.error("500 Error",e);
        return handleExceptionInternalFalse(e, GeneralErrorCode._INTERNAL_SERVER_ERROR.getHttpStatus(), request, e.getMessage());
    }

    // 비즈니스 로직 커스텀 예외처리
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity onThrowException(GeneralException generalException, HttpServletRequest request) {
        return handleExceptionInternal(generalException,
                generalException.getCode(),
                HttpHeaders.EMPTY,
                request);
    }

    /**
     *
     * @param e
     * @param reason : 구현체(GeneralErrorCode)로 쓰면 GeneralException.getCode()에서 타입 불일치 -> 객체 지향...
     * @param headers : 부모 클래스 메서드 시그니처 일관성 따르기 위함 -> 쓰이지 않음 -> HttpHeaders.EMPTY
     * @param request
     * @return
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception e, BaseErrorCode reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        ApiResponse<Object> body = ApiResponse.onFailure(reason, reason.getMessage());
//        e.printStackTrace();

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e,
                                                                HttpStatus status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(GeneralErrorCode._INTERNAL_SERVER_ERROR,errorPoint);
        return super.handleExceptionInternal(
                e,
                body,
                HttpHeaders.EMPTY,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, GeneralErrorCode errorCommonStatus,
                                                               WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus, errorArgs);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, GeneralErrorCode errorCommonStatus,
                                                                     HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus, null);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }
}