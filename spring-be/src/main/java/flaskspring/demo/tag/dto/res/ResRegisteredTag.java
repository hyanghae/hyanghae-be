package flaskspring.demo.tag.dto.res;

import flaskspring.demo.tag.domain.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResRegisteredTag implements Serializable {
    private Long tagId;
    private String tagName;

    public ResRegisteredTag(Tag tag) {
        this.tagId = tag.getId();
        this.tagName = tag.getTagName().getValue();
    }
}