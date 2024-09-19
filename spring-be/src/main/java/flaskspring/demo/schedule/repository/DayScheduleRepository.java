package flaskspring.demo.schedule.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.domain.DaySchedule;
import flaskspring.demo.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {



    @Modifying
    @Query(value = "DELETE FROM day_schedule WHERE schedule_id = :scheduleId", nativeQuery = true)
    void deleteBySchedule(@Param("scheduleId") Long scheduleId);

    List<DaySchedule> findBySchedule(Schedule schedule);
}
