package flaskspring.demo.config.hello;


import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.config.hello.dto.req.ReqAgreement;
import flaskspring.demo.config.hello.dto.res.ResHello;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "헬로 기능", description = "헬로 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HelloController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "헬로 API", description = "앱 시작 시 API")
    @PostMapping("/hello")
    public ResponseEntity<BaseResponse<Object>> recommendSettingGet(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        log.info("GET /api/hello");
        if(memberDetails != null) {
            Long myMemberId = memberDetails.getMemberId();
        }
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResHello()));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "약관 동의 여부", description = "동의 여부 API")
    @PostMapping("/agree")
    public ResponseEntity<BaseResponse<Object>> agreePost(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody ReqAgreement reqAgreement
    ) {
        log.info("POST /api/agree");
        Long myMemberId = memberDetails.getMemberId();

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }
}
