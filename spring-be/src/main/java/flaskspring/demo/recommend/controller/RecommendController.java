package flaskspring.demo.recommend.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.*;
import flaskspring.demo.recommend.explore.service.ExploreService;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.service.FamousPlaceService;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
import flaskspring.demo.recommend.service.RecommendService;
import flaskspring.demo.utils.MessageUtils;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
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

import java.util.List;


@Tag(name = "여행지 추천", description = "유저 공통 여행지 추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Slf4j
public class RecommendController {

    private final RecommendService recommendService;
    private final FamousPlaceService famousPlaceService;
    private final MemberService memberService;
    private final ExploreService exploreService;

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
            @RequestParam(required = false, name = "countCursor") Long countCursor,
            @RequestParam(required = false, name = "idCursor") Long idCursor,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size
    ) {
        log.info("GET /api/recommend/rising");

        Long memberId = memberDetails.getMemberId();

        ResRisingPlacePaging response = recommendService.getRisingPlaces(memberId, countCursor, idCursor, size);
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

    @Operation(summary = "유명 여행지와 유사한 여행지들", description = "유명 여행지 태그 할당 점수와 유사도순" +
            " <br> city : " +
            " <br> 'SEOUL', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU', 'DAEJEON', 'ULSAN', 'GYEONGGI', 'GANGWON', 'CHUNGBUK', 'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("/{famousPlaceId}/similar")
    public ResponseEntity<BaseResponse<ResPlaceRecommendPaging>> similarPlacesGet(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                                  @PathVariable("famousPlaceId") Long famousPlaceId,
                                                                                  @RequestParam(required = false, defaultValue = "ALL", name = "city") String cityFilter,
                                                                                  @RequestParam(required = false, defaultValue = "1", name = "cursor") Long cursor,
                                                                                  @RequestParam(required = false, defaultValue = "10", name = "size") int size) {
        log.info("GET /api/recommend/{famousPlaceId}/similar");

        if (cursor != null && cursor <= 0) {
            throw new BaseException(BaseResponseCode.INVALID_CURSOR);
        }
        Long memberId = memberDetails.getMemberId();

        ExploreFilter filter = new ExploreFilter(null, CityCode.fromCityName(cityFilter));

        ResPlaceRecommendPaging similarPlaces = famousPlaceService.getSimilarPlaces(memberId, filter, famousPlaceId, cursor, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, similarPlaces));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "설정 취향 기반 여행지 탐색", description = "태그, 이미지 기반 추천 여행지 목록 API" +
            "<br> 처음 요청시: 커서 null, 다음 요청시 커서 이전 리턴값" +
            "<br> sort : 'recommend'(추천순)  idCursor  -> 취향 설정 없을 경우 400에러" +
            "<br> sort : 'alpha'(가나다순) nameCursor" +
            "<br> sort : 'popular'(저장수 순) countCursor, idCursor" +
            "<br> " +
            "<br> city : " +
            "<br> 'SEOUL', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU', 'DAEJEON', 'ULSAN', 'GYEONGGI', 'GANGWON', 'CHUNGBUK', 'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU'")
    @GetMapping("/explore")
    public ResponseEntity<BaseResponse<ResPlaceRecommendPaging>> exploreRecommendGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(required = false, defaultValue = "recommend", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "ALL", name = "city") String cityFilter,
            @RequestParam(required = false, name = "countCursor") String countCursor,
            @RequestParam(required = false, name = "nameCursor") String nameCursor,
            @RequestParam(required = false, name = "idCursor") String idCursor,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size
    ) {
        log.info("GET /api/recommend/explore");
        log.info("sort: {}, cityFilter: {}, countCursor: {}, nameCursor: {}, idCursor: {}", sort, cityFilter, countCursor, nameCursor, idCursor);

        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);

        // 디폴트 값 또는 "all"일 경우 필터를 적용하지 않음
        ExploreFilter filter = new ExploreFilter(sort, CityCode.fromCityName(cityFilter));
        ExploreCursor cursor = new ExploreCursor(countCursor, nameCursor, idCursor);
        ResPlaceRecommendPaging response = exploreService.getExplorePlace(member, filter, cursor, size);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, response));
    }

}
