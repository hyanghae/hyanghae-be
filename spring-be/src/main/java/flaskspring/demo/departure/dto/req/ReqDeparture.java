package flaskspring.demo.departure.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReqDeparture {

    @Schema(description = "출발지 x좌표", example = "127.12321")
    String mapX;
    @Schema(description = "출발지 y좌표", example = "127.12321")
    String mapY;
    @Schema(description = "출발지 도로명 주소", example = "00시 00면 00도로")
    String roadAddress;
}
