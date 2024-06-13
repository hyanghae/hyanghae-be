package flaskspring.demo.utils;

import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.home.dto.res.SimFamousPlaceDto2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlaskService {

    private final FlaskConfig flaskConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public SimFamousPlaceDto2 sendPostRequest(TagScoreDto tagScoreDto, String endPoint) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문에 데이터 설정
        HttpEntity<TagScoreDto> requestEntity = new HttpEntity<>(tagScoreDto, headers);

        // POST 요청 보내기
        return restTemplate.exchange(
                flaskConfig.getBaseUrl() + endPoint,
                HttpMethod.POST,
                requestEntity,
                SimFamousPlaceDto2.class
        ).getBody();
    }

    public LinkedHashMap<String, String> sendPostSimilarRequest(TagScoreDto tagScoreDto, Long cursor, int size) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문에 데이터 설정
        HttpEntity<TagScoreDto> requestEntity = new HttpEntity<>(tagScoreDto, headers);

        // POST 요청 보내기
        ResponseEntity<LinkedHashMap<String, String>> response = restTemplate.exchange(
                flaskConfig.getBaseUrl() + "similar" + "?cursor=" + cursor + "&size=" + size,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<LinkedHashMap<String, String>>() {}
        );

        return response.getBody();
    }

}
