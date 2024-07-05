package flaskspring.demo.place.service;


import com.querydsl.core.Tuple;
import flaskspring.demo.config.redis.cache.RedisCacheable;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.repository.FamousPlaceRepository;
import flaskspring.demo.place.repository.PlaceRepository;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
import flaskspring.demo.search.dto.res.ResPlaceSearchPaging;
import flaskspring.demo.tag.domain.FamousPlaceTagLog;
import flaskspring.demo.tag.repository.FamousPlaceTagLogRepository;
import flaskspring.demo.utils.FlaskService;
import flaskspring.demo.utils.filter.ExploreFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static flaskspring.demo.utils.ConvertUtil.convertToPlaceBriefList;
import static flaskspring.demo.utils.ConvertUtil.convertToPlaceBriefList2;

@Service
@Transactional
@RequiredArgsConstructor
public class FamousPlaceService {

    private final MemberService memberService;
    private final FamousPlaceRepository famousPlaceRepository;
    private final FamousPlaceTagLogRepository famousPlaceTagLogRepository;
    private final PlaceRepository placeRepository;
    private final FlaskService flaskService;
    private final List<Long> Top24FamousPlaceIds
            = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 10L,
            11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L,
            21L, 22L, 23L, 24L);
    ;

    @RedisCacheable(cacheName = "famousPlaces", expireTime = 60)
    public List<ResFamous> get24FamousPlaces() {
        List<FamousPlace> top24FamousPlaces = famousPlaceRepository.findByIdIn(Top24FamousPlaceIds);
        return top24FamousPlaces.stream().map(ResFamous::new).collect(Collectors.toList());
    }

    public Optional<FamousPlace> getFamousPlace(String touristSpotName) {
        return famousPlaceRepository.findByTouristSpotName(touristSpotName);
    }

    public List<ResFamous> getFamousPlace(ExploreFilter exploreFilter) {
        List<FamousPlace> famousPlaces = famousPlaceRepository.findByCity(exploreFilter.getCityFilter());
        return famousPlaces.stream().map(ResFamous::new).collect(Collectors.toList());
    }

    public ResPlaceSearchPaging getSimilarPlaces(Long memberId, ExploreFilter filter, Long famousPlaceId, Long cursor, int size) {
        Member member = memberService.findMemberById(memberId);

        FamousPlace famousPlace = findByFamousPlaceId(famousPlaceId);
        List<FamousPlaceTagLog> tagsByFamousPlace = famousPlaceTagLogRepository.findTagsByFamousPlace(famousPlace);
        TagScoreDto tagScoreDto = new TagScoreDto(tagsByFamousPlace);
        System.out.println("tagScoreDto = " + tagScoreDto);
        List<jakarta.persistence.Tuple> similarPlacesByKNN2 = placeRepository.findSimilarPlacesByKNN2(member, filter, cursor, tagScoreDto, size);

        CityCode cityCode = CityCode.fromCityKoreanName(famousPlace.getCity());

        List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList2(similarPlacesByKNN2);
        return new ResPlaceSearchPaging(resPlaceBriefs, cursor + 1, null, null, true, famousPlaceId, cityCode.getParamName());
    } //유명 여행지와 유사한 비인기 여행지들 반환

    public List<ResPlaceBrief> getSimilarPlacesDeprecated(Long memberId, Long famousPlaceId, Long cursor, int size) {
        Member member = memberService.findMemberById(memberId);

        FamousPlace famousPlace = findByFamousPlaceId(famousPlaceId);
        List<FamousPlaceTagLog> tagsByFamousPlace = famousPlaceTagLogRepository.findTagsByFamousPlace(famousPlace);
        TagScoreDto tagScoreDto = new TagScoreDto(tagsByFamousPlace);

        LinkedHashMap<String, String> stringStringMap = flaskService.sendPostSimilarRequest(tagScoreDto, cursor, size);
        List<Long> placeIds = extractPlaceIds(stringStringMap);
        List<Tuple> similarPlaces = placeRepository.findSimilarPlaces(member, placeIds);
        List<Tuple> sortedSimilarPlaces = sortSimilarPlaces(similarPlaces, stringStringMap);

        return convertToPlaceBriefList(sortedSimilarPlaces);
    }

    private List<Long> extractPlaceIds(LinkedHashMap<String, String> stringStringMap) {
        return stringStringMap.values().stream()
                .map(value -> Long.parseLong(value.replace("place", "")))
                .collect(Collectors.toList());
    }

    private List<Tuple> sortSimilarPlaces(List<Tuple> similarPlaces, LinkedHashMap<String, String> stringStringMap) {
        Map<Long, Integer> placeIdIndexMap = stringStringMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Long.parseLong(entry.getValue().replace("place", "")),
                        entry -> Integer.parseInt(entry.getKey().replace("place", "")) - 1
                ));

        return similarPlaces.stream()
                .sorted(Comparator.comparing(t -> placeIdIndexMap.get(t.get(0, Place.class).getId())))
                .collect(Collectors.toList());
    }


    public FamousPlace findByFamousPlaceId(Long famousPlaceId) {
        return famousPlaceRepository.findById(famousPlaceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
    }


}
