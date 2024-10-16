package flaskspring.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum BaseResponseCode {

    /**
     * 200
     */
    OK(HttpStatus.OK,CustomHttpStatus.OK, "요청 성공하였습니다."),
    CREATED(HttpStatus.OK,CustomHttpStatus.CREATED, "생성 성공하였습니다."),
    OK_LIKE(HttpStatus.OK,CustomHttpStatus.CREATED, "좋아요 성공하였습니다"),
    OK_UNLIKE(HttpStatus.OK,CustomHttpStatus.OK, "좋아요 취소하였습니다"),
    OK_REGISTER(HttpStatus.OK,CustomHttpStatus.CREATED, "여행지 등록 성공하였습니다"),
    OK_UNREGISTER(HttpStatus.OK,CustomHttpStatus.OK, "여행지 등록 취소하였습니다"),


    /**
     * 400
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    BAD_DUPLICATE(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "중복된 사용자가 있습니다"),
    AUTHORIZATION_NOT_VALID(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "인증정보가 일지하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다. 다시 입력해주세요."),
    NO_ID_EXCEPTION(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "해당 ID가 존재하지 않습니다."),
    NO_DEPARTURE_EXCEPTION(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "등록된 출발지가 없습니다."),
    NO_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "등록된 이미지가 없습니다."),
    INVALID_CITY_FILTER(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "유효하지 않은 지역 필터입니다"),
    INVALID_CURSOR(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "유효하지 않은 커서 값입니다"),
    MISSING_PREFERENCES_FOR_RECOMMENDATION(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "추천을 하기 위한 취향 설정이 없습니다"),
    PREFERENCE_NOT_APPLIED(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "아직 취향 설정이 반영되지 않았습니다"),
    INVALID_SCHEDULE_DAYCOUNT(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "일정 리스트 길이와 날짜 수는 같아야 합니다"),
    INVALID_DAYCOUNT(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "dayCount와 (endDate - startDate)가 다릅니다"),

    INVALID_SCHEDULE(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "해당하는 스케쥴이 없습니다"),

    REGISTRATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "여행지 등록개수 초과하였습니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST,CustomHttpStatus.BAD_REQUEST, "페이지 번호가 잘못되었습니다."),


    /**
     * 401
     */
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED,CustomHttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

    NO_TOKEN_FOUND(HttpStatus.UNAUTHORIZED,CustomHttpStatus.UNAUTHORIZED, "토큰이 없습니다."),

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,CustomHttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,CustomHttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),

    FORBIDDEN(HttpStatus.UNAUTHORIZED,CustomHttpStatus.FORBIDDEN, "권한이 없습니다"),


    /**
     * 500
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,CustomHttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다"),
    IMAGE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,CustomHttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 중 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,CustomHttpStatus.INTERNAL_SERVER_ERROR, "데이터 불러오는 도중 에러가 발생"),
    TAG_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,CustomHttpStatus.INTERNAL_SERVER_ERROR, "데이터 불러오는 도중 태그 매핑 에러가 발생"),
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,CustomHttpStatus.INTERNAL_SERVER_ERROR, "REDIS 에러 발생"),
    ANNOTATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,CustomHttpStatus.INTERNAL_SERVER_ERROR, "애노테이션 에러입니다."),

    /**
     *  600 Custom 상태코드.
     *  httpCode는 기존의 것으로 유지해달라는 요청
     */
    CANNOT_REISSUE_TOKEN(HttpStatus.BAD_REQUEST,CustomHttpStatus.REISSUE_ERROR, "토큰 재발급 실패하였습니다");

    private HttpStatusCode httpCode;
    private CustomHttpStatus code;
    private String message;
}
