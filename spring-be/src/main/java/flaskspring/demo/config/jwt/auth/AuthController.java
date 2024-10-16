package flaskspring.demo.config.jwt.auth;

import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.dto.Res.ResReIssue;
import flaskspring.demo.member.service.AuthService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Tag(name = "인증 기능", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "토큰 재발급", description = "Header " +
            "<br> Authorization : Bearer 만료토큰" +
            "<br> Refresh-Token : Bearer 리프레시토큰" +
            "<br> 재발급 요청 횟수 10회로 제한됨. 다시 로그인 한 경우 횟수 리셋")
    @PutMapping("/reissue")
    public ResponseEntity<BaseResponse<ResReIssue>> reissue(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @Parameter(description = "Refresh token", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
                                                            @RequestHeader(value = "Refresh-Token", required = false) String refreshToken) {
        log.info("PATCH /api/auth/reissue");

        ResReIssue resReIssue = authService.reissueAccessToken(request, response);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, resReIssue));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "로그아웃", description = "로그아웃입니다. ")
    @PutMapping("/logout")
    public ResponseEntity<BaseResponse<Object>> logout(HttpServletRequest request) {
        log.info("PUT /api/auth/logout");

        authService.logout(request);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }
}