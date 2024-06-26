package flaskspring.demo.member.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralLoginReq;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralLoginRes;
import flaskspring.demo.member.dto.GerneralLoginDto.GeneralSignUpReq;
import flaskspring.demo.member.dto.Res.GeneralSignUpRes;
import flaskspring.demo.member.dto.Res.UserStatus;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@Tag(name = "일반 로그인 Test Completed", description = "실제 사용하지 않지만 테스트용 토큰 얻기 위한 가입 및 로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class GeneralLoginController {

    private final MemberService memberService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "테스트용 일반 회원가입 Test completed", description = "account, password 기반 일반 회원가입입니다. <br>리턴 데이터는 회원번호입니다")
    @PostMapping("/signUp")
    public ResponseEntity<BaseResponse<GeneralSignUpRes>> signUp(@Parameter(name = "회원가입 위한 회원 정보들", required = true) @Valid @RequestBody GeneralSignUpReq signUpReq) {
        log.info("POST /api/member/signUp");
        BaseResponse<GeneralSignUpRes> response = new BaseResponse<>(BaseResponseCode.OK, memberService.generalSignUp(signUpReq));

        return ResponseEntity.ok(response);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "404", description = MessageUtils.NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "테스트용 일반 로그인 Test completed", description = "account, password 기반 일반 로그인입니다. " +
            "<br> ONBOARDING : 온보딩 안 한 경우" +
            "<br> LOGINED : 온보딩 하여 로그인 한 경우" +
            "<br> DEACTIVATED : 탈퇴")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<GeneralLoginRes>> login(@Parameter(name = "로그인 위한 회원 정보들", required = true) @RequestBody GeneralLoginReq loginRequestDto) {
        log.info("POST /api/member/login");
        BaseResponse<GeneralLoginRes> response = new BaseResponse<>(BaseResponseCode.OK, memberService.generalLogin(loginRequestDto));
        return ResponseEntity.ok(response);
    }


}
