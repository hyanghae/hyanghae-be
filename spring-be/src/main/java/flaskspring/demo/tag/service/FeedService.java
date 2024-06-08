package flaskspring.demo.tag.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.dto.res.ResPlaceWithSim;
import flaskspring.demo.home.dto.res.ImgRecommendationDto;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final PlaceTagLogRepository placeTagLogRepository;
    private final PlaceRepository placeRepository;
    private final MemberService memberService;

    public List<ResPlace> getRecommendFeed(Long memberId, String sort) {
        Member member = memberService.findMemberById(memberId);
        List<Tag> tags = memberService.getRegisteredTag(member);
        List<Tuple> tuples = placeTagLogRepository.getFeedByTags(tags, sort, member);

        List<ResPlace> resPlaceList = new ArrayList<>();
        for (Tuple tuple : tuples) {
            ResPlace resPlace = new ResPlace(tuple);
            resPlaceList.add(resPlace);
        }
        return resPlaceList;
    }


    public List<ResPlaceWithSim> getRecommendFeed(Long memberId, List<ImgRecommendationDto> recommendationDtos) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        List<String> placeNames = recommendationDtos.stream()
                .map(ImgRecommendationDto::getName)
                .toList();

        List<Tuple> tuples = placeRepository.getFeedByPlaceNames(placeNames, member);

        List<ResPlaceWithSim> resPlaceList = tuples.stream()
                .map(ResPlaceWithSim::new)
                .toList();

        setSimilarityScoresAndSort(resPlaceList, recommendationDtos);

        return resPlaceList;
    }

    private void setSimilarityScoresAndSort(List<ResPlaceWithSim> resPlaceList, List<ImgRecommendationDto> recommendationDtos) {
        resPlaceList.forEach(resPlaceWithSim -> {
            // recommendationDtos에서 해당 장소의 이름을 찾아 유사도 점수를 설정
            ImgRecommendationDto recommendationDto = recommendationDtos.stream()
                    .filter(dto -> dto.getName().equals(resPlaceWithSim.getPlaceName()))
                    .findFirst()
                    .orElse(null);
            if (recommendationDto != null) {
                resPlaceWithSim.setSimilarityScore(recommendationDto.getSimilarity());
            }
        });

        resPlaceList.sort(Comparator.comparing(ResPlaceWithSim::getSimilarityScore).reversed());
    }

}
