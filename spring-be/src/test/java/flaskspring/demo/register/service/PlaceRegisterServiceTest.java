package flaskspring.demo.register.service;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceRegisterServiceTest {

    @Autowired
    PlaceRegisterService placeRegisterService;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void registerPlaceTest() {
        Long savedMemberId = createMember();
        Long placeId = 1L;

        placeRegisterService.registerPlace(savedMemberId, placeId);
        Place place = placeRepository.findById(placeId).orElseThrow();
        assertThat(place.getRegisterCount()).isEqualTo(1);
    }

    @Test
    void unregisterPlaceTest() {
        Long savedMemberId = createMember();
        Long placeId = 1L;

        placeRegisterService.registerPlace(savedMemberId, placeId);
        placeRegisterService.registerPlace(savedMemberId, placeId); // 취소
        Place place = placeRepository.findById(placeId).orElseThrow();
        assertThat(place.getRegisterCount()).isEqualTo(0);
    }

    private Long createMember() {
        // 새로운 회원 생성 및 회원 ID 반환
        Member member = Member.builder()
                .account("testAccount")
                .password("testPassword")
                .name("testName")
                .build();
        memberRepository.save(member);
        return member.getMemberId();
    }
}
