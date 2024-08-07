package flaskspring.demo.place.register.repository;

import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.register.domain.PlaceRegister;
import flaskspring.demo.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRegisterRepository extends JpaRepository<PlaceRegister, Long> ,PlaceRegisterRepositoryCustom {

    Optional<PlaceRegister> findByMemberAndPlace(Member member, Place place);

    boolean existsByMemberAndPlace(Member member, Place place);


//    @Query("SELECT pr.place " +
//            "FROM PlaceRegister pr " +
//            "JOIN FETCH pr.place " +
//            "WHERE pr.member = :member")
//    List<Place> findPlacesByMember(@Param("member") Member member);


}
