package flaskspring.demo.departure.service;

import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.register.service.PlaceRegisterService;
import flaskspring.demo.schedule.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PlaceRegisterService placeRegisterService;
    @Autowired
    MemberService memberService;

    @Test
    void registerTest() {
        Member member = memberService.findMemberById(1L);
        placeRegisterService.registerPlace(member, 41L);
    }

    @Test
    void testSchedule() {
        Long savedMemberId = 1L;


        List<ResSchedulePlace> schedule = scheduleService.getSchedule(savedMemberId);

        for (ResSchedulePlace schedulePlace : schedule) {

            System.out.println("Place ID: " + schedulePlace.getPlaceId());
            System.out.println("Place Name: " + schedulePlace.getPlaceName());
            System.out.println("Place Image URL: " + schedulePlace.getPlaceImgUrl());
            System.out.println("Place mapX: " + schedulePlace.getLocation().getMapX());
            System.out.println("Place mapY: " + schedulePlace.getLocation().getMapY());
            System.out.println("Tag Names: " + schedulePlace.getTags());
            System.out.println("Likes Count: " + schedulePlace.getLikesCount());
            System.out.println("Register Count: " + schedulePlace.getRegisterCount());
            System.out.println("Is Liked: " + schedulePlace.getIsLiked());
            System.out.println("Distance From Departure: " + schedulePlace.getDistanceFromDeparture());
            System.out.println("RegisteredTime: " + schedulePlace.getRegisteredTime());
            System.out.println("--------------------------------------------");
        }
    }

    @Test
    void testSchedule_when_departure_exists() {
        Long savedMemberId = 1L;

        ReqDeparture reqDeparture = new ReqDeparture();
        reqDeparture.setRoadAddress("Test Road Address");
        reqDeparture.setMapX(127.619842753323);
        reqDeparture.setMapY(37.2125169562113);
        scheduleService.saveDeparture(savedMemberId, reqDeparture);

        List<ResSchedulePlace> schedule = scheduleService.getSchedule(savedMemberId);

        for (ResSchedulePlace schedulePlace : schedule) {

            System.out.println("Place ID: " + schedulePlace.getPlaceId());
            System.out.println("Place Name: " + schedulePlace.getPlaceName());
            System.out.println("Place Image URL: " + schedulePlace.getPlaceImgUrl());
            System.out.println("Place mapX: " + schedulePlace.getLocation().getMapX());
            System.out.println("Place mapY: " + schedulePlace.getLocation().getMapY());
            System.out.println("Tag Names: " + schedulePlace.getTags());
            System.out.println("Likes Count: " + schedulePlace.getLikesCount());
            System.out.println("Register Count: " + schedulePlace.getRegisterCount());
            System.out.println("Is Liked: " + schedulePlace.getIsLiked());
            System.out.println("Distance From Departure: " + schedulePlace.getDistanceFromDeparture());
            System.out.println("Distance From Previous: " + schedulePlace.getDistanceFromPrevious());
            System.out.println("RegisteredTime: " + schedulePlace.getRegisteredTime());
            System.out.println("--------------------------------------------");
        }
    }

    @Test
    void testSaveDeparture() {
        Long savedMemberId = 1L;

        for (int i = 1; i <= 3; i++) {
            ReqDeparture reqDeparture = new ReqDeparture();
            reqDeparture.setRoadAddress("Test Road Address" + i);
            reqDeparture.setMapX(123.456);
            reqDeparture.setMapY(789.012);

            scheduleService.saveDeparture(savedMemberId, reqDeparture);
        }

        ResDeparture recentDeparture = scheduleService.getRecentDeparture(savedMemberId);
        assertThat(recentDeparture.getRoadAddress()).isEqualTo("Test Road Address3");
    }

}