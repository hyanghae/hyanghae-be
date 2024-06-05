package flaskspring.demo.home.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResPopularSearch {

    @Schema(description = "인기 검색어 키워드" , example = "해운대")
    String keyword;
}
