package flaskspring.demo.recommend.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.recommend.dto.res.ResConfigInfo;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
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
@RequestMapping("/api/explore")
@Slf4j
public class ExploreController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "여행지 추천 설정 정보", description = "추천 상단 여행지 설정 정보 API")
    @GetMapping("/config")
    public ResponseEntity<BaseResponse<ResConfigInfo>> exploreConfigGet() {
        log.info("GET /api/explore/config");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResConfigInfo()));
    }



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "추천 여행지 목록", description = "탐색 하단 태그, 이미지 기반 추천 여행지 목록 API" +
            " <br> sort : 'recommend'(추천순) 이외 확정 필요 ")
    @GetMapping("/recommend")
    public ResponseEntity<BaseResponse<ResPlaceRecommendPaging>> exploreRecommendGet(
            @RequestParam(required = false, defaultValue = "recommend", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber
    ) {
        log.info("GET /api/explore/recommend");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResPlaceRecommendPaging()));
    }
}
