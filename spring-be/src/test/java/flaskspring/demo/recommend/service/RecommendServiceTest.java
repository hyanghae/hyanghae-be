package flaskspring.demo.recommend.service;

import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.utils.cursor.ExploreCursor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecommendServiceTest {

    @Autowired
    RecommendService recommendService;
    @Autowired
    MemberService memberService;

    @Test
    void getRisingPlacesTest() {
        Member member = memberService.findMemberById(1L);
        ExploreCursor exploreCursor = new ExploreCursor("1", null, "1");
        ResRisingPlacePaging risingPlaces = recommendService.getRisingPlaces(member, exploreCursor, 10);
        System.out.println("risingPlaces = " + risingPlaces);
    }
}