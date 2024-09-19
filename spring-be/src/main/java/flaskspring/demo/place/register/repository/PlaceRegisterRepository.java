package flaskspring.demo.place.register.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.register.domain.PlaceRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRegisterRepository extends JpaRepository<PlaceRegister, Long> ,PlaceRegisterRepositoryCustom {

    Optional<PlaceRegister> findByMemberAndPlace(Member member, Place place);

    boolean existsByMemberAndPlace(Member member, Place place);

    @Modifying
    @Query(value = "DELETE FROM place_register WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberHard(@Param("memberId") Long memberId);

//    @Query("SELECT pr.place " +
//            "FROM PlaceRegister pr " +
//            "JOIN FETCH pr.place " +
//            "WHERE pr.member = :member")
//    List<Place> findPlacesByMember(@Param("member") Member member);


}
