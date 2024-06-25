package flaskspring.demo.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BaseObject<T> {

    @Schema(description = "결과 List")
    List<T> resultList;

    public BaseObject(List<T> resultList) {
        this.resultList = resultList;
    }
}
