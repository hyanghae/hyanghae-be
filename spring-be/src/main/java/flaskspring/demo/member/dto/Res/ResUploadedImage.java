package flaskspring.demo.member.dto.Res;

import flaskspring.demo.image.domain.UploadImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResUploadedImage {

    @Schema(description = "이미지 URL", example = "1")
    private Long imgId;

    @Schema(description = "이미지 URL")
    private String imgUrl;

    @Schema(description = "업로드 날짜")
    private LocalDateTime uploadDate;

    public ResUploadedImage(UploadImage uploadImage) {
        this.imgId = uploadImage.getId();
        this.imgUrl = uploadImage.getSavedImageUrl();
        this.uploadDate = uploadImage.getCreatedTime();
    }
}
