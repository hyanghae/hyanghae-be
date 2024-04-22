package flaskspring.demo.departure.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.travel.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class ResSchedulePlace {

    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;

    @Schema(description = "여행지 이름", example = "수출의다리")
    private String placeName;

    @Schema(description = "여행지 이미지 URL", example = "https://example.com/image.jpg")
    private String placeImgUrl;

    @Schema(description = "좌표")
    ResLocation location;

    @Schema(description = "여행지 태그 목록", example = "[\"액티비티\", \"오션뷰\", \"산뷰\"]")
    private List<String> tags;

    @Schema(description = "좋아요 수", example = "100")
    private Integer likesCount;

    @Schema(description = "등록 수", example = "50")
    private Integer registerCount;

    @Schema(description = "좋아요 여부", example = "false")
    private Boolean isLiked;

    @Schema(description = "출발지로부터 거리", example = "113.12312")
    private double distanceFromDeparture;

    @Schema(description = "이전 여행지로부터 거리", example = "12.3232")
    private double distanceFromPrevious;

    private LocalDateTime registeredTime;

    public ResSchedulePlace(Tuple tuple) {
        Place place = tuple.get(0, Place.class); // 여행지 정보
        String tagNames = tuple.get(1, String.class); // 태그 이름들
        Boolean isLiked = tuple.get(2, Boolean.class); // 좋아요 여부
        Boolean isRegistered = tuple.get(3, Boolean.class); // 등록 여부
        LocalDateTime registeredTime = tuple.get(4, LocalDateTime.class); // 등록 시간


        this.placeId = place.getId();
        this.placeName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.location = new ResLocation(place.getLocation());
        this.tags = Collections.singletonList(tagNames);
        this.likesCount = place.getLikeCount();
        this.registerCount = place.getRegisterCount();
        this.isLiked = isLiked;
        this.registeredTime = registeredTime;
    }

    // 위도와 경도를 사용하여 두 지점 간의 거리를 계산하는 메서드
  /*  public void setDistanceFromPrevious(String otherMapX, String otherMapY) {

        double startLat = Double.parseDouble(this.mapY);
        double startLon = Double.parseDouble(this.mapX);
        double endLat = Double.parseDouble(otherMapY);
        double endLon = Double.parseDouble(otherMapX);

        double dLat = Math.toRadians(endLat - startLat);
        double dLon = Math.toRadians(endLon - startLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        this.distanceFromPrevious =  AVERAGE_RADIUS_OF_EARTH_KM * c;
    }
*/
    private static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

}
