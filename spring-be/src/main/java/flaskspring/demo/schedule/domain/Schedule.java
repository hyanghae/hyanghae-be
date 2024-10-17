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
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    public static Schedule create(Member member, ReqSchedule reqSchedule, String scheduleImgUrl){
        Schedule schedule = Schedule.builder()
                .member(member)
                .scheduleImgUrl(scheduleImgUrl)
                .title(reqSchedule.getTitle())
                .startDate(reqSchedule.getStartDate())
                .endDate(reqSchedule.getEndDate())
                .dayCount(reqSchedule.getDayCount())
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


    public ResSchedule toDto() {
        // 한국 시간대 설정
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        // 현재 시간과 종료 시간을 한국 시간으로 가져옴
        ZonedDateTime nowSeoul = ZonedDateTime.now(seoulZoneId);
        ZonedDateTime endDateSeoul = this.endDate.atStartOfDay(seoulZoneId);

        // 현재 날짜와 종료 날짜 간의 차이를 계산하여 dDay를 설정
        int dDay = (int) java.time.Duration.between(nowSeoul, endDateSeoul).toDays();
        return new ResSchedule(this.id, this.title, this.scheduleImgUrl, this.startDate, this.endDate, this.dayCount, dDay);
    }

}
