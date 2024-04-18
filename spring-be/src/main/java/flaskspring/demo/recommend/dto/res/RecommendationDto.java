package flaskspring.demo.recommend.dto.res;

import lombok.Data;

@Data
public class RecommendationDto {
    private Long firstPlaceId;
    private Long secondPlaceId;
    private Long thirdPlaceId;
}