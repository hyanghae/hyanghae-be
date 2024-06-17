package flaskspring.demo.tag.dto.res;

import flaskspring.demo.tag.domain.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResRegisteredTag {
    private Long tagId;
    private String tagName;

    public ResRegisteredTag(Tag tag) {
        this.tagId = tag.getId();
        this.tagName = tag.getTagName().getValue();
    }
}