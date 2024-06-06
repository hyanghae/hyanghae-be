package flaskspring.demo.recommend.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
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


@Tag(name = "여행지 추천", description = "여행지 추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Slf4j
public class RecommendController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "뜨고 있는 여행지", description = "뜨고 있는 여행지 API")
    @GetMapping("/rising")
    public ResponseEntity<BaseResponse<ResRisingPlacePaging>> homeRisingGet(
            @RequestParam(required = false, name = "countCursor") Long count,
            @RequestParam(required = false, name = "idCursor") Long placeId,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size
    ) {
        log.info("GET /api/recommend/rising");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResRisingPlacePaging()));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "유명 여행지", description = "중단 추천 : 유명 여행지 API")
    @GetMapping("/famous")
    public ResponseEntity<BaseResponse<BaseObject<ResFamous>>> homeSuggestionGet() {
        log.info("GET /api/recommend/famous");

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(null)));
    }
}
