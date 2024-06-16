package flaskspring.demo.departure.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.departure.repository.DepartureRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.place.register.repository.PlaceRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {


    private final MemberRepository memberRepository;
    private final DepartureRepository departureRepository;
    private final PlaceRegisterRepository placeRegisterRepository;

    public void saveDeparture(Long memberId, ReqDeparture reqDeparture) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        DeparturePoint departurePoint = DeparturePoint.create(reqDeparture, member);
        departureRepository.save(departurePoint);
    }

    public ResDeparture getRecentDeparture(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        DeparturePoint recentDeparture = departureRepository.findFirstByMemberOrderByCreatedTimeDesc(member)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_DEPARTURE_EXCEPTION));

        return new ResDeparture(recentDeparture);

    }

    @Deprecated
    public List<ResSchedulePlace> getSchedule(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        Optional<DeparturePoint> departure = departureRepository.findFirstByMemberOrderByCreatedTimeDesc(member);
        List<Tuple> registeredPlaces = placeRegisterRepository.findScheduleByMember(member);
        List<ResSchedulePlace> schedulePlaces = registeredPlaces.stream().map(ResSchedulePlace::new).toList();

        if (departure.isPresent()) {
            DeparturePoint departurePoint = departure.get();
            List<ResSchedulePlace> mutableSchedulePlaces = new ArrayList<>(schedulePlaces);
        }

        return null;
    }


}
