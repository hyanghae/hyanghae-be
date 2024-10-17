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
import java.time.temporal.ChronoUnit;

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

    private String scheduleImgUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    private int dayCount;

    private LocalDate startDate;
    private LocalDate endDate;

    private long dDay;


    public static Schedule create(Member member, ReqSchedule reqSchedule, String scheduleImgUrl) {
        // 현재 날짜와 startDate 간의 차이를 계산
        LocalDate startDate = reqSchedule.getStartDate();
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), startDate);

        Schedule schedule = Schedule.builder()
                .member(member)
                .scheduleImgUrl(scheduleImgUrl)
                .title(reqSchedule.getTitle())
                .startDate(startDate)
                .endDate(reqSchedule.getEndDate())
                .dayCount(reqSchedule.getDayCount())
                .dDay(dDay)  // 계산된 dDay 설정
                .build();

        return schedule;
    }


    public void update(ReqSchedule reqSchedule, String scheduleImgUrl){
        this.scheduleImgUrl = scheduleImgUrl;
        this.title = reqSchedule.getTitle();
        this.startDate = reqSchedule.getStartDate();
        this.endDate = reqSchedule.getEndDate();
        this.dayCount = reqSchedule.getDayCount();
    }

    public ResSchedule toDto(){
        return new ResSchedule(this.id, this.title, this.scheduleImgUrl, this.startDate, this.endDate, this.dayCount);
    }

}
