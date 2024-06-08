package flaskspring.demo.recommend.service;


import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import flaskspring.demo.place.repository.PlaceRepository;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ExploreService {

    private final MemberService memberService;
    private final PlaceTagLogRepository placeTagLogRepository;

    public List<ResPlace> getExplorePlace(Long memberId, String sort) {
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
}
