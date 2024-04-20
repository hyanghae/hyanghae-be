package flaskspring.demo.departure.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.service.DepartureService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@Tag(name = "출발지 기능", description = "출발지 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departure")
public class DepartureController {

    private final DepartureService departureService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))) ,

    })
    @Operation(summary = "출발지 등록", description = "출발지 등록 기능")
    @PostMapping("")
    public ResponseEntity<BaseResponse<Object>> departureCreate(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                 @RequestBody ReqDeparture reqDeparture) {

        Long myMemberId = memberDetails.getMemberId();
        departureService.saveDeparture(myMemberId, reqDeparture);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED),
            @ApiResponse(responseCode = "400", description ="등록된 출발지가 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "최근 등록한 출발지 조회", description = "등록한 출발지 없을 경우 400")
    @GetMapping("")
    public ResponseEntity<BaseResponse<ResDeparture>> departureGet(@AuthenticationPrincipal MemberDetails memberDetails) {

        Long myMemberId = memberDetails.getMemberId();
        ResDeparture recentDeparture = departureService.getRecentDeparture(myMemberId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, recentDeparture));
    }
}
