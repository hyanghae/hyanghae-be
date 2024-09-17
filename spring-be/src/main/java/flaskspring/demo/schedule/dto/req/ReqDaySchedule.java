package flaskspring.demo.schedule.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ReqDaySchedule {

    @Schema(description = "n번째 날", example = "1")
    int dayNumber;

    @Schema(description = "일자", example = "2024-09-16")
    LocalDate date;

    @Schema(description = "여행지 ID 리스트" , example = "[1, 2, 3]")
    List<Long> placeIds;
}
