package flaskspring.demo.departure.repository;

import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartureRepository extends JpaRepository<DeparturePoint, Long> {


    Optional<DeparturePoint> findFirstByMemberOrderByCreatedTimeDesc(Member member);

}
