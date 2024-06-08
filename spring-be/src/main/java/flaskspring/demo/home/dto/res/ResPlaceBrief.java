package flaskspring.demo.home.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.tag.dto.res.ResTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResPlaceBrief {


    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;

    @Schema(description = "지역", example = "강원 강릉")
    private String region;

    @Schema(description = "여행지명", example = "강문해변")
    private String touristSpotName;

    @Schema(description = "여행지 이미지 URL", example = "https://example.com/image.jpg")
    private String placeImgUrl;

    @Schema(description = "태그들")
    private List<ResTag> tags;

    @Schema(description = "저장 여부", example = "false")
    private Boolean isSaved;

    public ResPlaceBrief(Tuple tuple) {
        Place place = tuple.get(0, Place.class);
        String tagIdsString = tuple.get(1, String.class);
        String tagNamesString = tuple.get(2, String.class); // 두 번째 항목인 String을 가져옵니다.
        Boolean isRegistered = tuple.get(3, Boolean.class); // Boolean으로 가져옵니다.

        if (place == null || tagIdsString == null || tagNamesString == null ) {
            throw new BaseException(BaseResponseCode.DATABASE_ERROR);
        }

        this.placeId = place.getId();
        this.region = place.getCity() + " " + place.getRegion();
        this.touristSpotName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();

        this.tags = createResTags(tagIdsString, tagNamesString);
        this.isSaved = isRegistered;
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
