package flaskspring.demo.tag.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.like.repopsitory.PlaceLikeRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.register.repository.PlaceRegisterRepository;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.PlaceTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.repository.MemberTagLogRepository;
import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.dto.res.ResPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static flaskspring.demo.tag.domain.QPlaceTagLog.placeTagLog;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final PlaceTagLogRepository placeTagLogRepository;
    private final PlaceLikeRepository placeLikeRepository;
    private final PlaceRegisterRepository placeRegisterRepository;

    public List<ResPlace> getRecommendFeed(Long memberId, String sort) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        List<MemberTagLog> memberTagLogs = memberTagLogRepository.findByMember(member);

        List<Tag> tags = memberTagLogs.stream().map(MemberTagLog::getTag).toList();
        List<Tuple> tuples = placeTagLogRepository.findByTagIn(tags, sort, member);

        List<ResPlace> resPlaceList = new ArrayList<>();
        for (Tuple tuple : tuples) {
            Place place = tuple.get(0, Place.class);
            String tagNamesString = tuple.get(1, String.class); // 두 번째 항목인 String을 가져옵니다.
            Long sameTagCount = tuple.get(2, Long.class); // 두 번째 항목인 String을 가져옵니다.
            Boolean isLiked = tuple.get(3, Boolean.class); // Boolean으로 가져옵니다.
            Boolean isRegistered = tuple.get(4, Boolean.class); // Boolean으로 가져옵니다.

            List<String> tagNames = Arrays.asList(tagNamesString.split(","));
            ResPlace resPlace = new ResPlace(place, tagNames, sameTagCount, isLiked, isRegistered);
            resPlaceList.add(resPlace);
        }
        return resPlaceList;
        //  return sortResPlaces(resPlaceList, sort);
    }

}
