package flaskspring.demo.tag.repository;

import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.tag.domain.FamousPlaceTagLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamousPlaceTagLogRepository extends JpaRepository<FamousPlaceTagLog, Long>, FamousPlaceTagLogRepositoryCustom {

    @Query("SELECT fptl FROM FamousPlaceTagLog fptl JOIN FETCH fptl.tag WHERE fptl.famousPlace = :famousPlace")
    List<FamousPlaceTagLog> findTagsByFamousPlace(@Param("famousPlace") FamousPlace famousPlace);


}
