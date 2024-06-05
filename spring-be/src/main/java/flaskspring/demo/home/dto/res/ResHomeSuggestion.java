package flaskspring.demo.home.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ResHomeSuggestion {
    @Schema(description = "스케쥴")
    ResUpcomingSchedule upcomingSchedule;

    @Schema(description = "여행지 제안")
    List<ResPlaceBrief> placeSuggestions;

    @Schema(description = "유명 여행지")
    List<ResFamous> famousPlaces;
}
