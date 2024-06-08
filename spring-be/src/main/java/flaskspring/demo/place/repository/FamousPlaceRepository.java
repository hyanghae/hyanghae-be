package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface FamousPlaceRepository extends JpaRepository<FamousPlace, Long> {


    List<FamousPlace> findByIdIn(List<Long> ids);
}
