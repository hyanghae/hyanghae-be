package flaskspring.demo.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagName tagName;


    public static Tag createTag(TagName tagName) {
        Tag tag = new Tag();
        tag.tagName = tagName;
        return tag;
    }
}