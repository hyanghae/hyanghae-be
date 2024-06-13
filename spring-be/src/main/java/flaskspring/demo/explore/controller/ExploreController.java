package flaskspring.demo.explore.controller;

import com.querydsl.core.Tuple;
import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
import flaskspring.demo.explore.service.ExploreService;
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
import java.util.Optional;


@Tag(name = "여행지 탐색", description = "취향 설정 정보로 탐색하는 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/explore")
@Slf4j
public class ExploreController {

    private final ExploreService exploreService;
    private final MemberService memberService;



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "탐색 여행지 목록", description = "탐색 하단 : 태그, 이미지 기반 추천 여행지 목록 API" +
            "<br> 처음 요청시: 커서 null, 다음 요청시 커서 이전 리턴값" +
            "<br> sort : 'recommend'(추천순)  idCursor  -> 취향 설정 없을 경우 400에러" +
            "<br> sort : 'alpha'(가나다순) nameCursor" +
            "<br> sort : 'popular'(저장수 순) countCursor, idCursor" +
            "<br> " +
            "<br> city : " +
            "<br> 'SEOUL', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU', 'DAEJEON', 'ULSAN', 'GYEONGGI', 'GANGWON', 'CHUNGBUK', 'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU'")
    @GetMapping("")
    public ResponseEntity<BaseResponse<ResPlaceRecommendPaging>> exploreRecommendGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(required = false, defaultValue = "recommend", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "ALL", name = "city") String cityFilter,
            @RequestParam(required = false, name = "countCursor") Long countCursor,
            @RequestParam(required = false, name = "nameCursor") String nameCursor,
            @RequestParam(required = false, name = "idCursor") Long idCursor,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size
    ) {
        log.info("GET /api/explore");
        log.info("sort: {}, cityFilter: {}, countCursor: {}, nameCursor: {}, idCursor: {}", sort, cityFilter, countCursor, nameCursor, idCursor);

        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);

        // 디폴트 값 또는 "all"일 경우 필터를 적용하지 않음
        ExploreFilter filter = new ExploreFilter(sort, CityCode.fromCityName(cityFilter));
        ExploreCursor cursor = new ExploreCursor(countCursor, nameCursor, idCursor);
        ResPlaceRecommendPaging response = exploreService.getExplorePlace(member, filter, cursor, size);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, response));
    }

    /**
     * 태그, 이미지
     * 지역, 정렬
     */
}
