package flaskspring.demo.search.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.*;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.dto.res.ResCity;
import flaskspring.demo.place.service.FamousPlaceService;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.search.dto.res.ResPlaceSearchPaging;
import flaskspring.demo.search.dto.res.ResSearchRankDto;
import flaskspring.demo.search.service.SearchService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Tag(name = "여행지 검색", description = "검색어로 검색하는 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SearchController {

    private final FamousPlaceService famousPlaceService;
    private final PlaceService placeService;
    private final MemberService memberService;
    private final SearchService searchService;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/search/rank")
    public ResponseEntity<BaseResponse<BaseObject<ResSearchRankDto>>> getPopularSearches() {
        List<ResSearchRankDto> topSearches = searchService.getPopularSearches();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(topSearches)));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<ResPlaceSearchPaging>> searchPlacesGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(required = false, defaultValue = "recommend", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "ALL", name = "city") String cityFilter,
            @RequestParam(required = false, defaultValue = "") String searchQuery,
            @RequestParam(required = false, defaultValue = "1", name = "countCursor") String countCursor,
            @RequestParam(required = false, defaultValue = "1", name = "idCursor") String idCursor,
            @RequestParam(required = false, defaultValue = "10", name = "size") int size) {

        log.info("GET /api/search - sort: {}, city: {}, query: {}, countCursor: {}, idCursor: {}, size: {}",
                sort, cityFilter, searchQuery, countCursor, idCursor, size);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            searchService.saveSearchQuery(searchQuery);
        }

        Long memberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(memberId);
        ExploreCursor cursor = new ExploreCursor(countCursor, null, idCursor);
        ExploreFilter filter = new ExploreFilter(sort, CityCode.fromCityParameterName(cityFilter));

        Optional<FamousPlace> famousPlace = famousPlaceService.getFamousPlace(searchQuery);
        //유명여행지 중 검색어와 완전 일치하는 곳이 있는 지 검색

        if (famousPlace.isPresent()) { // 검색어와 여행지명이 완전 일치하는 경우
            Long famousPlaceId = famousPlace.get().getId();
            //디폴트 카운트 커서 : 1
            ResPlaceSearchPaging similarPlaces = famousPlaceService.getSimilarPlaces(member, filter, famousPlaceId, Optional.ofNullable(cursor.getCountCursor()).orElse(1L), size);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, similarPlaces));
        }
        // 일반적인 검색 로직
        ResPlaceSearchPaging placeSearchPaging = placeService.getPlacesBySearching(member, searchQuery, cursor, filter, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, placeSearchPaging));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "지역 목록", description = "도 단위 목록")
    @GetMapping("/search/city")
    public ResponseEntity<BaseResponse<BaseObject<ResCity>>> allCityGet() {
        log.info("GET /api/search/city");

        CityCode[] values = CityCode.values();
        List<ResCity> resCities = Arrays.stream(values)
                .map(ResCity::new)
                .toList();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(resCities)));
    }



    //    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
//            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
//                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
//            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
//                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
//    })
//    @Operation(summary = "여행지 검색", description = "여행지 검색 API" +
//            " <br> sort : 확정 필요 ")
//    @GetMapping("/search")
//    public ResponseEntity<BaseResponse<ResPlaceSearchPaging>> searchPlaceGet(
//            @RequestParam(required = false, defaultValue = "", name = "searchQuery") String searchQuery,
//            @RequestParam(required = false, defaultValue = "recommend", name = "sort") String sort,
//            @RequestParam(required = false, name = "countCursor") Long countCursor,
//            @RequestParam(required = false, name = "idCursor") Long idCursor,
//            @RequestParam(required = false, name = "nameCursor") String name,
//            @RequestParam(required = false, defaultValue = "10", name = "size") int size
//    ) {
//        log.info("GET /api/search");
//
//
//        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResPlaceSearchPaging()));
//    }
}
