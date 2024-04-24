package flaskspring.demo.place.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResTagSim {

    @Schema(description = "태그 ID", example = "4")
    private Long tagId;

    @Schema(description = "태그 이름", example = "등산")
    private String tagName;

    @Schema(description = "유사도 점수", example = "70")
    private int simScore;
}
