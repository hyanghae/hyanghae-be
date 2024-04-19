package flaskspring.demo.travel.repository;

import flaskspring.demo.travel.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByIdIn(List<Long> ids);
}
