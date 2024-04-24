package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.FamousPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FamousPlaceRepository extends JpaRepository<FamousPlace, Long> {



}
