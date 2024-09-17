package flaskspring.demo.recommend.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.home.dto.res.ResRisingPlacePaging;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.service.FamousPlaceService;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
import flaskspring.demo.recommend.explore.service.ExploringService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@Tag(name = "여행지 추천", description = "유저 공통 여행지 추천 API")
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Slf4j
@RestController
public class RecommendController {

    private final RecommendService recommendService;
    private final FamousPlaceService famousPlaceService;
    private final MemberService memberService;
    private final ExploringService exploringService;

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
            @RequestParam(required = false, name = "countCursor") String countCursor,
            @RequestParam(required = false, name = "idCursor") String idCursor,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size
    ) {
        log.info("GET /api/recommend/rising");
        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);
        ExploreCursor exploreCursor = new ExploreCursor(countCursor, null, idCursor);
        ResRisingPlacePaging response = recommendService.getRisingPlaces(member, exploreCursor, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, response));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "유명 여행지", description = "중단 추천 : 유명 여행지 API" +
            " <br> city : " +
            " <br> 'SEOUL', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU', 'DAEJEON', 'ULSAN', 'GYEONGGI', 'GANGWON', 'CHUNGBUK', 'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU')")
    @GetMapping("/famous")
    public ResponseEntity<BaseResponse<BaseObject<ResFamous>>> famousPlacesGet(
            @RequestParam(required = false, defaultValue = "ALL", name = "city") String cityFilter) {

        log.info("GET /api/recommend/famous");
        ExploreFilter filter = new ExploreFilter(null, CityCode.fromCityParameterName(cityFilter));
        List<ResFamous> famousPlaces = null;
        if (filter.getCityFilter() == null) {
            famousPlaces = famousPlaceService.get24FamousPlaces(); //24개 여행지에 대해 정의해야함
        } else {
            famousPlaces = famousPlaceService.getFamousPlace(filter); // 지역 필터 적용
        }
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(famousPlaces)));
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
        ExploreFilter filter = new ExploreFilter(sort, CityCode.fromCityParameterName(cityFilter));
        ExploreCursor cursor = new ExploreCursor(countCursor, nameCursor, idCursor);
        ResPlaceRecommendPaging response = exploringService.getExplorePlace(member, filter, cursor, size);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, response));
    }

}
