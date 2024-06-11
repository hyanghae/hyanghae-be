package flaskspring.demo.explore.service;


import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.explore.repository.ExploreRepository;
import flaskspring.demo.home.dto.res.ImgRecommendationDto;
import flaskspring.demo.home.dto.res.PlaceScore;
import flaskspring.demo.home.dto.res.ResImageStandardScore;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.SortedPlaceName;
import flaskspring.demo.member.repository.SortedPlaceNameRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.member.setting.SettingService;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import flaskspring.demo.place.repository.PlaceRepository;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import flaskspring.demo.utils.FlaskService;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.Lint;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static flaskspring.demo.utils.ConvertUtil.convertToPlaceBriefList;

@RequiredArgsConstructor
@Service
@Transactional
public class ExploreService {


    private final ExploreRepository exploreRepository;
    private final SortedPlaceNameRepository sortedPlaceNameRepository;
    private final SettingService settingService;

    public ResPlaceRecommendPaging getExplorePlace(Member member, ExploreFilter filter, ExploreCursor cursor, int size) {
        if (filter.getSort().equals("alpha") || filter.getSort().equals("popular")) {
            List<Tuple> explorePlace = exploreRepository.findExplorePlaceWithoutTags(member, filter, cursor, size);
            return createResPlaceRecommendPaging(explorePlace, filter);
        } else if (filter.getSort().equals("recommend")) {
            if (isFirstRecommendRequest(cursor)) { // 처음 진입 시
                handleInitialRecommendationRequest(member);
            }
            List<Tuple> explorePlaceWithRecommend = sortedPlaceNameRepository.findExplorePlaceSorted(member, filter, cursor, size);
            return createResPlaceRecommendPaging(explorePlaceWithRecommend, filter);
        }
        return null;
    }

    private boolean isFirstRecommendRequest(ExploreCursor cursor) {
        return cursor.getIdCursor() == null;
    }

    private void handleInitialRecommendationRequest(Member member) {
        if (member.isRecommendPossible()) { // 추천 가능: 설정 정보 있음
            settingService.refreshData(member);
        } else { // 추천 불가 : 설정 정보 없음
            throw new BaseException(BaseResponseCode.MISSING_PREFERENCES_FOR_RECOMMENDATION);
        }
    }

    private ResPlaceRecommendPaging createResPlaceRecommendPaging(List<Tuple> explorePlace, ExploreFilter filter) {
        Long nextCountCursor = null;
        String nextNameCursor = null;
        Long nextIdCursor = null;

        List<ResPlaceBrief> placeDto = convertToPlaceBriefList(explorePlace);

        if (!explorePlace.isEmpty()) {
            Place place = explorePlace.get(explorePlace.size() - 1).get(0, Place.class);
            if (place == null) {
                throw new BaseException(BaseResponseCode.DATABASE_ERROR);
            }
            if ("alpha".equalsIgnoreCase(filter.getSort())) {
                nextNameCursor = place.getTouristSpotName();
                nextIdCursor = place.getId(); //가나다순일 때 idCursor는 안 쓰임
                return new ResPlaceRecommendPaging(placeDto, null, nextNameCursor, null);
            } else if ("popular".equalsIgnoreCase(filter.getSort())) {
                nextCountCursor = place.getRegisterCount();
                nextIdCursor = place.getId();
                return new ResPlaceRecommendPaging(placeDto, nextCountCursor, null, nextIdCursor);
            } else if ("recommend".equalsIgnoreCase(filter.getSort())) {
                nextIdCursor = explorePlace.get(explorePlace.size() - 1).get(4, Long.class);
                return new ResPlaceRecommendPaging(placeDto, null, null, nextIdCursor);
            }
        }
        return new ResPlaceRecommendPaging(placeDto, nextCountCursor, nextNameCursor, nextIdCursor);
    }


}


