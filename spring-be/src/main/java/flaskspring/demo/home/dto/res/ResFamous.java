package flaskspring.demo.home.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResFamous {

    @Schema(description = "지역", example = "부산 해운대구")
    String region;

    @Schema(description = "여행지명", example = "해운대")
    String touristSpotName;
}