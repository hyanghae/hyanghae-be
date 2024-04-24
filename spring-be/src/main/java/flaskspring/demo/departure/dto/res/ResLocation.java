package flaskspring.demo.departure.dto.res;

import flaskspring.demo.place.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResLocation {
    @Schema(description = "출발지 x좌표", example = "127.12321")
    private double mapX;

    @Schema(description = "출발지 y좌표", example = "37.12321")
    private double mapY;

    public ResLocation(Location location) {
        this.mapX = location.getMapX();
        this.mapY = location.getMapY();
    }
}