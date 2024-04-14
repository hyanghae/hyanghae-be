package flaskspring.demo.travel.controller;

import flaskspring.demo.travel.service.TravelService;
import flaskspring.demo.travel.domain.NotFamousPlace;
import flaskspring.demo.travel.dto.req.FeatureScoreDto;
import flaskspring.demo.travel.dto.res.RecommendationDto;
import flaskspring.demo.travel.dto.res.ResRecommend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static flaskspring.demo.utils.Constant.BASE_FLASK_URL;

@RestController
@RequestMapping("/api/recommends")
@RequiredArgsConstructor
@Slf4j
public class RecommendController {

    RestTemplate restTemplate = new RestTemplate();
    private final TravelService travelService;

    @GetMapping("")
    public ResponseEntity<ResRecommend> toPythonPost(@ModelAttribute FeatureScoreDto featureScoreDto) {
        System.out.println("python GET");

        System.out.println("featureScoreDto = " + featureScoreDto);
        // 요청 URL 생성
        String url = BASE_FLASK_URL + "recommends";

        String queryString = String.format("?feat_a=%d&feat_b=%d&feat_c=%d",
                featureScoreDto.getFeatA(),
                featureScoreDto.getFeatB(),
                featureScoreDto.getFeatC());
        url += queryString;

        ResponseEntity<RecommendationDto> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,  // 요청 본문이 없음
                RecommendationDto.class
        );
        RecommendationDto body = responseEntity.getBody();
        System.out.println("body = " + body);


        assert body != null;
        NotFamousPlace firstPlace = travelService.getNotFamousPlace(body.getFirstPlaceId());
        NotFamousPlace secondPlace = travelService.getNotFamousPlace(body.getSecondPlaceId());
        NotFamousPlace thirdPlace = travelService.getNotFamousPlace(body.getThirdPlaceId());


        List<NotFamousPlace> notFamousPlaces = Arrays.asList(firstPlace, secondPlace, thirdPlace);

        ResRecommend resRecommend = new ResRecommend(notFamousPlaces);

        return ResponseEntity.ok(resRecommend);
    }
}
