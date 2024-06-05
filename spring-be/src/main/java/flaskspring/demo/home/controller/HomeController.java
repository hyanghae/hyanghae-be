package flaskspring.demo.home.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.*;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "홈화면 기능", description = "홈화면 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Slf4j
public class HomeController {

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
        log.info("GET /api/home/schedule");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResUpcomingSchedule()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "홈화면 상단 여행지 제안", description = "상단 추천 : 2개 추천 여행지 제안 API")
    @GetMapping("/suggestion")
    public ResponseEntity<BaseResponse<List<ResPlaceBrief>>> homeFamousGet() {
        log.info("GET /api/home/suggestion");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, null));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "홈화면 유명 여행지", description = "중단 추천 : 유명 여행지 API")
    @GetMapping("/famous")
    public ResponseEntity<BaseResponse<ResFamous>> homeSuggestionGet() {
        log.info("GET /api/home/famous");

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResFamous()));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "뜨고 있는 여행지", description = " 홈화면 하단 뜨고 있는 여행지 API")
    @GetMapping("/rising")
    public ResponseEntity<BaseResponse<ResRisingPlacePaging>> homeRisingGet(
            @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber
    ) {
        log.info("GET /api/home/rising");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResRisingPlacePaging()));
    }
}
