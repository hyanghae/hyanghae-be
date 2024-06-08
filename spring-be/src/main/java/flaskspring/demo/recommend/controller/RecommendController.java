package flaskspring.demo.recommend.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
import flaskspring.demo.place.repository.FamousPlaceRepository;
import flaskspring.demo.place.service.FamousPlaceService;
import flaskspring.demo.recommend.service.RecommendService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "여행지 추천", description = "여행지 추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Slf4j
public class RecommendController {

    private final RecommendService recommendService;
    private final FamousPlaceService famousPlaceService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "뜨고 있는 여행지", description = "뜨고 있는 여행지 API" +
            "<br> 처음 요청시 countCursor: null idCursor: null" +
            "<br> 다음 요청시 countCursor: 이전 리턴값 idCursor: 이전 리턴값" +
            "<br> 로드 데이터 없는 경우 빈 리스트, countCursor: null idCursor: null 반환")
    @GetMapping("/rising")
    public ResponseEntity<BaseResponse<ResRisingPlacePaging>> homeRisingGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(required = false, name = "countCursor") Long count,
            @RequestParam(required = false, name = "idCursor") Long placeId,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size
    ) {
        log.info("GET /api/recommend/rising");

        Long memberId = memberDetails.getMemberId();

        ResRisingPlacePaging response = recommendService.getRisingPlaces(memberId, count, placeId, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, response));
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
        List<ResFamous> famousPlaces = famousPlaceService.get24FamousPlaces();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(famousPlaces)));
    }
}
