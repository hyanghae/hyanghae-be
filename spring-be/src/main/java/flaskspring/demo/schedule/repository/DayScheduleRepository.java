package flaskspring.demo.schedule.repository;

import flaskspring.demo.schedule.domain.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {
}
