package flaskspring.demo.schedule.dto.req;

import flaskspring.demo.departure.dto.res.ResLocation;
import flaskspring.demo.place.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqDeparture {

    @Schema(description = "출발지 지명", example = "홍대입구역")
    String placeName;

    @Schema(description = "출발지 도로명 주소", example = "00시 00면 00도로")
    String roadAddress;

    @Schema(description = "x좌표", example = "127.000000")
    double mapX;

    @Schema(description = "y좌표", example = "37.000000")
    double mapY;



}
