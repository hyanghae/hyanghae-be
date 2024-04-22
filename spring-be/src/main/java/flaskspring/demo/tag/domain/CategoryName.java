package flaskspring.demo.tag.domain;

import lombok.Getter;

@Getter
public enum CategoryName {
    체험("체험"),
    휴식("휴식"),
    전시("전시"),
    역사("역사"),
    음식("음식"),
    경치("경치");

    private final String value;

    CategoryName(String value) {
        this.value = value;
    }
}
