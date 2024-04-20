package flaskspring.demo.travel.dto.res;

import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.travel.domain.Place;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
public class ResPlace {

    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;

    @Schema(description = "여행지 이름", example = "수출의다리")
    private String placeName;

    @Schema(description = "여행지 이미지 URL", example = "https://example.com/image.jpg")
    private String placeImgUrl;

    @Schema(description = "여행지 태그 목록", example = "[\"액티비티\", \"오션뷰\", \"산뷰\"]")
    private List<String> tags;

    @Schema(description = "좋아요 수", example = "100")
    private Integer likesCount;

    @Schema(description = "등록 수", example = "50")
    private Integer registerCount;

    @Schema(description = "좋아요 여부", example = "false")
    private Boolean isLiked;

    @Schema(description = "등록 여부", example = "true")
    private Boolean isRegistered;


    @Schema(hidden = true)
    transient private Long sameTagCount;

    // 생성자, getter, setter 생략

    public ResPlace(Place place, List<String> placeTags, Long sameTagCount, boolean isLiked, boolean isRegistered) {
        this.placeId = place.getId();
        this.placeName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.tags = placeTags;
        this.likesCount = place.getLikeCount();
        this.registerCount = place.getRegisterCount();
        this.isLiked = isLiked;
        this.isRegistered = isRegistered;
        this.sameTagCount = sameTagCount;
    }
}

