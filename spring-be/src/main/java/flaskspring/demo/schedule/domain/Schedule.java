package flaskspring.demo.schedule.domain;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.dto.req.ReqDaySchedule;
import flaskspring.demo.schedule.dto.req.ReqSchedule;
import flaskspring.demo.schedule.dto.res.ResSchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private String title;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    private int dayCount;

    private LocalDate startDate;
    private LocalDate endDate;

    public static Schedule create(Member member, ReqSchedule reqSchedule){
        Schedule schedule = Schedule.builder()
                .member(member)
                .title(reqSchedule.getTitle())
                .startDate(reqSchedule.getStartDate())
                .endDate(reqSchedule.getEndDate())
                .dayCount(reqSchedule.getDayCount())
                .build();

        return schedule;
    }

    public ResSchedule toDto(){
        return new ResSchedule(this.id, this.title, this.startDate, this.endDate, this.dayCount);
    }

}
