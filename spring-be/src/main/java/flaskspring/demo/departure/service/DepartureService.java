package flaskspring.demo.departure.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import flaskspring.demo.departure.domain.DeparturePoint;
import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.repository.DepartureRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartureService {

    private final MemberRepository memberRepository;
    private final DepartureRepository departureRepository;

    public void saveDeparture(Long memberId, ReqDeparture reqDeparture) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        DeparturePoint departurePoint = DeparturePoint.create(reqDeparture, member);
        departureRepository.save(departurePoint);
    }

    public ResDeparture getRecentDeparture(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        DeparturePoint recentDeparture = departureRepository.findFirstByMemberOrderByCreatedTimeDesc(member)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_DEPARTURE_EXCEPTION));

        return new ResDeparture(recentDeparture);
    }
}
