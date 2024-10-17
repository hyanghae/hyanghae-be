package flaskspring.demo.schedule.domain;

import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.dto.req.ReqDaySchedule;
import flaskspring.demo.schedule.dto.res.ResDepartureDto;
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

    @Embedded
    private Departure departure;

    private LocalDate date;


    public static DaySchedule create(Schedule schedule, ReqDaySchedule reqDaySchedule) {
        Departure departure = (reqDaySchedule.getReqDeparture() != null)
                ? Departure.create(reqDaySchedule.getReqDeparture())
                : null;

        DaySchedule daySchedule = DaySchedule.builder()
                .departure(departure)
                .schedule(schedule)
                .date(reqDaySchedule.getDate())
                .build();

        return daySchedule;
    }



}
