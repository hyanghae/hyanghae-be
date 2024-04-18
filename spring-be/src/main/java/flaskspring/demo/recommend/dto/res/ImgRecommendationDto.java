package flaskspring.demo.recommend.dto.res;

import lombok.Data;

@Data
public class ImgRecommendationDto {
    private String name;
    private double similarity;
}