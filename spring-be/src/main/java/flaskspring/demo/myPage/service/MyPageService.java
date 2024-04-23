package flaskspring.demo.myPage.service;

import com.querydsl.core.Tuple;
import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.like.repopsitory.PlaceLikeRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.myPage.dto.res.ResLikedPlace;
import flaskspring.demo.myPage.dto.res.ResMyPageMain;
import flaskspring.demo.myPage.dto.res.ResUploadedImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final PlaceLikeRepository placeLikeRepository;
    private final UploadImageRepository uploadImageRepository;

    public ResMyPageMain getMyPageData(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        List<ResLikedPlace> likedPlaces = getLikedPlaces(member);
        List<ResUploadedImage> uploadedImage = getUploadedImage(member);

        return new ResMyPageMain(likedPlaces, uploadedImage);
    }

    private List<ResUploadedImage> getUploadedImage(Member member){
        List<UploadImage> uploadImages = uploadImageRepository.findByMemberOrderByCreatedTimeDesc(member);
        return uploadImages.stream().map(ResUploadedImage::new).collect(Collectors.toList());
    }

    private List<ResLikedPlace> getLikedPlaces(Member member) {
        List<Tuple> likedPlaces = placeLikeRepository.findLikedPlacesByMember(member);
        return likedPlaces.stream().map(ResLikedPlace::new).toList();
    }
}
