package flaskspring.demo.tag.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.recommend.dto.res.ImgRecommendationDto;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.travel.repository.MemberTagLogRepository;
import flaskspring.demo.travel.repository.PlaceTagLogRepository;
import flaskspring.demo.travel.dto.res.ResPlace;
import flaskspring.demo.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final PlaceTagLogRepository placeTagLogRepository;
    private final PlaceRepository placeRepository;

    public List<ResPlace> getRecommendFeed(Long memberId, String sort) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        List<MemberTagLog> memberTagLogs = memberTagLogRepository.findByMember(member);

        List<Tag> tags = memberTagLogs.stream().map(MemberTagLog::getTag).toList();
        List<Tuple> tuples = placeTagLogRepository.getFeedByTags(tags, sort, member);

        List<ResPlace> resPlaceList = new ArrayList<>();
        for (Tuple tuple : tuples) {
            ResPlace resPlace = new ResPlace(tuple);
            resPlaceList.add(resPlace);
        }
        return resPlaceList;
    }


    public List<ResPlace> getRecommendFeed(Long memberId, List<ImgRecommendationDto> recommendationDtos) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        List<String> placeNames = recommendationDtos.stream()
                .map(ImgRecommendationDto::getName)
                .toList();

        List<Tuple> tuples = placeRepository.getFeedByPlaceNames(placeNames, member);
        List<ResPlace> resPlaceList = tuples.stream().map(ResPlace::new).toList();

        return resPlaceList;
    }
}
