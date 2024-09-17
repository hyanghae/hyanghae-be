package flaskspring.demo.schedule.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByMember(Member member);
}
