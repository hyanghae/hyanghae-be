package flaskspring.demo.like.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.like.service.PlaceLikeService;
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

@Tag(name = "여행지 좋아요 기능", description = "여행지 좋아요 API")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class PlaceLikeController {

    private final PlaceLikeService placeLikeService;

    @Operation(summary = "여행지 좋아요", description = "여행지 좋아요 누릅니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "좋아요 성공"),
            @ApiResponse(responseCode = "200", description = "좋아요 취소"),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @PostMapping("/{placeId}")
    public ResponseEntity<BaseResponse<Object>> like(@AuthenticationPrincipal MemberDetails memberDetails,
                                  @PathVariable Long placeId) {
        Long myMemberId = memberDetails.getMemberId();

        boolean isLikeAction = placeLikeService.likePlace(myMemberId, placeId);

        // 좋아요 경우 :  201 CREATED, 좋아요 해제의 경우 : 200 OK
        BaseResponseCode baseResponseCode = isLikeAction ? BaseResponseCode.OK_LIKE : BaseResponseCode.OK_UNLIKE;
        BaseResponse<Object> response = new BaseResponse<>(baseResponseCode, new HashMap<>());
        return ResponseEntity.ok(response);
    }
}
