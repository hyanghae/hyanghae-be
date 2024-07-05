package flaskspring.demo.place.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.dto.res.ResPlaceDetail;
import flaskspring.demo.place.dto.res.ResSimilarity;
import flaskspring.demo.place.dto.res.ResTagSim;
import flaskspring.demo.place.repository.FamousPlaceRepository;
import flaskspring.demo.place.repository.PlaceRepository;
import flaskspring.demo.search.SearchUtil;
import flaskspring.demo.search.dto.res.ResPlaceSearchPaging;
import flaskspring.demo.tag.domain.FamousPlaceTagLog;
import flaskspring.demo.tag.domain.PlaceTagLog;
import flaskspring.demo.tag.dto.res.ResTag;
import flaskspring.demo.tag.repository.FamousPlaceTagLogRepository;
import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import flaskspring.demo.utils.FlaskService;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static flaskspring.demo.utils.ConvertUtil.convertToPlaceBriefList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final FamousPlaceTagLogRepository famousPlaceTagLogRepository;
    private final PlaceTagLogRepository placeTagLogRepository;
    private final FamousPlaceRepository famousPlaceRepository;
    private final FamousPlaceService famousPlaceService;
    private final SearchUtil searchUtil;

    public ResPlaceSearchPaging getPlacesBySearching(Member member, String searchQuery, ExploreCursor cursor, ExploreFilter filter, int size) {
        List<Tuple> places = new ArrayList<>();
        Long tempCursor;
        // 지역 검색 가능한 경우

        String regionQuery = searchUtil.containRegionQuery(searchQuery);
        if (regionQuery != null) { // 시군구 검색 가능한 경우
            log.info("regionQuery 시군구 검색 가능!");
            List<Tuple> byRegionQuery = placeRepository.findPlacesByRegionQuery(member, filter, regionQuery, cursor, size);
            places.addAll(byRegionQuery);
            if (places.size() >= size) {
                List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places.subList(0, size));
                return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + size, 1L, null, false, null, null);
            }
        } else {
            String cityQuery = searchUtil.containCityQuery(searchQuery);
            if (cityQuery != null) { // 행정구역이라도 검색 가능한 경우
                log.info("cityQuery 행정구역 검색 가능!");
                List<Tuple> byCityQuery = placeRepository.findPlacesByCityQuery(member, filter, cityQuery, cursor, size);
                places.addAll(byCityQuery);
                if (places.size() >= size) {
                    List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places.subList(0, size));
                    return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + size, 1L, null, false, null, null);
                }
            }
        }

        log.info("touristSpotName 여행지명 검색 가능!");

        // 검색어가 여행지명에 포함되는 경우
        if (cursor.getIdCursor() == null || cursor.getIdCursor() < 2L) {
            int temp1 = places.size();
            cursor.setCountCursor(0L);
            List<Tuple> byNameQuery = placeRepository.findPlacesByNameQuery(member, filter, searchQuery, cursor, size - temp1); //필요한 만큼만 리턴
            tempCursor = (long) byNameQuery.size() + 1; //가져온 갯수 체크
            places.addAll(byNameQuery);
            if (places.size() >= size) { // ex) 7개로 다 채운 경우
                List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places);
                return new ResPlaceSearchPaging(resPlaceBriefs, tempCursor, 2L, null, false, null, null);
            }
        } else if (cursor.getIdCursor() == 2L) {
            List<Tuple> byNameQuery = placeRepository.findPlacesByNameQuery(member, filter, searchQuery, cursor, size);
            places.addAll(byNameQuery);
            if (places.size() >= size) {
                List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places);
                return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + size, 2L, null, false, null, null);
            }
        }

        // 태그 검색이 가능한 경우
        log.info("tag 태그 대응 검색 가능!");
        Long tagId = searchUtil.findTagBySearchQuery(searchQuery);

        if (tagId != null && (cursor.getIdCursor() == null || cursor.getIdCursor() < 3L)) {
            int temp2 = places.size();
            cursor.setCountCursor(0L);
            List<Tuple> placesByTag = placeRepository.findPlacesByTag(member, filter, tagId, cursor, size - temp2); //필요한 만큼
            tempCursor = (long) placesByTag.size() + 1; //사이즈 체크
            places.addAll(placesByTag);
            if (places.size() >= size) {
                List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places);
                return new ResPlaceSearchPaging(resPlaceBriefs, tempCursor, 3L, null, false, null, null);
            }

        }
        if (tagId != null && (cursor.getIdCursor() == null || cursor.getIdCursor() == 3L)) {
            List<Tuple> placesByTag = placeRepository.findPlacesByTag(member, filter, tagId, cursor, size);
            places.addAll(placesByTag);
            if (places.size() >= size) {
                List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places);
                return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + size, 3L, null, false, null, null);
            }
        }

        List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places); //모든 검색 결과 10개 미만
        return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + resPlaceBriefs.size(), 3L, null, false, null, null);
    }


    public ResPlaceDetail getPlaceDetail(Member member, Long placeId) {
        Tuple placeDetailTuple = placeRepository.findPlaceDetail(member, placeId);
        Place place = findPlaceById(placeId);
        List<Tuple> nearbyPlaces = placeRepository.findNearbyPlaces(member, place);
        List<ResPlaceBrief> nearbyPlacesDto = convertToPlaceBriefList(nearbyPlaces);

        return new ResPlaceDetail(placeDetailTuple, nearbyPlacesDto);
    }


    public List<ResFamous> getSimilarFamousPlace(ExploreFilter filter, Long placeId) {
        Place place = findPlaceById(placeId);
        List<Long> tagIds = placeTagLogRepository.findTagsByPlace(place).stream()
                .map(placeTagLog -> placeTagLog.getTag().getId()).toList();

        List<ResFamous> resFamous = famousPlaceRepository.findSimilarFamousPlaces(filter, tagIds).stream()
                .map(ResFamous::new)
                .toList();
        return resFamous;
    }

    public ResSimilarity getSimilarity(Long placeId, Long famousPlaceId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        List<PlaceTagLog> tagsByPlace = placeTagLogRepository.findTagsByPlace(place);

        FamousPlace famousPlace = famousPlaceService.findByFamousPlaceId(famousPlaceId);
        List<FamousPlaceTagLog> tagsByFamousPlace = famousPlaceTagLogRepository.findTagsByFamousPlace(famousPlace);

        List<ResTagSim> resTagSims = calculateTagSimilarity(tagsByPlace, tagsByFamousPlace);
        int totalSimScore = getTotalSimScore(resTagSims, tagsByFamousPlace.size());

        return new ResSimilarity(totalSimScore, resTagSims);
    }

    private int getTotalSimScore(List<ResTagSim> resTagSims, int totalFamousPlaceTagCount) {
        double scorePerEach = (double) 1 / totalFamousPlaceTagCount;
        double sum = resTagSims.stream()
                .mapToDouble(resTagSim -> (double) resTagSim.getSimScore() / 100)
                .sum();
        double totalSimScore = ((scorePerEach * sum) * 100);
        System.out.println("totalSimScore = " + totalSimScore);
        return (int) totalSimScore;
    }

    private List<ResTagSim> calculateTagSimilarity(List<PlaceTagLog> tagsByPlace, List<FamousPlaceTagLog> tagsByFamousPlace) {
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

    public Place findPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
    }
}
