package flaskspring.demo.tag.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "태그 인덱스 요청")
@Data
public class ReqTagIndexes {

    @Schema(description = "사진 교체 여부", example = "true")
    Boolean isImgChanged;

    @Schema(description = "태그 인덱스 목록", example = "[1, 2, 3]")
    private List<Long> tagIndexes;
}