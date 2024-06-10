package flaskspring.demo.recommend.dto.res;

import flaskspring.demo.home.dto.res.ResPlaceBrief;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResPlaceRecommendPaging {

    @Schema(description = "태그, 이미지 기반 추천 여행지 목록")
    List<ResPlaceBrief> recommendedPlaces;

    @Schema(description = "카운트 커서", example = "3")
    private Long nextCountCursor;

    @Schema(description = "이름 커서", example = "강문해변")
    private String nextNameCursor;

    @Schema(description = "ID 커서", example = "1234")
    private Long nextIdCursor;



}
