package flaskspring.demo.travel.controller;

import flaskspring.demo.travel.dto.res.ImgRecommendationDto;
import flaskspring.demo.travel.service.TravelService;
import flaskspring.demo.travel.domain.NotFamousPlace;
import flaskspring.demo.travel.dto.req.FeatureScoreDto;
import flaskspring.demo.travel.dto.res.RecommendationDto;
import flaskspring.demo.travel.dto.res.ResRecommend;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/image")
    public ResponseEntity<List<ImgRecommendationDto>> share(@RequestParam("photo") MultipartFile placeImage) {
        try {
            // 이미지 파일을 바이트 배열로 변환
            byte[] imageBytes = placeImage.getBytes();

            // 플라스크 서버로 전송할 요청 생성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // ByteArrayResource로 이미지 파일을 감싸서 요청 본문에 추가
            ByteArrayResource contentsAsResource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return placeImage.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("photo", contentsAsResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            log.info("이미지 요청 들어옴");

            // 플라스크 서버로 이미지 전송 및 응답 받기
            ResponseEntity<List<ImgRecommendationDto>> response = restTemplate.exchange(
                    BASE_FLASK_URL + "/img-recommends/upload-image",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<ImgRecommendationDto>>() {}
            );

            // 플라스크 서버로부터 받은 응답 반환
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.error("Failed to share image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




}
