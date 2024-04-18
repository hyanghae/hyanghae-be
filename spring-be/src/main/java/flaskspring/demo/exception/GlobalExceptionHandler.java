package flaskspring.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    protected ResponseEntity<BaseExceptionResponse> handleBaseException(BaseException e) {
        return ResponseEntity.status(e.baseResponseCode.getCode())
                .body(new BaseExceptionResponse(e.baseResponseCode.getCode(), e.baseResponseCode.getMessage()));
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<BaseExceptionResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        // HttpClientErrorException으로부터 상태 코드와 에러 응답을 가져옵니다.
     /*   HttpStatus statusCode = (HttpStatus) e.getStatusCode();
        String responseBody = e.getResponseBodyAsString();

        // 에러 응답을 BaseExceptionResponse에 매핑합니다.
        ObjectMapper mapper = new ObjectMapper();
        BaseExceptionResponse response;
        try {
            response = mapper.readValue(responseBody, BaseExceptionResponse.class);
        } catch (JsonProcessingException ex) {
            // JSON 파싱 오류가 발생한 경우 기본 에러 응답을 생성합니다.
            response = new BaseExceptionResponse(statusCode, "Unknown Error");
        }*/
        //카카오 코드 에러가 났을 경우!
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseExceptionResponse(HttpStatus.BAD_REQUEST, "유효하지 않은 코드입니다."));
    }

}