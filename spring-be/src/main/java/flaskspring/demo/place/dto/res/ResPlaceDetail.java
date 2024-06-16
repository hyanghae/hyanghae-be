package flaskspring.demo.place.dto.res;

import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResPlaceDetail {

    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;

    @Schema(description = "지역 포함 여행지명", example = "하동 노량대교")
    private String touristSpotNameWithRegion;

    @Schema(description = "이미지 url 리스트")
    private List<String> placeImgUrlList;

    @Schema(description = "저장 여부", example = "false")
    private Boolean isSaved;

    @Schema(description = "출발지 x좌표", example = "127.619842753323")
    double mapX;

    @Schema(description = "출발지 y좌표", example = "37.2125169562113")
    double mapY;

    @Schema(description = "출발지 도로명 주소", example = "00시 00면 00도로")
    String roadAddress;

    @Schema(description = "주변 여행지")
    private List<ResPlaceBrief> nearbyPlaces;


    public ResPlaceDetail(Place place) {
        this.placeId = place.getId();
        this.touristSpotNameWithRegion = place.getTouristSpotName();
        this.placeImgUrlList = List.of(place.getImagePath());
        this.isSaved = null;
        this.mapX = place.getLocation().getMapX();
        this.mapY = place.getLocation().getMapY();
        this.roadAddress = place.getRoadAddress();
        this.nearbyPlaces = null;
    }
}