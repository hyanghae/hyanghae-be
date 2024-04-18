package flaskspring.demo.travel.repository;

import flaskspring.demo.travel.domain.NotFamousPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotFamousPlaceRepository extends JpaRepository<NotFamousPlace, Long> {

    List<NotFamousPlace> findByIdIn(List<Long> ids);
}
