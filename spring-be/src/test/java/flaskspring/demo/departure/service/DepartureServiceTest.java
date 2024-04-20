package flaskspring.demo.departure.service;

import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartureServiceTest {

    @Autowired
    DepartureService departureService;

    @Test
    void testSaveDeparture() {
        Long savedMemberId = 1L;


        for (int i = 1; i <= 3; i++) {
            ReqDeparture reqDeparture = new ReqDeparture();
            reqDeparture.setRoadAddress("Test Road Address" + i);
            reqDeparture.setMapX("123.456");
            reqDeparture.setMapY("789.012");

            departureService.saveDeparture(savedMemberId, reqDeparture);
        }

        ResDeparture recentDeparture = departureService.getRecentDeparture(savedMemberId);
        assertThat(recentDeparture.getRoadAddress()).isEqualTo("Test Road Address3");
    }

}