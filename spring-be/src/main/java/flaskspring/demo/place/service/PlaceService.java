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

        // 지역 검색 가능한 경우
        if (performRegionOrCitySearch(member, searchQuery, cursor, filter, size, places)) {
            return createPagingResponse(places, cursor, size, 1L);
        }

        log.info("touristSpotName 여행지명 검색 가능!");

        // 검색어가 여행지명에 포함되는 경우
        if (performNameSearch(member, searchQuery, cursor, filter, size, places)) {
            return createPagingResponse(places, cursor, size, 2L);
        }

        log.info("Tag 태그 연관 검색 가능!");

        // 태그 검색 가능한 경우
        if (performTagSearch(member, searchQuery, cursor, filter, size, places)) {
            return createPagingResponse(places, cursor, size, 3L);
        }

        // 모든 검색 결과가 10개 미만인 경우
        List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places);
        return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + resPlaceBriefs.size(), 3L, null, false, null, null);
    }

    private boolean performRegionOrCitySearch(Member member, String searchQuery, ExploreCursor cursor, ExploreFilter filter, int size, List<Tuple> places) {
        String regionQuery = searchUtil.containRegionQuery(searchQuery);
        String cityQuery = regionQuery == null ? searchUtil.containCityQuery(searchQuery) : null;

        if (cursor.getIdCursor() == null || cursor.getIdCursor() == 1L) {
            if (regionQuery != null) {
                log.info("regionQuery 시군구 검색 가능!");
                places.addAll(placeRepository.findPlacesByRegionQuery(member, filter, regionQuery, cursor, size));
            } else if (cityQuery != null) {
                log.info("cityQuery 행정구역 검색 가능!");
                places.addAll(placeRepository.findPlacesByCityQuery(member, filter, cityQuery, cursor, size));
            }
            return places.size() >= size;
        }
        return false;
    }


    private boolean performNameSearch(Member member, String searchQuery, ExploreCursor cursor, ExploreFilter filter, int size, List<Tuple> places) {
        if (cursor.getIdCursor() == null || cursor.getIdCursor() < 2L) {
            cursor.setCountCursor(0L);
            places.addAll(placeRepository.findPlacesByNameQuery(member, filter, searchQuery, cursor, size - places.size()));
            return places.size() >= size;
        } else if (cursor.getIdCursor() == 2L) {
            places.addAll(placeRepository.findPlacesByNameQuery(member, filter, searchQuery, cursor, size));
            return places.size() >= size;
        }
        return false;
    }

    private boolean performTagSearch(Member member, String searchQuery, ExploreCursor cursor, ExploreFilter filter, int size, List<Tuple> places) {
        Long tagId = searchUtil.findTagBySearchQuery(searchQuery);
        if (tagId != null) {
            if (cursor.getIdCursor() == null || cursor.getIdCursor() < 3L) {
                cursor.setCountCursor(0L);
                places.addAll(placeRepository.findPlacesByTag(member, filter, tagId, cursor, size - places.size()));
                return places.size() >= size;
            } else if (cursor.getIdCursor() == 3L) {
                places.addAll(placeRepository.findPlacesByTag(member, filter, tagId, cursor, size));
                return places.size() >= size;
            }
        }
        return false;
    }

    private ResPlaceSearchPaging createPagingResponse(List<Tuple> places, ExploreCursor cursor, int size, Long nextIdCursor) {
        List<ResPlaceBrief> resPlaceBriefs = convertToPlaceBriefList(places.subList(0, Math.min(size, places.size())));
        return new ResPlaceSearchPaging(resPlaceBriefs, cursor.getCountCursor() + resPlaceBriefs.size(), nextIdCursor, null, false, null, null);
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
