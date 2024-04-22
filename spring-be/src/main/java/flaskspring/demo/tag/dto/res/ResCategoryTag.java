package flaskspring.demo.tag.dto.res;

import flaskspring.demo.tag.domain.Category;
import flaskspring.demo.tag.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResCategoryTag {

    @Schema(description = "카테고리 ID", example = "1")
    Long categoryId;

    @Schema(description = "카테고리 이름", example = "체험")
    String categoryName;

    @Schema(description = "태그들")
    List<ResTag> tags;

    public ResCategoryTag(Category category, List<Tag> allTags) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName().getValue();
        this.tags = allTags.stream()
                .map(tag -> new ResTag(tag.getId(), tag.getTagName().getValue()))
                .collect(Collectors.toList());
    }
}
