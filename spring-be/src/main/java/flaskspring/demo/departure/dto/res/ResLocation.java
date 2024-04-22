package flaskspring.demo.departure.dto.res;

import flaskspring.demo.travel.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResLocation {
    @Schema(description = "출발지 x좌표", example = "127.12321")
    private String mapX;

    @Schema(description = "출발지 y좌표", example = "127.12321")
    private String mapY;

    public ResLocation(Location location) {
        this.mapX = location.getMapX();
        this.mapY = location.getMapY();
    }
}