package flaskspring.demo.myPage.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.myPage.dto.res.ResMyPageMain;
import flaskspring.demo.myPage.service.MyPageService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "마이페이지 기능", description = "마이페이지 API")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "마이페이지 메인", description = "좋아요한 여행지, 업로드한 사진")
    @GetMapping("/main")
    public ResponseEntity<BaseResponse<ResMyPageMain>> myPageMain(@AuthenticationPrincipal MemberDetails memberDetails) {
        Long myMemberId = memberDetails.getMemberId();
        ResMyPageMain myPageData = myPageService.getMyPageData(myMemberId);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, myPageData));
    }
}
