package flaskspring.demo.place.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.dto.res.ResPlaceDetail;
import flaskspring.demo.place.dto.res.ResTagSim;
import flaskspring.demo.place.repository.FamousPlaceRepository;
import flaskspring.demo.place.repository.PlaceRepository;
import flaskspring.demo.tag.domain.FamousPlaceTagLog;
import flaskspring.demo.tag.dto.res.ResTag;
import flaskspring.demo.tag.repository.FamousPlaceTagLogRepository;
import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.home.dto.res.SimFamousPlaceDto;
import flaskspring.demo.tag.domain.PlaceTagLog;
import flaskspring.demo.utils.FlaskConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    RestTemplate restTemplate = new RestTemplate();
    private final PlaceRepository placeRepository;
    private final FamousPlaceTagLogRepository famousPlaceTagLogRepository;
    private final PlaceTagLogRepository placeTagLogRepository;
    private final FlaskConfig flaskConfig;
    private final FamousPlaceRepository famousPlaceRepository;


    public ResPlaceDetail getPlaceDetail(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        List<PlaceTagLog> tagsByPlace = placeTagLogRepository.findTagsByPlace(place);

        // 요청 본문으로 전송할 데이터 설정
        TagScoreDto tagScoreDto = new TagScoreDto(tagsByPlace);
        String url = flaskConfig.getBaseUrl() + "recommends";
        //flask서버로 부터 가장 유사한 인기여행지 얻기
        SimFamousPlaceDto famousPlaceDto = sendPostRequest(tagScoreDto, url, restTemplate);

        FamousPlace famousPlace = famousPlaceRepository.findById(famousPlaceDto.getFirstPlaceId())
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        List<FamousPlaceTagLog> tagsByFamousPlace = famousPlaceTagLogRepository.findTagsByFamousPlace(famousPlace);

        List<ResTagSim> resTagSims = calculateTagSimilarity(tagsByPlace, tagsByFamousPlace);
        int totalSimScore = getTotalSimScore(resTagSims, tagsByFamousPlace.size());

        return new ResPlaceDetail(place, famousPlace, totalSimScore, resTagSims);
    }

    public int getTotalSimScore(List<ResTagSim> resTagSims, int totalFamousPlaceTagCount) {
        double scorePerEach = (double) 1 / totalFamousPlaceTagCount;
        double sum = resTagSims.stream()
                .mapToDouble(resTagSim -> (double) resTagSim.getSimScore() / 100)
                .sum();
        double totalSimScore = ((scorePerEach * sum) * 100);
        System.out.println("totalSimScore = " + totalSimScore);
        return (int) totalSimScore;
    }

    public List<ResTagSim> calculateTagSimilarity(List<PlaceTagLog> tagsByPlace, List<FamousPlaceTagLog> tagsByFamousPlace) {
        return tagsByPlace.stream()
                .flatMap(placeTagLog ->
                        tagsByFamousPlace.stream()
                                .filter(famousPlaceTagLog -> placeTagLog.getTag().getId().equals(famousPlaceTagLog.getTag().getId()))
                                .map(famousPlaceTagLog -> {
                                    double placeScore = placeTagLog.getTagScore();
                                    double famousPlaceScore = famousPlaceTagLog.getTagScore();

                                    // 유사도 점수 계산
                                    double similarityScore = (placeScore / famousPlaceScore) * 100;
                                    similarityScore = Math.min(similarityScore, 100); // 유사도 점수가 100을 초과하지 않도록 제한

                                    return ResTagSim.builder()
                                            .tagId(placeTagLog.getTag().getId())
                                            .tagName(placeTagLog.getTag().getTagName().getValue())
                                            .simScore((int) similarityScore)
                                            .build();
                                })
                )
                .collect(Collectors.toList());
    }


    public SimFamousPlaceDto sendPostRequest(TagScoreDto tagScoreDto, String url, RestTemplate restTemplate) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문에 데이터 설정
        HttpEntity<TagScoreDto> requestEntity = new HttpEntity<>(tagScoreDto, headers);

        // POST 요청 보내기
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                SimFamousPlaceDto.class
        ).getBody();

    }

    private List<ResTag> createResTags(String tagIdsString, String tagNamesString) {
        List<ResTag> resTags = new ArrayList<>();
        String[] tagIds = tagIdsString.split(",");
        String[] tagNames = tagNamesString.split(",");

        for (int i = 0; i < tagIds.length; i++) {
            Long tagId = Long.parseLong(tagIds[i]);
            String tagName = tagNames[i];
            resTags.add(new ResTag(tagId, tagName));
        }

        return resTags;
    }
}
