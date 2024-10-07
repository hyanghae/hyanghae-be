package flaskspring.demo.place.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ReqPlaceGet {

    @Schema(description = "여행지 인덱스 목록", example = "[1, 2, 3]")
    private List<Long> placeIds;
}
