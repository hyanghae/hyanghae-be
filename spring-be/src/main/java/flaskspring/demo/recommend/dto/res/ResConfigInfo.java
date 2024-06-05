package flaskspring.demo.recommend.dto.res;

import flaskspring.demo.tag.dto.res.ResTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ResConfigInfo {

    @Schema(description = "설정된 이미지 url" , example = "www.example.com")
    String configuredImageUrl;

    @Schema(description = "설정된 태그")
    List<ResTag> configuredTags;
}
