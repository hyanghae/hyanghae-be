package flaskspring.demo.register.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.register.domain.PlaceRegister;
import flaskspring.demo.register.repository.PlaceRegisterRepository;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PlaceRegisterService {
    private final PlaceRegisterRepository placeRegisterRepository;
    private final PlaceService placeService;
    private final MemberService memberService;

    private static final int MAX_REGISTRATION_COUNT = 20;

    public boolean registerPlace(Long memberId, Long placeId) {
        Member member = memberService.findMemberById(memberId);
        Place place = placeService.findPlaceById(placeId);
        // 여행지 등록 개수를 초과하는지 확인
        if (member.getRegistrationCount() >= MAX_REGISTRATION_COUNT) {
            throw new BaseException(BaseResponseCode.REGISTRATION_LIMIT_EXCEEDED);
        }

        Optional<PlaceRegister> register = placeRegisterRepository.findByMemberAndPlace(member, place);
        if (register.isEmpty()) {
            // 등록되지 않은 경우 등록 진행
            PlaceRegister newRegistration = PlaceRegister.createRegister(member, place);
            placeRegisterRepository.save(newRegistration);
            place.increaseRegisterCount();
            member.increaseRegistrationCount(); // 등록 개수 증가
            return true; // 등록 동작
        }
        placeRegisterRepository.delete(register.get());
        place.decreaseRegisterCount();
        member.decreaseRegistrationCount(); // 등록 개수 감소
        return false; // 등록 취소 동작
    }


}
