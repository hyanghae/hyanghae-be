package flaskspring.demo.recommend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.repository.PlaceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
