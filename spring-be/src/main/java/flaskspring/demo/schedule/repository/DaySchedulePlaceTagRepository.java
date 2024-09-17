package flaskspring.demo.schedule.repository;

import flaskspring.demo.schedule.domain.DaySchedulePlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaySchedulePlaceTagRepository extends JpaRepository<DaySchedulePlaceTag, Long> {
}
