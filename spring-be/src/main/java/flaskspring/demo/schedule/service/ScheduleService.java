package flaskspring.demo.schedule.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.departure.repository.DepartureRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.register.repository.PlaceRegisterRepository;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.schedule.domain.DaySchedule;
import flaskspring.demo.schedule.domain.DaySchedulePlaceTag;
import flaskspring.demo.schedule.domain.Schedule;
import flaskspring.demo.schedule.dto.req.ReqSchedule;
import flaskspring.demo.schedule.dto.res.ResDaySchedule;
import flaskspring.demo.schedule.dto.res.ResSchedule;
import flaskspring.demo.schedule.dto.res.ResScheduleDto;
import flaskspring.demo.schedule.repository.DaySchedulePlaceTagRepository;
import flaskspring.demo.schedule.repository.DayScheduleRepository;
import flaskspring.demo.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static flaskspring.demo.utils.ConvertUtil.convertToPlaceBriefList;

@Service
@Transactional(readOnly = true)
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

    public ResScheduleDto getScheduleDetail(Member member, Long scheduleId) {

        Schedule schedule = scheduleRepository.findByMemberAndId(member, scheduleId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_SCHEDULE));
        List<DaySchedule> daySchedules = dayScheduleRepository.findBySchedule(schedule);

        List<ResDaySchedule> resDaySchedules = daySchedules.stream()
                .map(daySchedule -> {
                    List<Tuple> placeDetail = dayScheduleRepository.getScheduleDetail(member, daySchedule.getId());
                    List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(placeDetail);
                    return new ResDaySchedule(daySchedule, resPlaceBriefs);
                })
                .collect(Collectors.toList());// 반환된 리스트를 컬렉션으로 변환하여 반환

        return new ResScheduleDto(schedule, resDaySchedules);
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

        if (ChronoUnit.DAYS.between(reqSchedule.getStartDate(), reqSchedule.getEndDate()) != reqSchedule.getDayCount() - 1) {
            throw new BaseException(BaseResponseCode.INVALID_DAYCOUNT);
        }

        if (reqSchedule.getDayCount() != reqSchedule.getDaySchedules().size()) {
            throw new BaseException(BaseResponseCode.INVALID_SCHEDULE_DAYCOUNT);
        }
    }


    public List<ResSchedule> getScheduleUpcoming(Member member, LocalDate currentDate) {
        List<Schedule> schedules = scheduleRepository.findByMemberAndStartDateAfter(member, currentDate);
        return schedules.stream()
                .map(Schedule::toDto)
                .collect(Collectors.toList());
    }


    public List<ResSchedule> getPastSchedule(Member member, LocalDate currentDate) {
        List<Schedule> schedules = scheduleRepository.findByMemberAndStartDateBefore(member, currentDate);
        return schedules.stream()
                .map(Schedule::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void saveSchedule(Member member, ReqSchedule reqSchedule) {
        validate(reqSchedule);
        Long firstPlaceId = reqSchedule.getDaySchedules().get(0).getPlaceIds().get(0);
        String firstPlaceImgUrl = placeService.findPlaceById(firstPlaceId).getImagePath();
        Schedule schedule = Schedule.create(member, reqSchedule, firstPlaceImgUrl);
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
