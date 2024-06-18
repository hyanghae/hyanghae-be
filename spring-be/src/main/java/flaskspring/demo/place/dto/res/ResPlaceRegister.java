package flaskspring.demo.place.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResPlaceRegister {

    @Schema(description = "등록 여부", example = "true")
    private Boolean isSaved;
}
