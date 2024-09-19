package flaskspring.demo.remove.service;


import flaskspring.demo.departure.repository.DepartureRepository;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.repository.SortedPlaceNameRepository;
import flaskspring.demo.place.register.repository.PlaceRegisterRepository;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import flaskspring.demo.schedule.domain.DaySchedule;
import flaskspring.demo.schedule.domain.Schedule;
import flaskspring.demo.schedule.repository.DaySchedulePlaceTagRepository;
import flaskspring.demo.schedule.repository.DayScheduleRepository;
import flaskspring.demo.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveService {

    private final PlaceRegisterRepository placeRegisterRepository;
    private final SortedPlaceNameRepository sortedPlaceNameRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final UploadImageRepository uploadImageRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final DayScheduleRepository dayScheduleRepository;
    private final DaySchedulePlaceTagRepository daySchedulePlaceTagRepository;
    private final DepartureRepository departureRepository;



    public void remove(Member member) {
        // memberId를 추출합니다
        Long memberId = member.getMemberId();

        // 각 레포지토리의 deleteByMemberHard 메서드를 호출하여 데이터를 삭제합니다
        placeRegisterRepository.deleteByMemberHard(memberId);
        sortedPlaceNameRepository.deleteByMemberHard(memberId);
        memberTagLogRepository.deleteByMemberHard(memberId);
        uploadImageRepository.deleteByMemberHard(memberId);

        // 1. 해당 member와 관련된 모든 Schedule을 가져옵니다.
        List<Schedule> schedules = scheduleRepository.findByMember(member);
        for (Schedule schedule : schedules) {
            List<DaySchedule> daySchedules = dayScheduleRepository.findBySchedule(schedule);
            for (DaySchedule daySchedule : daySchedules) {
                // day_schedule_place_tag에서 참조하는 데이터를 먼저 삭제합니다.
                daySchedulePlaceTagRepository.deleteByDaySchedule(daySchedule.getId());
            }
            dayScheduleRepository.deleteBySchedule(schedule.getId());
        }

        scheduleRepository.deleteByMemberHard(memberId);
        departureRepository.deleteByMemberId(memberId);
        memberRepository.deleteMemberFromDB(memberId);
    }
}
