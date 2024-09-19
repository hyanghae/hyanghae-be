package flaskspring.demo.departure.repository;

import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartureRepository extends JpaRepository<DeparturePoint, Long> {


    Optional<DeparturePoint> findFirstByMemberOrderByCreatedTimeDesc(Member member);

    @Modifying
    @Query("DELETE FROM DeparturePoint dp WHERE dp.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
