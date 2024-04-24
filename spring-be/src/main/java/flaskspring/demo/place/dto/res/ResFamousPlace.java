package flaskspring.demo.place.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import flaskspring.demo.place.domain.FamousPlace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResFamousPlace {

    @Schema(description = "유사 인기여행지 ID", example = "17")
    private Long famousPlaceId;

    @Schema(description = "유사 인기여행지 광역", example = "경상북도")
    private String city;

    @Schema(description = "유사 인기여행지 도시", example = "경주시")
    private String region;

    @Schema(description = "유사 인기여행지명", example = "불국사")
    private String touristSpotName;


    @Schema(hidden = true)
    @JsonIgnore
    private String majorCategory;

    @Schema(hidden = true)
    @JsonIgnore
    private String roadAddress;

    @Schema(hidden = true)
    @JsonIgnore
    private String subCategory;

    @Schema(hidden = true)
    @JsonIgnore
    private int hashTagCount;

    @Schema(hidden = true)
    @JsonIgnore
    private int searchCount;

    public ResFamousPlace(FamousPlace famousPlace) {
        this.famousPlaceId = famousPlace.getId();
        this.city = famousPlace.getCity();
        this.majorCategory = famousPlace.getMajorCategory();
        this.region = famousPlace.getRegion();
        this.roadAddress = famousPlace.getRoadAddress();
        this.subCategory = famousPlace.getSubCategory();
        this.touristSpotName = famousPlace.getTouristSpotName();
        this.hashTagCount = famousPlace.getHashTagCount();
        this.searchCount = famousPlace.getSearchCount();
    }
}
