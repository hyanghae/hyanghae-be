package flaskspring.demo.departure.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.tag.dto.res.ResTag;
import flaskspring.demo.place.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<ResTag> tags;

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
        String tagIdsString = tuple.get(1, String.class); // 태그 이름들
        String tagNamesString = tuple.get(2, String.class); // 태그 이름들
        Boolean isLiked = tuple.get(3, Boolean.class); // 좋아요 여부
        Boolean isRegistered = tuple.get(4, Boolean.class); // 등록 여부
        LocalDateTime registeredTime = tuple.get(5, LocalDateTime.class); // 등록 시간


        this.placeId = place.getId();
        this.placeName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.location = new ResLocation(place.getLocation());
        this.tags = createResTags(tagIdsString, tagNamesString);
        this.likesCount = place.getLikeCount();
        this.registerCount = place.getRegisterCount();
        this.isLiked = isLiked;
        this.registeredTime = registeredTime;
    }

    private List<ResTag> createResTags(String tagIdsString, String tagNamesString) {
        List<ResTag> resTags = new ArrayList<>();
        String[] tagIds = tagIdsString.split(",");
        String[] tagNames = tagNamesString.split(",");

        for (int i = 0; i < tagIds.length; i++) {
            Long tagId = Long.parseLong(tagIds[i]);
            String tagName = tagNames[i];
            resTags.add(new ResTag(tagId, tagName));
        }

        return resTags;
    }

}
