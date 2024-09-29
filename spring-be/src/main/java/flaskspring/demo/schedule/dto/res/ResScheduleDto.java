package flaskspring.demo.schedule.dto.res;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.domain.Schedule;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResScheduleDto {

    private String title;

    private String scheduleImgUrl;

    private int dayCount;

    private LocalDate startDate;
    private LocalDate endDate;

    List<ResDaySchedule> daySchedules;

    public ResScheduleDto(Schedule schedule, List<ResDaySchedule> daySchedules) {
        this.title = schedule.getTitle();
        this.scheduleImgUrl = schedule.getScheduleImgUrl();
        this.dayCount = schedule.getDayCount();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.daySchedules = daySchedules;
    }
}
