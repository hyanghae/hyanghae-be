package flaskspring.demo.member.setting;

import com.querydsl.core.Tuple;
import flaskspring.demo.explore.repository.ExploreRepository;
import flaskspring.demo.home.dto.res.PlaceScore;
import flaskspring.demo.home.dto.res.ResImageStandardScore;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.SortedPlaceName;
import flaskspring.demo.member.repository.SortedPlaceNameRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.repository.PlaceRepository;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.utils.FlaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SettingService {

    private final MemberService memberService;
    private final SortedPlaceNameRepository sortedPlaceNameRepository;
    private final ExploreRepository exploreRepository;
    private final PlaceRepository placeRepository;
    private final UploadImageService uploadImageService;
    private final FlaskService flaskService;

    public void refreshData(Member member) {
        if (member.isRefreshNeeded()) { // 갱신 필요
            processRecommendation(member);
        }
    }

    private void processRecommendation(Member member) {
        sortedPlaceNameRepository.deleteByMember(member);
        List<PlaceScore> placeScores = getPlaceScores(member);
        saveSortedPlaceNamesAndUpdateRefreshStatus(member, placeScores);
    }

    private void saveSortedPlaceNamesAndUpdateRefreshStatus(Member member, List<PlaceScore> placeScores) {
        if (placeScores != null) {
            List<PlaceScore> sortedPlaceScores = getSortedPlaceScores(placeScores);
            saveSortedPlaceNames(member, sortedPlaceScores);
            member.setRefreshNotNeeded();
        }
    }

    private List<PlaceScore> getSortedPlaceScores(List<PlaceScore> placeScores) {
        // sum 필드를 기준으로 내림차순으로 정렬
        placeScores.sort(Comparator.comparing(PlaceScore::getSum).reversed());

        return placeScores;
    }

    private void saveSortedPlaceNames(Member member, List<PlaceScore> sortedPlaceScores) {
        List<SortedPlaceName> sortedPlaceNames = new ArrayList<>();
        for (int i = 0; i < sortedPlaceScores.size(); i++) {
            PlaceScore placeScore = sortedPlaceScores.get(i);
            SortedPlaceName sortedPlaceName = SortedPlaceName.create(member, placeScore);
            sortedPlaceNames.add(sortedPlaceName);
        }
        sortedPlaceNameRepository.saveAllSortedPlace(sortedPlaceNames);
    }


    private List<PlaceScore> getPlaceScores(Member member) {
        List<Long> tagIds = memberService.getRegisteredTag(member).stream().map(Tag::getId).toList();
        List<PlaceScore> placeScores;
        if (!tagIds.isEmpty()) { // 태그 설정이 있음
            placeScores = getPlaceScoresWithStandardizedScores(exploreRepository.findExplorePlaceWithTags(member, tagIds));
        } else { // 이미지 설정만 있음 -> 여행지 이름만 할당
            placeScores = getPlaceScoreWithNoScore(placeRepository.findAll());
        }

        if (uploadImageService.isSettingImageExist(member)) { // 이미지 설정이 있는 경우에만 이미지 스코어 업데이트
            MultipartFile settingImageFile = uploadImageService.getSettingImageFile(member);
            List<ResImageStandardScore> resultFromFlask = flaskService.getResultFromFlask(settingImageFile);
            updatePlaceScoresWithImageScores(placeScores, resultFromFlask);
        }
        return placeScores;
    }

    private List<PlaceScore> getPlaceScoresWithStandardizedScores(List<Tuple> explorePlace) {
        List<Integer> scores = explorePlace.stream()
                .map(tuple -> tuple.get(1, Integer.class)) // totalTagScore 가져오기
                .toList();

        // 평균 계산
        double mean = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        // 표준 편차 계산
        double variance = scores.stream()
                .mapToDouble(score -> Math.pow(score - mean, 2))
                .average()
                .orElse(0.0);
        double standardDeviation = Math.sqrt(variance);

        List<PlaceScore> placeScores = new ArrayList<>();

        // 각 여행지의 표준 점수 계산
        for (Tuple tuple : explorePlace) {
            Place place = tuple.get(0, Place.class);
            Integer totalTagScore = tuple.get(1, Integer.class);

            double standardizedScore = (totalTagScore - mean) / standardDeviation;
            placeScores.add(new PlaceScore(place.getTouristSpotName(), standardizedScore, null));
        }
        return placeScores;
    }


    private List<PlaceScore> getPlaceScoreWithNoScore(List<Place> places) {

        List<PlaceScore> placeScores = new ArrayList<>();
        // 각 여행지 이름만 할당
        for (Place place : places) {
            placeScores.add(new PlaceScore(place.getTouristSpotName(), null, null));
        }
        return placeScores;
    }

    private void updatePlaceScoresWithImageScores(List<PlaceScore> placeScores, List<ResImageStandardScore> resultFromFlask) {
        // ResImageStandardScore 리스트의 각 요소에 대해 이름이 일치하는 경우 이미지 점수를 추가
        for (ResImageStandardScore resImageStandardScore : resultFromFlask) {
            String name = resImageStandardScore.getName();
            Double imageScore = resImageStandardScore.getScore();
            // 이미 존재하는 이름인 경우 기존의 이미지 점수에 더함
            Optional<PlaceScore> existingPlaceScore = placeScores.stream()
                    .filter(p -> p.getName().equals(name))
                    .findFirst();
            if (existingPlaceScore.isPresent()) {
                existingPlaceScore.get().addImageScore(imageScore);
            } else {
                placeScores.add(new PlaceScore(name, null, imageScore));
            }
        }
    }

}
