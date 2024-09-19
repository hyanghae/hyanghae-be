package flaskspring.demo.remove.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.remove.service.RemoveService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RemoveController {

    private final RemoveService removeService;
    private final MemberService memberService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인한 사용자가 자신의 계정을 탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/remove")
    @Operation(summary = "회원 DB 제거 ", description = "데이터 베이스 영구삭제 (개발 전용)")
    public ResponseEntity<String> withdrewHH(@AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("EndPoint Delete /remove");
        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);
        removeService.remove(member);
        //데이터베이스 영구 삭제

        return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
    }

}
