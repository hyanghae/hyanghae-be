package flaskspring.demo.member.service;


import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.AccessToken;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.AccessTokenRepository;
import flaskspring.demo.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AccessTokenRepository accessTokenRepository;


    public void logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        String account = getAccountFromAccessToken(accessToken);
        Member member = findMemberByAccount(account);

        // AccessToken을 블랙리스트에 추가
        AccessToken blacklistedToken = AccessToken.builder()
                .token(accessToken)
                .member(member)
                .expirationDate(jwtTokenProvider.getAccessTokenExpirationDate(accessToken))
                .blacklisted(true)
                .build();
        accessTokenRepository.save(blacklistedToken);
    }

    private String getAccountFromAccessToken(String accessToken) {
        return jwtTokenProvider.getUserAccount(accessToken);
    }

    private Member findMemberByAccount(String account) {
        return memberRepository.findByAccount(account)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
    }
}