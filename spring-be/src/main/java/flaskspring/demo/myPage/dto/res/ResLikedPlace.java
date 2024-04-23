package flaskspring.demo.myPage.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.tag.dto.res.ResTag;
import flaskspring.demo.travel.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResLikedPlace {

    @Schema(description = "여행지 ID")
    private long placeId;

    @Schema(description = "여행지 이름")
    private String placeName;

    @Schema(description = "여행지 이미지 URL")
    private String placeImgUrl;

    @Schema(description = "태그 목록")
    private List<ResTag> tags;

    @Schema(description = "등록 여부")
    private boolean isRegistered;

    @Schema(description = "좋아요 여부")
    private boolean isLiked;

    public ResLikedPlace(Tuple tuple) {

        Place place = tuple.get(0, Place.class); // 여행지 정보
        String tagIdsString = tuple.get(1, String.class); // 태그 이름들
        String tagNamesString = tuple.get(2, String.class); // 태그 이름들
        Boolean isLiked = tuple.get(3, Boolean.class); // 좋아요 여부
        Boolean isRegistered = tuple.get(4, Boolean.class); // 등록 여부
        //LocalDateTime registeredTime = tuple.get(5, LocalDateTime.class); // 좋아요한 시간

        this.placeId = place.getId();
        this.placeName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.tags = createResTags(tagIdsString, tagNamesString);
        this.isRegistered = isRegistered;
        this.isLiked = isLiked;
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