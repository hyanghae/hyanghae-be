package flaskspring.demo.place.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReqPlaceRegister {

    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;
}