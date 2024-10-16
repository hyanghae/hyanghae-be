package flaskspring.demo.config.jwt;

import flaskspring.demo.config.redis.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

import static flaskspring.demo.config.auth.AuthConstant.TOKEN_EXPIRED_MESSAGE;
import static flaskspring.demo.config.auth.AuthConstant.TOKEN_NOT_VALID_MESSAGE;

/**
 * 만들어둔 jwt 패키지에 OncePerRequestFilter를 상속받는 유효성 체크용 필터를 만든다.
 * 해당 필터는 이름에서도 짐작 가능 하듯, 한번의 요청마다 한번씩 실행되는 필터이다. -> GenericFilterBean으로 변경
 * 프론트 측에서 요청 헤더에 토큰을 넣어 보내면 이 필터가 검증해 줄 것이다.
 */

//빈 주입을 하면 OncePerRequestFilter을 상속했더라도 SecurityConfig에서 한 번, 빈 주입에 의해 한 번 더 필터가 등록된다.
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
   // private final AccessTokenRepository accessTokenRepository;
    private final RedisUtils redisUtils;



    // Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter 진입");

        // header에서 JWT를 받아온다. //없을 수도 있다 없어도 접근 가능한 곳은 있으니.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        // token이 있을 경우에는 유효성 확인. token이 없는데 인증 필요한 api 접근시 예외 발생
        // 매 진입마다 토큰 검사가 이뤄진다. -> 토큰으로부터
        try {
            if (token != null) {
                if (doNotLogout(token)) { // 토큰이 블랙리스트에 없는 경우에만 인증 처리
                    jwtTokenProvider.validateToken(token); //여기서 만료, 유효성 예외 발생.
                    Authentication auth = jwtTokenProvider.getAuthentication(token);  // 인증 객체 생성
                    SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContext 에 Authentication 객체를 저장
                    log.info("인증 처리 함");//인증 객체를 통해 인증 처리
                }
            }
        } catch (ExpiredJwtException e) {
            //  log.info("토큰이 있지만 기간이 만료 됨, 리프레시 토큰을 통한 액세스 토큰 재발급");
            log.info("토큰이 있지만 기간이 만료 됨");


            servletRequest.setAttribute(JwtProperties.HEADER_STRING, TOKEN_EXPIRED_MESSAGE);
            //      reissueAccessToken((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, e);
        } catch (Exception e) {
            log.info("토큰이 있지만 유효하지 않음, 인증 필요한 api 접근시 예외 발생");
            servletRequest.setAttribute(JwtProperties.HEADER_STRING, TOKEN_NOT_VALID_MESSAGE);
        }

        filterChain.doFilter(servletRequest, servletResponse); //토큰이 없는 경우는 바로 다음 필터로 넘어감.
        //토큰 검증에서 예외가 발생하여도 인증이 필요 없는 경로로 요청한 경우 서블릿으로 진입가능하다. -> 정상적인 응답이 가능.
        //토큰 검증에서 예외가 발생하고 인증이 필요한 경로로 요청한 경우 서블릿 진입이 불가하고, -> 인증 예외 발생 ->  예외를 AuthenticationEntryPoint에서 다룬다.
    }

    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    @Deprecated
    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        System.out.println("reissueAccessToken 진입");
        try {
            String refreshToken = parseBearerToken(request, "Refresh-Token");
            if (refreshToken == null) {
                System.out.println("리프레시 토큰 없음");
                throw exception;
            }
            String oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            jwtTokenProvider.validateRefreshToken(refreshToken, oldAccessToken);
            String newAccessToken = jwtTokenProvider.recreateAccessToken(oldAccessToken);
            System.out.println("newAccessToken 발급 = " + newAccessToken);
            Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);


            response.setHeader("New-Access-Token", newAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", "새 토큰 재발급 실패");
        }
    }

    private boolean doNotLogout(String accessToken) {
        //블랙리스트에 해당 토큰이 없으면 로그아웃 하지 않은 토큰 -> 레디스 활용
        return !redisUtils.hasKeyBlackList(accessToken);
    }
}