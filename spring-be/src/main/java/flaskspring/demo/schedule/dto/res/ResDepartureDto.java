package flaskspring.demo.schedule.dto.res;

import flaskspring.demo.place.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDepartureDto {

    @Schema(description = "출발지 지명", example = "홍대입구역")
    private String placeName;

    @Schema(description = "출발지 도로명주소", example = "00면 00도로")
    private String roadAddress;

    @Schema(description = "출발지 x좌표", example = "127.123123")
    private double mapX;

    @Schema(description = "출발지 y좌표", example = "37.2125169562113")
    private double mapY;
}
