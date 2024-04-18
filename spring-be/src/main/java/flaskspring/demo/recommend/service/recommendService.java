package flaskspring.demo.recommend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import flaskspring.demo.travel.domain.NotFamousPlace;
import flaskspring.demo.travel.repository.NotFamousPlaceRepository;
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

    private final NotFamousPlaceRepository notFamousPlaceRepository;

    public List<NotFamousPlace> getNotFamousPlace(List<Long> ids) {
        return notFamousPlaceRepository.findByIdIn(ids);
    }

    public NotFamousPlace getNotFamousPlace(Long id) {
        return notFamousPlaceRepository.findById(id).orElseThrow();
    }







}
