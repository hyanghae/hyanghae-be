package flaskspring.demo.tag.repository;

import flaskspring.demo.tag.domain.PlaceTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.travel.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceTagLogRepository extends JpaRepository<PlaceTagLog, Long>, PlaceTagLogRepositoryCustom {

  /*  @Query("SELECT p, " +
            "COLLECT(ptl.tag.tagName) AS tagNames " +
            "FROM PlaceTagLog ptl " +
            "JOIN ptl.tag t " +
            "JOIN ptl.place p " +
            "WHERE ptl.tag IN :tags " +
            "GROUP BY p")
    List<Object[]> findByTagIn(@Param("tags") List<Tag> tags);*/
}
