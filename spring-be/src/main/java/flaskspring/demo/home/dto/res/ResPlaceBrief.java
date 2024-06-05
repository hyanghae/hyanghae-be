package flaskspring.demo.home.dto.res;

import flaskspring.demo.tag.dto.res.ResTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    private  List<ResTag> tags;

    @Schema(description = "저장 여부", example = "false")
    private Boolean isSaved;
}
