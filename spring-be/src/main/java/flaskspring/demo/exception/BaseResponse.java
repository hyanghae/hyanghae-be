package flaskspring.demo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    @Schema(description = "http 상태코드", example = "200")
    private int code;  // 상태 코드 메세지

    @Schema(description = "전달 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message; // 에러 설명

    @Schema(description = "전달 데이터")
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public BaseResponse(BaseResponseCode responseCode, T data) {
        this.code = responseCode.getCode().getStatusCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }
}
/**
 * 없을 경우 빈 객체를 전달
 *
 * @JsonInclude(JsonInclude.Include.NON_NULL) 어노테이션은 Jackson 라이브러리에서 사용되며,
 * JSON 직렬화 시에 data 필드가 null인 경우 JSON에서 생략되도록 설정합니다.
 * 이렇게 함으로써, 클라이언트에게 null 값이 포함된 데이터를 전달하지 않아도 됩니다.
 * <p>
 * 성공할 경우 컨틀롤러에 리턴 타입<제너릭 타입>이 명시되어 있으므로 BaseResponse로 해도 나온다.
 * 그러나 예외상황의 경우 T에 무엇이 들어갈지 모르기 때문에... -> 리턴 타입을 BASE Exception으로 바꿔보지ㅏ
 */
/**
 * @JsonInclude(JsonInclude.Include.NON_NULL) 어노테이션은 Jackson 라이브러리에서 사용되며,
 * JSON 직렬화 시에 data 필드가 null인 경우 JSON에서 생략되도록 설정합니다.
 * 이렇게 함으로써, 클라이언트에게 null 값이 포함된 데이터를 전달하지 않아도 됩니다.
 */


/**
 *  성공할 경우 컨틀롤러에 리턴 타입<제너릭 타입>이 명시되어 있으므로 BaseResponse로 해도 나온다.
 *  그러나 예외상황의 경우 T에 무엇이 들어갈지 모르기 때문에... -> 리턴 타입을 BASE Exception으로 바꿔보지ㅏ
 */