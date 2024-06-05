package flaskspring.demo.home.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResRisingPlacePaging {

    @Schema(description = "뜨고 있는 여행지 목록")
    List<ResPlaceBrief> risingPlaces;

    @Schema(description = "전체 페이지 수", example = "5")
    private int numOfTotalPages;

    @Schema(description = "모든 요소 개수", example = "50")
    private long numOfTotalElements;

    @Schema(description = "현재 페이지의 요소 개수", example = "10")
    private int numOfElements;
}
