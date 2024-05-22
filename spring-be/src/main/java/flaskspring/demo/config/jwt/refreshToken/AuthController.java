package flaskspring.demo.config.jwt.refreshToken;

import flaskspring.demo.config.jwt.JwtTokenProvider;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.service.AuthService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PatchMapping("/reissue")
    public ResponseEntity<BaseResponse<Object>> reissue(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @Parameter(description = "Refresh token", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
                                                            @RequestHeader(value = "Refresh-Token", required = false) String refreshToken) {
        authService.reissueAccessToken(request, response);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "로그아웃", description = "로그아웃입니다. ")
    @PutMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        authService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}