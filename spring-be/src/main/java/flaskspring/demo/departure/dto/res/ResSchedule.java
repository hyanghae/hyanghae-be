package flaskspring.demo.departure.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
public class ResSchedule {

    @Schema(description = "전체 최단 거리", example = "302.123123")
    private double fullDistance;

    @Schema(description = "여행지 스케쥴")
    private List<ResSchedulePlace> schedulePlaces;

}
