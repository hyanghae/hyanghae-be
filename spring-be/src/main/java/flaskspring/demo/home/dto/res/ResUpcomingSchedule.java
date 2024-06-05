package flaskspring.demo.home.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResUpcomingSchedule {

    @Schema(description = "스케쥴명", example = "강원 강릉")
    private String scheduleName;

    @Schema(description = "디데이", example = "4")
    private int DDay;

    @Schema(description = "스케쥴 시작 일자", example = "2024.08.27")
    private LocalDateTime startDate;

    @Schema(description = "스케쥴 종료 일자", example = "2024.08.30")
    private LocalDateTime endDate;
}