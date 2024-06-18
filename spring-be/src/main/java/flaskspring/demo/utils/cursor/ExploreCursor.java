package flaskspring.demo.utils.cursor;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import lombok.Getter;

@Getter
public class ExploreCursor {
    private Long countCursor;
    private String nameCursor;
    private Long idCursor;

    public ExploreCursor(String countCursor, String nameCursor, String idCursor) {
        this.countCursor = parseLongOrNull(countCursor);
        this.nameCursor = parseStringOrNull(nameCursor);
        this.idCursor = parseLongOrNull(idCursor);
    }

    private Long parseLongOrNull(String value) {
        if ("null".equals(value)) {
            return null;
        }
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            throw new BaseException(BaseResponseCode.INVALID_CURSOR);
        }
    }

    private String parseStringOrNull(String value) {
        if ("null".equals(value)) {
            return null;
        }
        return value;
    }

    // Getter, Setter 등 필요한 메서드 생략
}
