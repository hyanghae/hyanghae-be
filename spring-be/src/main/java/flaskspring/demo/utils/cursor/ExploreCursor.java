package flaskspring.demo.utils.cursor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExploreCursor {
    private Long countCursor;
    private String nameCursor;
    private Long idCursor;

    // 생성자, Getter, Setter 생략
}