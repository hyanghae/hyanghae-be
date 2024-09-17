package flaskspring.demo.schedule.domain;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.dto.req.ReqDaySchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_schedule_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private LocalDate date;


    public static DaySchedule create(Schedule schedule, ReqDaySchedule reqDaySchedule) {
        DaySchedule daySchedule = DaySchedule.builder()
                .schedule(schedule)
                .date(reqDaySchedule.getDate())
                .build();

        return daySchedule;
    }
}
