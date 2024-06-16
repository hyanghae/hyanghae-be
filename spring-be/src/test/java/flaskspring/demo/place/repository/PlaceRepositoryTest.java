package flaskspring.demo.place.repository;

import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.Place;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;

@SpringBootTest
@Transactional
class PlaceRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    MemberService memberService;

    @Test
    void queryTest() {
        placeRepository.findLocationsWithinDistance(37, 127, 50);
    }

    @Test
    void testFindSimilarPlacesByKNN() {
        Member member = memberService.findMemberById(1L);

        Long idCursor = 0L;

        int size = 10;

        TagScoreDto tagScoreDto = TagScoreDto.builder()
                .tag1(1)
                .tag2(5)
                .tag3(10)
                .build();



        // 반환된 리스트가 null이 아닌지 확인합니다.
        //   assertNotNull(places);

        // 반환된 리스트가 비어있지 않은지 확인합니다.
        //    assertFalse(places.isEmpty());

        // 테스트에 맞게 추가적인 검증을 수행합니다.
        // 예를 들어, 반환된 장소가 특정 조건을 만족하는지를 확인할 수 있습니다.
    }
}