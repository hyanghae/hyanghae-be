package flaskspring.demo.search.dto.res;

import flaskspring.demo.home.dto.res.ResPlaceBrief;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResPlaceSearchPaging {



    List<ResPlaceBrief> searchedPlaces;

    @Schema(description = "카운트 커서", example = "3")
    private Long nextCountCursor;

    @Schema(description = "ID 커서", example = "1234")
    private Long nextIdCursor;

    @Schema(description = "이름 커서", example = "강문해변")
    private String nextNameCursor;

    @Schema(description = "유명 여행지 기준 검색 여부", example = "false")
    private boolean isFamousSearch;

    @Schema(description = "검색된 유명 여행지 Id", example = "false")
    private Long famousPlaceId;
}
