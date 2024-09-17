package flaskspring.demo.schedule.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ReqSchedule {

    @Schema(description = "스케줄 명", example = "강원도 여행")
    private String title;

    @Schema(description = "시작 일자", example = "2024-09-16")
    private LocalDate startDate;

    @Schema(description = "종료 일자", example = "2024-09-18")
    private LocalDate endDate;

    @Schema(description = "여행일 수", example = "3")
    private int dayCount;

    @Schema(description = "일정 리스트")
    List<ReqDaySchedule> daySchedules;
}
