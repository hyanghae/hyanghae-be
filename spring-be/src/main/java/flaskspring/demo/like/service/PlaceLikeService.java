package flaskspring.demo.like.service;


import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.like.domain.PlaceLike;
import flaskspring.demo.like.repopsitory.PlaceLikeRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceLikeService {

    private final PlaceLikeRepository placeLikeRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;


    public boolean likePlace(Long memberId, Long placeId) {
        boolean isLikeAction;

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        Optional<PlaceLike> placeLike = placeLikeRepository.findByMemberAndPlace(member, place);
        if (placeLike.isEmpty()) {
            place.increaseLikeCount(); // 좋아요 + 1
            PlaceLike placeLikeObj = PlaceLike.createLike(member, place);
            placeLikeRepository.save(placeLikeObj);
            return true;
        }
        place.decreaseLikeCount(); // 좋아요 - 1
        placeLikeRepository.deleteById(placeLike.get().getId());
        return false;

    }

}
