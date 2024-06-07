package flaskspring.demo.image.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import jdk.jfr.TransitionTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UploadImageService {

    private final UploadImageRepository uploadImageRepository;
    private final MemberRepository memberRepository;

    public void getSettingImageFile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        UploadImage uploadImage = uploadImageRepository.findByMemberAndIsSetting(member, true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_IMAGE_EXCEPTION));

        uploadImage.getSavedImageUrl();
    }
}
