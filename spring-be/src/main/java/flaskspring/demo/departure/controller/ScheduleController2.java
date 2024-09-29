package flaskspring.demo.departure.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.departure.dto.res.ResSchedule;
import flaskspring.demo.departure.service.PathService;
import flaskspring.demo.schedule.service.ScheduleService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Tag(name = "스케쥴 기능", description = "스케쥴 API")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController2 {

    private final ScheduleService scheduleService;
    private final PathService pathService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @Operation(summary = "출발지 등록", description = "출발지 등록 기능")
    @PostMapping("/departure")
    public ResponseEntity<BaseResponse<Object>> departureCreate(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                @RequestBody ReqDeparture reqDeparture) {

        Long myMemberId = memberDetails.getMemberId();
        scheduleService.saveDeparture(myMemberId, reqDeparture);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "등록된 출발지가 없습니다",
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "최근 등록한 출발지 조회", description = "등록한 출발지 없을 경우 400")
    @GetMapping("/departure")
    public ResponseEntity<BaseResponse<ResDeparture>> departureGet(@AuthenticationPrincipal MemberDetails memberDetails) {

        Long myMemberId = memberDetails.getMemberId();
        ResDeparture recentDeparture = scheduleService.getRecentDeparture(myMemberId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, recentDeparture));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "201", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @Operation(summary = "등록한 여행지 스케쥴 기능", description = "등록한 출발지 없을 경우 200 : 등록 최신순 정렬 " +
            "<br> 등록한 출발지 있을 경우 201 : 최단거리 정렬")
    @GetMapping("/path")
    public ResponseEntity<BaseResponse<ResSchedule>> scheduleGet(@AuthenticationPrincipal MemberDetails memberDetails) {
        Long myMemberId = memberDetails.getMemberId();
        return ResponseEntity.ok(pathService.getSchedule(myMemberId));
    }
}
