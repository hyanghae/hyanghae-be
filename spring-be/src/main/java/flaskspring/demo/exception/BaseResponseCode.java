package flaskspring.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BaseResponseCode {

    /**
     * 200
     */
    OK(CustomHttpStatus.OK, "요청 성공하였습니다."),
    CREATED(CustomHttpStatus.CREATED, "생성 성공하였습니다."),
    OK_LIKE(CustomHttpStatus.CREATED, "좋아요 성공하였습니다"),
    OK_UNLIKE(CustomHttpStatus.OK, "좋아요 취소하였습니다"),
    OK_REGISTER(CustomHttpStatus.CREATED, "여행지 등록 성공하였습니다"),
    OK_UNREGISTER(CustomHttpStatus.OK, "여행지 등록 취소하였습니다"),


    /**
     * 400
     */
    BAD_REQUEST(CustomHttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    BAD_DUPLICATE(CustomHttpStatus.BAD_REQUEST, "중복된 사용자가 있습니다"),
    AUTHORIZATION_NOT_VALID(CustomHttpStatus.BAD_REQUEST, "인증정보가 일지하지 않습니다"),
    INVALID_PASSWORD(CustomHttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다. 다시 입력해주세요."),
    NO_ID_EXCEPTION(CustomHttpStatus.BAD_REQUEST, "해당 ID가 존재하지 않습니다."),
    NO_DEPARTURE_EXCEPTION(CustomHttpStatus.BAD_REQUEST, "등록된 출발지가 없습니다."),
    NO_IMAGE_EXCEPTION(CustomHttpStatus.BAD_REQUEST, "등록된 이미지가 없습니다."),
    INVALID_CITY_FILTER(CustomHttpStatus.BAD_REQUEST, "유효하지 않은 지역 필터입니다"),
    INVALID_CURSOR(CustomHttpStatus.BAD_REQUEST, "유효하지 않은 커서 값입니다"),
    MISSING_PREFERENCES_FOR_RECOMMENDATION(CustomHttpStatus.BAD_REQUEST, "추천을 하기 위한 취향 설정이 없습니다"),
    PREFERENCE_NOT_APPLIED(CustomHttpStatus.BAD_REQUEST, "아직 취향 설정이 반영되지 않았습니다"),

    REGISTRATION_LIMIT_EXCEEDED(CustomHttpStatus.BAD_REQUEST, "여행지 등록개수 초과하였습니다."),
    INVALID_PAGE_NUMBER(CustomHttpStatus.BAD_REQUEST, "페이지 번호가 잘못되었습니다."),


    /**
     * 401
     */
    NOT_VALID_TOKEN(CustomHttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

    NO_TOKEN_FOUND(CustomHttpStatus.UNAUTHORIZED, "토큰이 없습니다."),

    TOKEN_EXPIRED(CustomHttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    UNAUTHORIZED(CustomHttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),

    FORBIDDEN(CustomHttpStatus.FORBIDDEN, "권한이 없습니다"),


    /**
     * 500
     */
    INTERNAL_SERVER_ERROR(CustomHttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다"),
    IMAGE_PROCESSING_ERROR(CustomHttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 중 오류가 발생했습니다."),
    DATABASE_ERROR(CustomHttpStatus.INTERNAL_SERVER_ERROR, "데이터 불러오는 도중 에러가 발생"),
    REDIS_ERROR(CustomHttpStatus.INTERNAL_SERVER_ERROR, "REDIS 에러 발생"),
    ANNOTATION_ERROR(CustomHttpStatus.INTERNAL_SERVER_ERROR, "애노테이션 에러입니다."),

    /**
     *  1000
     */
    CANNOT_REISSUE_TOKEN(CustomHttpStatus.REISSUE_ERROR, "토큰 재발급 실패하였습니다");

    private CustomHttpStatus code;
    private String message;
}
