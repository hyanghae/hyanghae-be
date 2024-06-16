package flaskspring.demo.place.register.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;

import java.util.List;

public interface PlaceRegisterRepositoryCustom {

        List<Tuple> findScheduleByMember(Member member);
}
