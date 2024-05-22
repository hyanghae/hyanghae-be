package flaskspring.demo.member.service;


import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.AccessToken;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.AccessTokenRepository;
import flaskspring.demo.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("reissueAccessToken 진입");
        try {
            String refreshToken = parseBearerToken(request, "Refresh-Token");
            if (refreshToken == null) {
                System.out.println("리프레시 토큰 없음");
                throw new Exception();
            }
            String oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            jwtTokenProvider.validateRefreshToken(refreshToken, oldAccessToken);
            String newAccessToken = jwtTokenProvider.recreateAccessToken(oldAccessToken);
            System.out.println("newAccessToken 발급 = " + newAccessToken);
//            Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
//            SecurityContextHolder.getContext().setAuthentication(auth);

            response.setHeader("New-Access-Token", newAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", "새 토큰 재발급 실패");
        }
    }

    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }
}