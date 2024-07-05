package flaskspring.demo.place.register.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.register.domain.PlaceRegister;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceRegisterRepositoryCustom {
        List<Tuple> findSavedPlacesByMember(Member member, Pageable pageable);
}
