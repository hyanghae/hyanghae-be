package flaskspring.demo.like.repopsitory;

import flaskspring.demo.like.domain.PlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceLikeRepository extends JpaRepository<PlaceLike, Long>, PlaceLikeRepositoryCustom {

    boolean existsByMemberAndPlace(Member member, Place place);

    Optional<PlaceLike> findByMemberAndPlace(Member member, Place place);

}
