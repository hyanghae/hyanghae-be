package flaskspring.demo.schedule.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResUpcomingSchedule;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "스케쥴 기능", description = "스케쥴 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ScheduleController {


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "홈화면 스케쥴 API", description = "홈화면 스케쥴 API")
    @GetMapping("/schedule")
    public ResponseEntity<BaseResponse<ResUpcomingSchedule>> homeSchedule() {
        log.info("GET /api/schedule");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResUpcomingSchedule()));
    }
}
