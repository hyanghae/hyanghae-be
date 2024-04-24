package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member);
}
