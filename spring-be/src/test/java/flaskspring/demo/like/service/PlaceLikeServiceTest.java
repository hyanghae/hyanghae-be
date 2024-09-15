package flaskspring.demo.like.service;

import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.Res.GeneralSignUpRes;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class PlaceLikeServiceTest {

    @Autowired
    MemberService memberService;



    @Autowired
    PlaceRepository placeRepository;


    @Test
    void likeTest() {
        Long savedMemberId = createMember();
        Long placeId = 1L;


        Place place = placeRepository.findById(1L).orElseThrow();
        assertThat(place.getLikeCount()).isEqualTo(1);
    }

    @Test
    void likeCancelTest() {
        Long savedMemberId = createMember();
        Long placeId = 1L;

        Place place = placeRepository.findById(1L).orElseThrow();
        assertThat(place.getLikeCount()).isEqualTo(0);
    }

    private Long createMember() {
        GeneralSignUpReq member = GeneralSignUpReq.builder()
                .account("testAccount")
                .password("testPassword")
                .name("testName")
                .build();

        GeneralSignUpRes generalSignUpRes = memberService.generalSignUp(member);
        return generalSignUpRes.getMemberId();
    }
}