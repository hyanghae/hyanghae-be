package flaskspring.demo.place.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor

public class ResSimilarity {

    @Schema(description = "총 유사도 점수", example = "80")
    Integer totalSimScore;

    @Schema(description = "태그별 유사도 점수")
    List<ResTagSim> tagSimScores;

}
