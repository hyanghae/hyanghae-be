package flaskspring.demo.travel.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class NotFamousPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("MAPX")
    private String mapx;

    @JsonProperty("MAPY")
    private String mapy;

    @JsonProperty("SEARCH_COUNT")
    private int searchCount;

    @JsonProperty("CITY")
    private String city;

    @JsonProperty("MAJOR_CATEGORY")
    private String majorCategory;

    @JsonProperty("REGION")
    private String region;

    @JsonProperty("ROAD_ADDRESS")
    private String roadAddress;

    @JsonProperty("SUB_CATEGORY")
    private String subCategory;

    @JsonProperty("TOURIST_SPOT_NAME")
    private String touristSpotName;

    // 생성자, getter, setter 등 필요한 메서드 추가
}
