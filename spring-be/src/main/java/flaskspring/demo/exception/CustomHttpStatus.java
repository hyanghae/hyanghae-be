package flaskspring.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomHttpStatus {

    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    REISSUE_ERROR(600, "Reissue Error");

    private final int statusCode;
    private final String reasonPhrase;
}
