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

    @Schema(description = "카운트 커서", example = "3")
    private Long nextCountCursor;

    @Schema(description = "ID 커서", example = "1234")
    private Long nextIdCursor;


}
