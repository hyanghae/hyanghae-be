package flaskspring.demo.schedule.repository;

import flaskspring.demo.schedule.domain.DaySchedulePlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DaySchedulePlaceTagRepository extends JpaRepository<DaySchedulePlaceTag, Long>, DayScheduleRepositoryCustom {

    @Modifying
    @Query(value = "DELETE FROM day_schedule_place_tag WHERE day_schedule_id = :dayScheduleId", nativeQuery = true)
    void deleteByDaySchedule(@Param("dayScheduleId") Long dayScheduleId);

}
