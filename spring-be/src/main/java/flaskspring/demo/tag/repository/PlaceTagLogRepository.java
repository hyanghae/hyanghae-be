package flaskspring.demo.tag.repository;

import flaskspring.demo.place.domain.Place;
import flaskspring.demo.tag.domain.PlaceTagLog;
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
    List<Object[]> getFeedByTags(@Param("tags") List<Tag> tags);*/

    @Query("SELECT ptl FROM PlaceTagLog ptl JOIN FETCH ptl.tag WHERE ptl.place = :place")
    List<PlaceTagLog> findTagsByPlace(@Param("place") Place place);

}
