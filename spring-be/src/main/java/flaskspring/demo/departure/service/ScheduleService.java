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
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.register.repository.PlaceRegisterRepository;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.schedule.domain.DaySchedule;
import flaskspring.demo.schedule.domain.DaySchedulePlaceTag;
import flaskspring.demo.schedule.domain.Schedule;
import flaskspring.demo.schedule.dto.req.ReqSchedule;
import flaskspring.demo.schedule.dto.res.ResSchedule;
import flaskspring.demo.schedule.repository.DaySchedulePlaceTagRepository;
import flaskspring.demo.schedule.repository.DayScheduleRepository;
import flaskspring.demo.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {


    private final MemberRepository memberRepository;
    private final DepartureRepository departureRepository;
    private final PlaceRegisterRepository placeRegisterRepository;
    private final ScheduleRepository scheduleRepository;
    private final DayScheduleRepository dayScheduleRepository;
    private final PlaceService placeService;
    private final DaySchedulePlaceTagRepository daySchedulePlaceTagRepository;

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
        List<Tuple> registeredPlaces = placeRegisterRepository.findSavedPlacesByMember(member, Pageable.unpaged());
        List<ResSchedulePlace> schedulePlaces = registeredPlaces.stream().map(ResSchedulePlace::new).toList();

        if (departure.isPresent()) {
            DeparturePoint departurePoint = departure.get();
            List<ResSchedulePlace> mutableSchedulePlaces = new ArrayList<>(schedulePlaces);
        }

        return null;
    }

    private void validate(ReqSchedule reqSchedule) {
        if (reqSchedule.getDayCount() != reqSchedule.getDaySchedules().size()) {
            throw new BaseException(BaseResponseCode.INVALID_SCHEDULE);
        }
    }

    @Transactional
    public List<ResSchedule> getSchedule(Member member){
        List<Schedule> schedules = scheduleRepository.findByMember(member);
        return schedules.stream()
                .map(Schedule::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveSchedule(Member member, ReqSchedule reqSchedule) {
        Schedule schedule = Schedule.create(member, reqSchedule);
        validate(reqSchedule);
        scheduleRepository.save(schedule);

        reqSchedule.getDaySchedules()
                .stream()
                .forEach(reqDaySchedule -> {
                    DaySchedule daySchedule = DaySchedule.create(schedule, reqDaySchedule);
                    dayScheduleRepository.save(daySchedule);
                    reqDaySchedule.getPlaceIds().stream().forEach(placeId ->
                    {
                        Place place = placeService.findPlaceById(placeId);
                        daySchedulePlaceTagRepository.save(DaySchedulePlaceTag.create(daySchedule, place));
                    });
                });
    }

}
