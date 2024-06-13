package flaskspring.demo.place.controller;


import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.*;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.place.service.FamousPlaceService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/famous")
@RequiredArgsConstructor
@Slf4j
public class FamousController {

    private final MemberService memberService;
    private final FamousPlaceService famousPlaceService;

    @Operation(summary = "유명 여행지와 유사한 여행지들", description = "유명 여행지 태그 할당 점수와 유사도순")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("/{famousPlaceId}/similar")
    public ResponseEntity<BaseResponse<BaseObject<ResPlaceBrief>>> FeedGet(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                           @PathVariable("famousPlaceId") Long famousPlaceId,
                                                                           @RequestParam(required = false, defaultValue = "1", name = "cursor") Long cursor,
                                                                           @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        if (cursor != null && cursor <= 0) {
            throw new BaseException(BaseResponseCode.INVALID_CURSOR);
        }

        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);

        List<ResPlaceBrief> similarPlaces = famousPlaceService.getSimilarPlaces(member, famousPlaceId, cursor, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(similarPlaces)));
    }
}
