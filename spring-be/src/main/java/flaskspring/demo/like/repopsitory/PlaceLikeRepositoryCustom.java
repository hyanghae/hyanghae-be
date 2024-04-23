package flaskspring.demo.like.repopsitory;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;

import java.util.List;

public interface PlaceLikeRepositoryCustom {

    List<Tuple> findLikedPlacesByMember(Member member);
}
