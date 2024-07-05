package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

    List<Place> findByIdIn(List<Long> ids);

    @Query("SELECT p, " +
            "(6371 * acos(cos(radians(:givenMapY)) * cos(radians(p.location.mapY)) * cos(radians(p.location.mapX) - radians(:givenMapX)) + " +
            "sin(radians(:givenMapY)) * sin(radians(p.location.mapY)))) AS distance " +
            "FROM Place p " +
            "WHERE (6371 * acos(cos(radians(:givenMapY)) * cos(radians(p.location.mapY)) * cos(radians(p.location.mapX) - radians(:givenMapX)) + " +
            "sin(radians(:givenMapY)) * sin(radians(p.location.mapY)))) <= :distance " +
            "ORDER BY distance")
    List<Object[]> findLocationsWithinDistance(@Param("givenMapY") double givenMapY,
                                               @Param("givenMapX") double givenMapX,
                                               @Param("distance") double distance);






}

