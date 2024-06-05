package flaskspring.demo.search.dto.res;

import flaskspring.demo.home.dto.res.ResPlaceBrief;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ResPlaceSearchPaging {

    @Schema(description = "검색된 여행지 목록")
    List<ResPlaceBrief> searchedPlaces;

    @Schema(description = "전체 페이지 수", example = "5")
    private int numOfTotalPages;

    @Schema(description = "모든 요소 개수", example = "50")
    private long numOfTotalElements;

    @Schema(description = "현재 페이지의 요소 개수", example = "10")
    private int numOfElements;
}
