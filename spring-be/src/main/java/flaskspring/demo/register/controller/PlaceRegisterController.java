package flaskspring.demo.register.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.register.service.PlaceRegisterService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "여행지 등록 기능", description = "여행지 등록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/save")
public class PlaceRegisterController {

    private final PlaceRegisterService placeRegisterService;
    private final MemberService memberService;

    @Operation(summary = "여행지 등록", description = "여행지를 등록합니다" +
            "<br> 200 : 여행지 등록 취소" +
            "<br> 201 : 여행지 등록 성공" +
            "<br> 400 : 여행지 등록 개수 초과")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "200", description = "등록 해제"),
            @ApiResponse(responseCode = "400", description = "여행지 등록 개수 초과",
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @PostMapping("/{placeId}")
    public ResponseEntity<BaseResponse<Object>> register(@AuthenticationPrincipal MemberDetails memberDetails,
                                                         @PathVariable("placeId") Long placeId) {
        Long myMemberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(myMemberId);

        boolean isRegisterAction = placeRegisterService.registerPlace(member, placeId);

        // 등록 경우 : 201 CREATED, 등록 해제의 경우 : 200 OK
        BaseResponseCode baseResponseCode = isRegisterAction ? BaseResponseCode.OK_REGISTER : BaseResponseCode.OK_UNREGISTER;
        BaseResponse<Object> response = new BaseResponse<>(baseResponseCode, new HashMap<>());
        return ResponseEntity.ok(response);
    }
}
