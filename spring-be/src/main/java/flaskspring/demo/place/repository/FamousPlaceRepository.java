package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.FamousPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamousPlaceRepository extends JpaRepository<FamousPlace, Long>, FamousPlaceRepositoryCustom {

    List<FamousPlace> findByIdIn(List<Long> ids);


}
