package flaskspring.demo.travel.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.travel.dto.res.ResPlace;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member);
}
