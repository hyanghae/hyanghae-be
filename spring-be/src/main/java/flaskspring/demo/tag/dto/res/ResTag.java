package flaskspring.demo.tag.dto.res;

import flaskspring.demo.tag.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResTag {

    @Schema(description = "태그 ID", example = "4")
    private Long tagId;

    @Schema(description = "태그 이름", example = "등산")
    private String tagName;

    public ResTag(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
