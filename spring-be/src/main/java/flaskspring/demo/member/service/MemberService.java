package flaskspring.demo.member.service;

import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.config.jwt.auth.RefreshToken;
import flaskspring.demo.config.jwt.auth.RefreshTokenRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralLoginReq;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralLoginRes;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.Res.GeneralSignUpRes;
import flaskspring.demo.member.dto.Res.UserStatus;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.place.repository.MemberTagLogRepository;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final Random random = new Random();

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
   // private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final MemberTagLogRepository memberTagLogRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public GeneralLoginRes generalLogin(GeneralLoginReq loginReq) {
        Member member = getMemberByAccount(loginReq.getAccount());
        validatePassword(loginReq.getPassword(), member.getPassword());

        String token = jwtTokenProvider.createToken(member.getAccount());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getAccount());
        log.info("refreshToken : {}", refreshToken);

        updateRefreshToken(member, refreshToken);

        if (!member.isOnboarded()) {
            return new GeneralLoginRes(token, refreshToken, UserStatus.ONBOARDING);
        }
        return new GeneralLoginRes(token, refreshToken, UserStatus.LOGINED);
    }

    private Member getMemberByAccount(String account) {
        return memberRepository.findByAccount(account)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BaseException(BaseResponseCode.UNAUTHORIZED);
        }
    }

    private void updateRefreshToken(Member member, String refreshToken) {
        refreshTokenRepository.findById(member.getAccount())
                .ifPresentOrElse(
                        it -> {
                            it.updateRefreshToken(refreshToken);
                            refreshTokenRepository.save(it); // 업데이트 후 명시적으로 저장, 이전 데이터는 덮어써짐
                        },
                        () -> refreshTokenRepository.save(new RefreshToken(member.getAccount(), refreshToken))
                );
    }


    @Transactional
    public GeneralSignUpRes generalSignUp(GeneralSignUpReq signUpReq) {
        signUpReq.setPassword(bCryptPasswordEncoder.encode(signUpReq.getPassword()));
        if (memberRepository.existsByAccount(signUpReq.getAccount())) {
            throw new BaseException(BaseResponseCode.BAD_DUPLICATE);
        }

        String uniqueNickname = generateUniqueNickname();

        Member member = signUpReq.toEntity();
        member.setNickname(uniqueNickname);
        Member savedMember = memberRepository.save(member);

        return new GeneralSignUpRes(savedMember.getMemberId());
    }

    private String generateUniqueNickname() {
        String randomNumber;
        boolean nicknameDuplicate;
        do {
            randomNumber = Integer.toString((random.nextInt(900_000) + 100_000));
            nicknameDuplicate = memberRepository.existsByNickname("유저-" + randomNumber);
        } while (nicknameDuplicate);
        return "유저-" + randomNumber;
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
    }

    public List<Tag> getRegisteredTag(Member member) { //N+1 문제 대비 -> 페치 조인 적용해보기
        List<MemberTagLog> memberTagLogs = memberTagLogRepository.findByMember(member);
        return memberTagLogs.stream().map(MemberTagLog::getTag).toList();
    }

    public boolean isRefreshNeeded(Member member) {
        Member findMember = findMemberById(member.getMemberId());
        return findMember.isRefreshNeeded();
    }

}
