package flaskspring.demo.member.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatusService {

    private final MemberRepository memberRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final UploadImageRepository imageRepository;

    public void updateStatus(String nickname, String status) {
        if (status.equalsIgnoreCase("unsetting")) {
            updateStatusOnboarding(nickname);
            return;
        } else if (status.equalsIgnoreCase("setting")) {
            updateStatusLogined(nickname);
            return;
        }
        throw new BaseException(BaseResponseCode.BAD_REQUEST);
    }

    private void updateStatusOnboarding(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BAD_REQUEST));
        memberTagLogRepository.deleteByMember(member);
        imageRepository.deleteByMember(member);

        member.initStatus();
    }


    void updateStatusLogined(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BAD_REQUEST));
        member.onBoard();
    }
}
