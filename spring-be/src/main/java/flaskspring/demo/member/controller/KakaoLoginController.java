package flaskspring.demo.member.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.dto.Req.ReqKakaoAccessToken;
import flaskspring.demo.member.dto.Res.KakaoLoginResponseDto;
import flaskspring.demo.member.service.KakaoService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Tag(name = "카카오 로그인", description = "카카오 로그인 API")

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    // 프론트에서 인가코드 받아오는 url
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "카카오 엑세스토큰 전송하는 곳", description = "카카오로부터 받은 코드 전송해주세요. 로그인 처리 후 jwt토큰, 온보딩 여부 반환. " +
            "<br> ONBOARDING : 온보딩 안 한 경우" +
            "<br> LOGINED : 온보딩 하여 로그인 한 경우" +
            "<br> DEACTIVATED : 탈퇴")
    @PostMapping("/auth/kakao")
    public BaseResponse<KakaoLoginResponseDto> getLogin(@RequestBody ReqKakaoAccessToken accessToken) { //(1)
        log.info("POST /api/auth/kakao");
        log.info("code = {}", accessToken);
        return new BaseResponse<>(BaseResponseCode.OK, kakaoService.kakaoLogin(accessToken));
    }
}
