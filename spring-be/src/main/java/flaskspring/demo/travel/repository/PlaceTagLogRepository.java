package flaskspring.demo.travel.repository;

import flaskspring.demo.tag.domain.PlaceTagLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTagLogRepository extends JpaRepository<PlaceTagLog, Long>, PlaceTagLogRepositoryCustom {

  /*  @Query("SELECT p, " +
            "COLLECT(ptl.tag.tagName) AS tagNames " +
            "FROM PlaceTagLog ptl " +
            "JOIN ptl.tag t " +
            "JOIN ptl.place p " +
            "WHERE ptl.tag IN :tags " +
            "GROUP BY p")
    List<Object[]> getFeedByTags(@Param("tags") List<Tag> tags);*/
}
