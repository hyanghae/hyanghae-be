package flaskspring.demo.member.service;

import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.config.jwt.refreshToken.MemberRefreshToken;
import flaskspring.demo.config.jwt.refreshToken.MemberRefreshTokenRepository;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralLoginReq;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralLoginRes;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpRes;
import flaskspring.demo.member.dto.Res.UserStauts;
import flaskspring.demo.member.repository.MemberRepository;
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
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    @Transactional
    public GeneralLoginRes generalLogin(GeneralLoginReq loginReq) {
        Member member = memberRepository.findByAccount(loginReq.getAccount()).orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        if (!bCryptPasswordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
            throw new BaseException(BaseResponseCode.UNAUTHORIZED);
        }
        String token = jwtTokenProvider.createToken(member.getAccount());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getAccount());

        log.info("refreshToken : {}", refreshToken);

        memberRefreshTokenRepository.findById(member.getMemberId())
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        () -> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
                );
        if (!member.isOnboarded()) {
            return new GeneralLoginRes(token, refreshToken, UserStauts.NOT_ONBOARDED);
        }
        return new GeneralLoginRes(token, refreshToken, UserStauts.ONBOARDED);
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


}
