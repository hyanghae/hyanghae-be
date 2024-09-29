package flaskspring.demo.schedule.dto.res;

import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.schedule.domain.DaySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResDaySchedule {


    @Schema(description = "출발지 도로명 주소", example = "00시 00면 00도로")
    private ResDepartureDto departure;

    @Schema(description = "여행 일자", example = "2024-09-29")
    private LocalDate date;

    @Schema(description = "일정 여행지 목록", example = "")
    List<ResPlaceBrief> places;

    public ResDaySchedule(DaySchedule daySchedule, List<ResPlaceBrief> placeBriefs){
        this.departure = daySchedule.getDeparture().toDto();
        this.date = daySchedule.getDate();
        places = placeBriefs;
    }

}
