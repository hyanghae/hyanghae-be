package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.utils.filter.ExploreFilter;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member);

    List<Tuple> findRisingPlaces(Member member, @Param("countCursor") Long countCursor, @Param("placeId") Long placeId, @Param("size") int size);

    Tuple findPlaceDetail(Member member, Long placeId);

    List<Tuple> findSimilarPlaces(Member member, List<Long> placeIds);

    List<Tuple> findNearbyPlaces(Member member, Place place);

    List<jakarta.persistence.Tuple> findSimilarPlacesByKNN2(Member member, ExploreFilter filter, Long countCursor, TagScoreDto tagScoreDto, int size);

}
