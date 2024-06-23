package flaskspring.demo.recommend.service;

import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
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

    @Test
    void getRisingPlacesTest() {

        ExploreCursor exploreCursor = new ExploreCursor("1", null, "1");
        ResRisingPlacePaging risingPlaces = recommendService.getRisingPlaces(1L,exploreCursor , 10);
        System.out.println("risingPlaces = " + risingPlaces);
    }
}