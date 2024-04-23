package flaskspring.demo.myPage.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ResMyPageMain {

    @Schema(description = "좋아요한 여행지 목록")
    private List<ResLikedPlace> likedPlaces;

    @Schema(description = "업로드된 이미지 목록")
    private List<ResUploadedImage> uploadedImgs;
}
