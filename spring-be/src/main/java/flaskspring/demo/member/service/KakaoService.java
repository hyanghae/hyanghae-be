package flaskspring.demo.member.service;

import flaskspring.demo.config.auth.AuthConstant;
import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.config.jwt.auth.RefreshToken;
import flaskspring.demo.config.redis.RedisUtils;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.dto.Req.KakaoLoginRequestDto;
import flaskspring.demo.member.dto.Req.ReqKakaoAccessToken;
import flaskspring.demo.member.dto.Res.KakaoLoginResponseDto;
import flaskspring.demo.member.dto.Res.UserStatus;
import flaskspring.demo.member.dto.kakaoLoginDto.KakaoProfile;
import flaskspring.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    @Value("${security.jwt.token.refresh-expiration-minutes}")
    public long refreshExpirationMinutes;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    //   private final RefreshTokenRepository refreshTokenRepository;
   // private final RedisUtils redisUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    public KakaoLoginResponseDto kakaoLogin(ReqKakaoAccessToken reqKakaoAccessToken) {
        KakaoProfile kakaoProfile = getKakaoProfile(reqKakaoAccessToken.getAccessToken());
        log.info("kakaoProfile = {}", kakaoProfile);

        Member member = findOrRegisterMember(kakaoProfile);
        String token = jwtTokenProvider.createToken(member.getAccount());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getAccount());

        log.info("refreshToken : {}", refreshToken);
        updateRefreshToken(member, refreshToken);

        if (!member.isOnboarded()) {
            return new KakaoLoginResponseDto(token, refreshToken, UserStatus.ONBOARDING);
        }
        return new KakaoLoginResponseDto(token, refreshToken, UserStatus.LOGINED);
    } // 그냥 회원 가입 할 경우는 로그인을 따로 진행해야 토큰을 주고, 카카오 로그인을 할 경우 처음 등록시에도 토큰을 부여? -> yes

    private Member findOrRegisterMember(KakaoProfile kakaoProfile) {
        KakaoLoginRequestDto kakaoLoginRequestDto = new KakaoLoginRequestDto(kakaoProfile);
        return memberRepository.findByKakaoId(kakaoLoginRequestDto.getKakaoIdentifier())
                .orElseGet(() -> {
                    log.info("카카오로 회원가입");
                    Member newMember = memberRepository.save(kakaoLoginRequestDto.toEntity());
                    return newMember;
                });
    }

    private void updateRefreshToken(Member member, String refreshToken) {
        // 새로운 RefreshToken 객체를 생성하고 바로 저장
        RefreshToken refreshTokenObj = new RefreshToken(member.getAccount(), refreshToken);
        redisTemplate.opsForValue().set(member.getAccount(), refreshTokenObj, refreshExpirationMinutes, TimeUnit.MINUTES);
    }



    //(2)
    // 발급 받은 accessToken 으로 카카오 회원 정보 얻기
    public KakaoProfile getKakaoProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("accessToken = " + accessToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(httpHeaders); //헤더만 가지고 요청헤더를 만들 수 있다.
        KakaoProfile kakaoProfile = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest2, KakaoProfile.class).getBody();
        return kakaoProfile;
    }

    // (3) 회원 정보 DB 저장 후 JWT 를 생성
    /*public String SaveUserAndGetToken(String code) {
        OAuthToken accessToken = getAccessToken(code);
        KakaoProfile profile = getKakaoProfile(accessToken);

        Optional<Member> member = memberRepository.findByAccount(profile.getProperties().getNickname() + profile.getId());

        if (member.isPresent()) { //가입한 적이 있는 경우 토큰 발행
            return createToken(member.get());
        }
        // 가입한 적이 없는 경우 DB에 저장하고 토큰 발행
        Member newMember = Member.builder()
                .account(profile.getProperties().getNickname() + profile.getId())
                .password(password)
                .profileImageFileName(profile.getProperties().getProfile_image())
                .name(profile.getProperties().getNickname())
                .role(MemberRole.ROLE_MEMBER)
                .build();

        memberRepository.save(newMember);
        return createToken(newMember);
    }*/

    // (1)넘어온 인가 코드를 통해 access_token 발급
   /* public OAuthToken getAccessToken(String code) {
        //POST 방식으로 key=value 데이터를 요청
        //Post 요청을 하는 다양한 라이브러리가 있다 Retrofit(안드로이드), OkHttp, RestTempate
        RestTemplate restTemplate = new RestTemplate();


        //Haeder 오브젝트 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        log.info("before");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "adf170bad1bb693217a3ee4dc49ccf0c");
        params.add("redirect_uri", "http://localhost:3000/login/callback");
        params.add("code", code);
        //params.add("client_secret", client_secret);

        log.info("after");
        *//**
     * 토큰을 발급할때 좀 더 보안을 강화하기 위해 Client Secret을 사용할 수 있다.
     * Client Secret을 받는 위치는 내 애플리케이션 -> 제품 설정 -> 카카오로그인 -> 보안 입니다.
     * Client Secret을 받고난후 밑에 활성화 상태를 사용함으로 변경해주어야한다.
     *//*

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담는다
        //HttpHeader와 HttpBody 담기기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders); //

        //Http요청하기 그리고 response 변수의 응답 받음.
        OAuthToken oAuthToken = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, OAuthToken.class).getBody();

        return oAuthToken;
    }*/

    //id email nickname 기반으로 암호화 하여 토큰을 생성
/*    private String createToken(Member member) {
        // Jwt 생성 후 헤더에 추가해서 보내줌
        String jwtToken = JWT.create() //
                .withSubject(member.getKakaoEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("nickname", member.getKakaoNickname())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return jwtToken;
    }*/
}
