package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member);

    List<Tuple> findRisingPlaces(Member member, ExploreCursor exploreCursor, @Param("size") int size);

    Tuple findPlaceDetail(Member member, Long placeId);

    List<Tuple> findSimilarPlaces(Member member, List<Long> placeIds);

    List<Tuple> findNearbyPlaces(Member member, Place place);

    List<jakarta.persistence.Tuple> findSimilarPlacesByKNN2(Member member, ExploreFilter filter, Long countCursor, TagScoreDto tagScoreDto, int size);

    List<Tuple> findPlacesByRegionQuery(Member member, ExploreFilter filter, String regionQuery, ExploreCursor cursor, int size);

    List<Tuple> findPlacesByCityQuery(Member member, ExploreFilter filter, String cityQuery, ExploreCursor cursor, int size);

    List<Tuple> findPlacesByNameQuery(Member member, ExploreFilter filter, String nameQuery, ExploreCursor cursor, int size);

    List<Tuple> findPlacesByTag(Member member, ExploreFilter filter, Long tagId, ExploreCursor cursor, int size);


}
