package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.utils.filter.ExploreFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FamousPlaceRepositoryCustomImplTest {

    @Autowired
    FamousPlaceRepository famousPlaceRepository;

    @Test
    void findSimilarFamousPlacesTest(){

        ExploreFilter filter = new ExploreFilter(null, CityCode.fromCityName("GANGWON"));
        List<FamousPlace> similarFamousPlaces = famousPlaceRepository.findSimilarFamousPlaces(filter, List.of(1L,2L,3L));

        System.out.println("similarFamousPlaces = " + similarFamousPlaces);
    }

}