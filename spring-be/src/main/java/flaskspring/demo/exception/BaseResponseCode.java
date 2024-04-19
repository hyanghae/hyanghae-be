package flaskspring.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BaseResponseCode {

    /**
     * 200
     */
    OK(HttpStatus.OK, "요청 성공하였습니다."),


    /**
     * 400
     */
    BAD_DUPLICATE(HttpStatus.BAD_REQUEST, "중복된 사용자가 있습니다"),
    AUTHORIZATION_NOT_VALID(HttpStatus.BAD_REQUEST, "인증정보가 일지하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다. 다시 입력해주세요."),
    NO_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 ID가 존재하지 않습니다."),

    /**
     * 401
     */
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NO_TOKEN_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다");





    private HttpStatus code;
    private String message;
}
