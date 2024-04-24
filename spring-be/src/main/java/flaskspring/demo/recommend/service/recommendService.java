package flaskspring.demo.recommend.service;

import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class recommendService {

    private final PlaceRepository PlaceRepository;

    public List<Place> getPlace(List<Long> ids) {
        return PlaceRepository.findByIdIn(ids);
    }

    public Place getPlace(Long id) {
        return PlaceRepository.findById(id).orElseThrow();
    }







}
