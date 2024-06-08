package flaskspring.demo.place.service;


import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.repository.FamousPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FamousPlaceService {

    private final FamousPlaceRepository famousPlaceRepository;
    private final List<Long> Top24FamousPlaceIds
            = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 10L,
            11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L,
            21L, 22L, 23L, 24L);
    ;

    public List<ResFamous> get24FamousPlaces() {
        List<FamousPlace> top24FamousPlaces = famousPlaceRepository.findByIdIn(Top24FamousPlaceIds);
        return top24FamousPlaces.stream().map(ResFamous::new).toList();
    }
}
