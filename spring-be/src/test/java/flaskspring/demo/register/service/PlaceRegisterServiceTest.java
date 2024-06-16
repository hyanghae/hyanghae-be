package flaskspring.demo.register.service;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.register.service.PlaceRegisterService;
import flaskspring.demo.place.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class PlaceRegisterServiceTest {

    @Autowired
    PlaceRegisterService placeRegisterService;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    void registerPlaceTest() {
        Long savedMemberId = createMember();
        Long placeId = 1L;
        Member member = memberService.findMemberById(savedMemberId);
        placeRegisterService.registerPlace(member, placeId);
        Place place = placeRepository.findById(placeId).orElseThrow();
        assertThat(place.getRegisterCount()).isEqualTo(1);
    }

    @Test
    void unregisterPlaceTest() {
        Long savedMemberId = createMember();
        Long placeId = 1L;

        Member member = memberService.findMemberById(1L);

        placeRegisterService.registerPlace(member, placeId);
        placeRegisterService.registerPlace(member, placeId); // 취소
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
