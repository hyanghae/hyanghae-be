package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom{

    List<Place> findByIdIn(List<Long> ids);
}
