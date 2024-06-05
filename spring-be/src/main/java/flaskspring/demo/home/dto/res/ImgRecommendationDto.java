package flaskspring.demo.home.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgRecommendationDto {
    private String name;
    private double similarity;

}