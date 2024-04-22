package flaskspring.demo.travel.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.dto.res.ResTag;
import flaskspring.demo.travel.domain.Place;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    private List<ResTag> tags;

    @Schema(description = "좋아요 수", example = "100")
    private Integer likesCount;

    @Schema(description = "등록 수", example = "50")
    private Integer registerCount;

    @Schema(description = "좋아요 여부", example = "false")
    private Boolean isLiked;

    @Schema(description = "등록 여부", example = "true")
    private Boolean isRegistered;

    @Schema(hidden = true)
    @JsonIgnore
    transient private Long sameTagCount;


    public ResPlace(Tuple tuple) {
        Place place = tuple.get(0, Place.class);
        String tagIdsString = tuple.get(1, String.class);
        String tagNamesString = tuple.get(2, String.class); // 두 번째 항목인 String을 가져옵니다.
        Long sameTagCount = tuple.get(3, Long.class); // 두 번째 항목인 String을 가져옵니다.
        Boolean isLiked = tuple.get(4, Boolean.class); // Boolean으로 가져옵니다.
        Boolean isRegistered = tuple.get(5, Boolean.class); // Boolean으로 가져옵니다.

        this.placeId = place.getId();
        this.placeName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.tags = createResTags(tagIdsString, tagNamesString);
        this.likesCount = place.getLikeCount();
        this.registerCount = place.getRegisterCount();
        this.isLiked = isLiked;
        this.isRegistered = isRegistered;
        this.sameTagCount = sameTagCount;
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

