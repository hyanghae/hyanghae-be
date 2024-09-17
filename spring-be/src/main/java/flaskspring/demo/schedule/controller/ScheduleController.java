package flaskspring.demo.schedule.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.departure.service.ScheduleService;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResUpcomingSchedule;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.schedule.dto.req.ReqSchedule;
import flaskspring.demo.schedule.dto.res.ResSchedule;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


@Tag(name = "스케쥴 기능", description = "스케쥴 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final MemberService memberService;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "홈화면 스케쥴 API", description = "홈화면 스케쥴 API" +
            "<br> 스케쥴 없을 경우 data : null 리턴")
    @GetMapping("/schedule/home")
    public ResponseEntity<BaseResponse<BaseObject<ResUpcomingSchedule>>> homeSchedule() {
        log.info("GET /api/schedule");

        ResUpcomingSchedule resUpcomingSchedule1 = ResUpcomingSchedule.builder()
                .scheduleId(1L)
                .scheduleName("강원 강릉")
                .DDay(3)
                .startDate(LocalDate.of(2024, 8, 22))
                .endDate(LocalDate.of(2024, 8, 24))
                .build();

        ResUpcomingSchedule resUpcomingSchedule2 = ResUpcomingSchedule.builder()
                .scheduleId(2L)
                .scheduleName("서울 강남")
                .DDay(3)
                .startDate(LocalDate.of(2024, 8, 24))
                .endDate(LocalDate.of(2024, 8, 30))
                .build();

        List<ResUpcomingSchedule> resUpcomingSchedule = List.of(resUpcomingSchedule1, resUpcomingSchedule2);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(resUpcomingSchedule)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "등록한 스케쥴 목록", description = "등록한 스케쥴 목록 API")
    @GetMapping("/schedule")
    public ResponseEntity<BaseResponse<BaseObject<ResSchedule>>> scheduleGet(@AuthenticationPrincipal MemberDetails memberDetails
    ) {
        log.info("GET /api/schedule");

        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);
        List<ResSchedule> schedule = scheduleService.getSchedule(member);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(schedule)));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "스케쥴 등록 API", description = "스케쥴 등록 API" +
            "<br> dayCount랑 daySchedules 리스트 길이랑 같아야 합니다.")
    @PostMapping("/schedule")
    public ResponseEntity<BaseResponse<Object>> schedulePost(@AuthenticationPrincipal MemberDetails memberDetails,
                                                             @RequestBody ReqSchedule reqSchedule) {
        log.info("POST /api/schedule");

        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);

        scheduleService.saveSchedule(member, reqSchedule);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }

}
