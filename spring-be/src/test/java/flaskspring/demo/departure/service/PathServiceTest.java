package flaskspring.demo.departure.service;

import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResSchedule;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.schedule.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    PathService pathService;

    @Autowired
    ScheduleService scheduleService;

    @Test
    void testShortestPath() {

        Long savedMemberId = 1L;

        ReqDeparture reqDeparture = new ReqDeparture();
        reqDeparture.setRoadAddress("Test Road Address");
        reqDeparture.setMapX(127.619842753323);
        reqDeparture.setMapY(37.2125169562113);
        scheduleService.saveDeparture(savedMemberId, reqDeparture);

        BaseResponse<ResSchedule> schedule = pathService.getSchedule(savedMemberId);

        ResSchedule result = schedule.getData();

        System.out.println("shortest = " + result.getFullDistance());
        System.out.println("______________________");
        // 여행지 정보 출력
        for (ResSchedulePlace place : result.getSchedulePlaces()) {
            System.out.println("여행지 ID: " + place.getPlaceId());
            System.out.println("여행지 이름: " + place.getPlaceName());
            System.out.println("여행지 이미지 URL: " + place.getPlaceImgUrl());
            System.out.println("좌표: " + place.getLocation());
            System.out.println("여행지 태그 목록: " + place.getTags());
            System.out.println("좋아요 수: " + place.getLikesCount());
            System.out.println("등록 수: " + place.getRegisterCount());
            System.out.println("좋아요 여부: " + place.getIsLiked());
            System.out.println("출발지로부터의 거리: " + place.getDistanceFromDeparture());
            System.out.println("이전 여행지로부터의 거리: " + place.getDistanceFromPrevious());
            System.out.println("등록 시간: " + place.getRegisteredTime());
            System.out.println();
            System.out.println("__________________________________");
        }
    }
}