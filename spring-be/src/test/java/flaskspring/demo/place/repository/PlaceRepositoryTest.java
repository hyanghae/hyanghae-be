package flaskspring.demo.place.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;

    @Test
    void queryTest() {
        placeRepository.findLocationsWithinDistance(37, 127, 50);
    }
}