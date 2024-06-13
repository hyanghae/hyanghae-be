package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.place.domain.Place;
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

    @Test
    void queryTest() {
        placeRepository.findLocationsWithinDistance(37, 127, 50);
    }

    @Test
    void testFindSimilarPlacesByKNN() {
        Long idCursor = 0L;
        int[] queryPoint = new int[24];
        int size = 10;

        queryPoint[0] = 1;
        queryPoint[1] = 1;
        queryPoint[2] = 100;


        System.out.println("queryPoint = " + Arrays.toString(queryPoint));



        // 반환된 리스트가 null이 아닌지 확인합니다.
     //   assertNotNull(places);

        // 반환된 리스트가 비어있지 않은지 확인합니다.
    //    assertFalse(places.isEmpty());

        // 테스트에 맞게 추가적인 검증을 수행합니다.
        // 예를 들어, 반환된 장소가 특정 조건을 만족하는지를 확인할 수 있습니다.
    }
}