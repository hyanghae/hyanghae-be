package flaskspring.demo.register.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.register.domain.PlaceRegister;
import flaskspring.demo.register.repository.PlaceRegisterRepository;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PlaceRegisterService {
    private final PlaceRegisterRepository placeRegisterRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    public boolean registerPlace(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        Optional<PlaceRegister> register = placeRegisterRepository.findByMemberAndPlace(member, place);
        if (register.isEmpty()) {
            // 등록되지 않은 경우 등록 진행
            PlaceRegister newRegistration = PlaceRegister.createRegister(member, place);
            placeRegisterRepository.save(newRegistration);
            place.increaseRegisterCount();
            return true; // 등록 동작
        }
        placeRegisterRepository.delete(register.get());
        place.decreaseRegisterCount();
        return false; // 등록 취소 동작
    }
}
