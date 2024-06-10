package flaskspring.demo.recommend.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.dto.res.ResPlaceWithSim;
import flaskspring.demo.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static flaskspring.demo.utils.ConvertUtil.convertToPlaceBriefList;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final PlaceRepository placeRepository;
    private final MemberService memberService;

    public ResRisingPlacePaging getRisingPlaces(Long memberId, Long countCursor, Long placeId, int size) {
        Member member = memberService.findMemberById(memberId);
        List<Tuple> places = placeRepository.findRisingPlaces(member, countCursor, placeId, size);

        Long nextCountCursor = null;
        Long nextIdCursor = null;

        if (!places.isEmpty()) {
            Place place = places.get(places.size() - 1).get(0, Place.class);
            if (place == null) {
                throw new BaseException(BaseResponseCode.DATABASE_ERROR);
            }
            nextCountCursor = place.getRegisterCount();
            nextIdCursor = place.getId();
        }

        List<ResPlaceBrief> placeDto = convertToPlaceBriefList(places);
        return new ResRisingPlacePaging(placeDto, nextCountCursor, nextIdCursor);
    }




}
