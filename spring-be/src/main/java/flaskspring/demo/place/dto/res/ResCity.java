package flaskspring.demo.place.dto.res;

import flaskspring.demo.place.domain.CityCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResCity {
    @Schema(description = "도시이름", example = "서울특별시")
    private String koreanName;

    @Schema(description = "파라미터 이름", example = "SEOUL")
    private String paramName;

    @Schema(description = "도시 줄인 이름", example = "서울")
    private String shortenedName;

    public ResCity(CityCode cityCode) {
        this.koreanName = cityCode.getKoreanName();
        this.paramName = cityCode.getParamName();
        this.shortenedName = cityCode.getShortenedName();
    }
}
