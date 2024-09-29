package flaskspring.demo.schedule.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;

import java.util.List;

public interface DayScheduleRepositoryCustom {

     List<Tuple> getScheduleDetail(Member member, Long dayScheduleId);
}
