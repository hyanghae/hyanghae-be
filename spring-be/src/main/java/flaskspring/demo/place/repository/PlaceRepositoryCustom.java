package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member);

    List<Tuple> findRisingPlaces(Member member, @Param("countCursor") Long countCursor, @Param("placeId") Long placeId, @Param("size") int size);


    List<Tuple> findSimilarPlaces(Member member, List<Long> placeIds);

}
