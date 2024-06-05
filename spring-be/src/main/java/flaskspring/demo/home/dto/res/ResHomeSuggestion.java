package flaskspring.demo.home.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ResHomeSuggestion {

    @Schema(description = "여행지 제안")
    List<ResPlaceBrief> placeSuggestions;

}
