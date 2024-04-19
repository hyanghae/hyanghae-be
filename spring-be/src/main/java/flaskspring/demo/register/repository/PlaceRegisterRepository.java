package flaskspring.demo.register.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.register.domain.PlaceRegister;
import flaskspring.demo.travel.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRegisterRepository extends JpaRepository<PlaceRegister, Long> {

    Optional<PlaceRegister> findByMemberAndPlace(Member member, Place place);


}
