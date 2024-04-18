package flaskspring.demo.recommend.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureScoreDto {

    Integer featA;
    Integer featB;
    Integer featC;
}

