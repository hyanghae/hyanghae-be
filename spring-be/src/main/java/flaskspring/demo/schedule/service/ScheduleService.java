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
import flaskspring.demo.place.domain.Location;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.register.repository.PlaceRegisterRepository;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.schedule.domain.DaySchedule;
import flaskspring.demo.schedule.domain.DaySchedulePlaceTag;
import flaskspring.demo.schedule.domain.Departure;
import flaskspring.demo.schedule.domain.Schedule;
import flaskspring.demo.schedule.dto.req.ReqSchedule;
import flaskspring.demo.schedule.dto.res.ResDaySchedule;
import flaskspring.demo.schedule.dto.res.ResSchedule;
import flaskspring.demo.schedule.dto.res.ResScheduleDto;
import flaskspring.demo.schedule.repository.DaySchedulePlaceTagRepository;
import flaskspring.demo.schedule.repository.DayScheduleRepository;
import flaskspring.demo.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
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

        List<ResDaySchedule> resDaySchedules = new ArrayList<>();

        // 매 DaySchedule마다 출발지 혹은 이전 여행지 좌표를 추적
        double prevMapX = 0;
        double prevMapY = 0;
        boolean isFirstPlace = true;  // 첫 장소 여부 확인

        for (DaySchedule daySchedule : daySchedules) {
            // DaySchedule의 출발지가 있으면 그 좌표를 사용하고, 없으면 이전 여행지 좌표 사용
            if (daySchedule.getDeparture() != null && daySchedule.getDeparture().getLocation() != null) {
                prevMapX = daySchedule.getDeparture().getLocation().getMapX();
                prevMapY = daySchedule.getDeparture().getLocation().getMapY();
                isFirstPlace = false;  // 출발지를 사용했으므로 첫 장소 아님
            }

            List<Tuple> placeDetail = dayScheduleRepository.getScheduleDetail(member, daySchedule.getId());
            List<ResPlaceBrief> resPlaceBriefs = new ArrayList<>();

            for (Tuple tuple : placeDetail) {
                ResPlaceBrief resPlaceBrief = new ResPlaceBrief(tuple);

                double currentMapX = resPlaceBrief.getMapX();
                double currentMapY = resPlaceBrief.getMapY();

                double distance = 0;
                if (!isFirstPlace) {
                    // 첫 장소가 아닌 경우에만 거리 계산
                    distance = calculateDistance(prevMapY, prevMapX, currentMapY, currentMapX);
                }

                // 거리를 포맷팅하고 이전 좌표를 갱신
                resPlaceBrief.setDistFromPrev(formatDistance(distance));
                prevMapX = currentMapX;
                prevMapY = currentMapY;

                resPlaceBriefs.add(resPlaceBrief);
                isFirstPlace = false;  // 첫 장소가 지나면 이후는 거리를 계산
            }

            resDaySchedules.add(new ResDaySchedule(daySchedule, resPlaceBriefs));
        }

        return new ResScheduleDto(schedule, resDaySchedules);
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구의 반경 (킬로미터)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        // 소수점 한 자리로 반올림
        return Math.round(distance * 10) / 10.0;
    }

    public static double formatDistance(double distance) {
        if (distance < 0.1) {
            return 0.1; // 0.1km 미만은 0.1로 고정하여 반환
        } else if (distance < 1) {
            // 소수점 첫째 자리에서 반올림하여 double로 변환
            return Math.round(distance * 10.0) / 10.0; // 1km 미만은 소수점 첫 자리까지 나타내기
        } else {
            // 1km 이상은 정수로 반올림하여 반환
            return Math.round(distance);
        }
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

    @Transactional
    public void updateSchedule(Member member, ReqSchedule reqSchedule, Long scheduleId) {
        //유효성 확인
        validate(reqSchedule);
        //기존 데이터는 제거
        deleteScheduleData(member, scheduleId);
        // 스케줄 조회
        Schedule schedule = scheduleRepository.findByMemberAndId(member, scheduleId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_SCHEDULE));

        // 사진 조회
        Long firstPlaceId = reqSchedule.getDaySchedules().get(0).getPlaceIds().get(0);
        String firstPlaceImgUrl = placeService.findPlaceById(firstPlaceId).getImagePath();

        //업데이트
        schedule.update(reqSchedule, firstPlaceImgUrl);

        reqSchedule.getDaySchedules()
                .stream()
                .forEach(reqDaySchedule -> {
                    DaySchedule daySchedule = DaySchedule.create(schedule, reqDaySchedule);
                    dayScheduleRepository.save(daySchedule);

                    reqDaySchedule.getPlaceIds().stream().forEach(placeId -> {
                        Place place = placeService.findPlaceById(placeId);
                        daySchedulePlaceTagRepository.save(DaySchedulePlaceTag.create(daySchedule, place));
                    });
                });
    }

    @Transactional
    private void deleteScheduleData(Member member, Long scheduleId) {
        // 스케줄 조회
        Schedule schedule = scheduleRepository.findByMemberAndId(member, scheduleId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_SCHEDULE));

        // 스케줄에 관련된 DaySchedule 삭제
        List<DaySchedule> daySchedules = dayScheduleRepository.findBySchedule(schedule);

        daySchedules.forEach(daySchedule -> {
            // 각 DaySchedule에 관련된 DaySchedulePlaceTag 삭제
            daySchedulePlaceTagRepository.deleteByDaySchedule(daySchedule.getId());
        });
        dayScheduleRepository.deleteBySchedule(schedule.getId());

    }


}
