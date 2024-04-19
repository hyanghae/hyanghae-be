package flaskspring.demo.tag.repository;

import flaskspring.demo.travel.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTagLogRepository extends JpaRepository<Place, Long> {
}
