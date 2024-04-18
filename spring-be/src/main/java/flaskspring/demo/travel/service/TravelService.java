package flaskspring.demo.travel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import flaskspring.demo.travel.domain.NotFamousPlace;
import flaskspring.demo.travel.repository.NotFamousPlaceRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class TravelService {

    private final NotFamousPlaceRepository notFamousPlaceRepository;

    public List<NotFamousPlace> getNotFamousPlace(List<Long> ids) {
        return notFamousPlaceRepository.findByIdIn(ids);
    }

    public NotFamousPlace getNotFamousPlace(Long id) {
        return notFamousPlaceRepository.findById(id).orElseThrow();
    }


    @PostConstruct
    void saveData() {
        String filePath = "C:\\Temp\\hyanghae\\NOT_FAMOUS_PLACE.json";
        saveNotFamousPlacesFromJsonFile(filePath);
    }

    @Transactional
    public void saveNotFamousPlacesFromJsonFile(String filePath) {
        try {
            // JSON 파일을 읽어서 NotFamousPlace 엔티티 리스트로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            List<NotFamousPlace> notFamousPlaces = Arrays.asList(objectMapper.readValue(new File(filePath), NotFamousPlace[].class));

            // 변환된 엔티티들을 데이터베이스에 저장
            for (NotFamousPlace notFamousPlace : notFamousPlaces) {
                notFamousPlaceRepository.save(notFamousPlace);
            }

            System.out.println("NotFamousPlaces 저장 완료");
        } catch (IOException e) {
            System.err.println("JSON 파일을 읽는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }


}
