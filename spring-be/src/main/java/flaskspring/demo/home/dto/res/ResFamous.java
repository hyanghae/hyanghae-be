package flaskspring.demo.home.dto.res;

import flaskspring.demo.place.domain.FamousPlace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResFamous implements Serializable {

    @Schema(description = "지역", example = "부산 해운대구")
    Long famousPlaceId;

    @Schema(description = "지역", example = "부산 해운대구")
    String region;

    @Schema(description = "파라미터 이름", example = "SEOUL")
    private String cityParamName;

    @Schema(description = "여행지명", example = "해운대")
    String touristSpotName;

    public ResFamous(FamousPlace famousPlace) {
        this.famousPlaceId = famousPlace.getId();
        this.region = famousPlace.getCity() + " " + famousPlace.getRegion();
        this.cityParamName = famousPlace.getEnCityName();
        this.touristSpotName = famousPlace.getTouristSpotName();
    }
}
