package flaskspring.demo.departure.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.dto.res.ResLocation;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.departure.repository.DepartureRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.register.repository.PlaceRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static flaskspring.demo.departure.domain.QDeparturePoint.departurePoint;

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


//    public List<ResSchedulePlace> getShortestSchedule(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
//
//        Optional<DeparturePoint> departure = departureRepository.findFirstByMemberOrderByCreatedTimeDesc(member);
//        List<Tuple> registeredPlaces = placeRegisterRepository.findScheduleByMember(member);
//        List<ResSchedulePlace> schedulePlaces = registeredPlaces.stream().map(ResSchedulePlace::new).toList();
//
//        if (departure.isPresent()) { //출발지가 등록되어 있을 경우 거리순 정렬
//            DeparturePoint departurePoint = departure.get();
//            List<ResSchedulePlace> mutableSchedulePlaces = new ArrayList<>(schedulePlaces);
//        }
    }

    public List<ResSchedulePlace> getSchedule(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        Optional<DeparturePoint> departure = departureRepository.findFirstByMemberOrderByCreatedTimeDesc(member);
        List<Tuple> registeredPlaces = placeRegisterRepository.findScheduleByMember(member);
        List<ResSchedulePlace> schedulePlaces = registeredPlaces.stream().map(ResSchedulePlace::new).toList();

        if (departure.isPresent()) {
            DeparturePoint departurePoint = departure.get();
            List<ResSchedulePlace> mutableSchedulePlaces = new ArrayList<>(schedulePlaces);

         /*   mutableSchedulePlaces
                    .forEach(schedulePlace -> schedulePlace
                            .setDistanceFromDeparture(
                                    departurePoint
                                            .getLocation()
                                            .calculateDistance(schedulePlace.getMapX(), schedulePlace.getMapY())));*/

            mutableSchedulePlaces.sort(Comparator.comparingDouble(ResSchedulePlace::getDistanceFromDeparture));

//            List<ResSchedulePlace> updatedSchedulePlaces = new ArrayList<>();
//            updatedSchedulePlaces.add(mutableSchedulePlaces.get(0));
//            // 각 여행지와 바로 앞 여행지 간의 거리를 계산하여 필드에 추가
//            AtomicInteger i = new AtomicInteger(1);
//            for (int index = 1; index < mutableSchedulePlaces.size(); index++) {
//                ResSchedulePlace previousPlace = mutableSchedulePlaces.get(index - 1);
//                ResSchedulePlace currentPlace = mutableSchedulePlaces.get(index);
//                currentPlace.setDistanceFromPrevious(previousPlace.getMapX(), previousPlace.getMapY());
//                updatedSchedulePlaces.add(currentPlace);
//            }
//            return updatedSchedulePlaces;
        }

        return null;

    }


}
