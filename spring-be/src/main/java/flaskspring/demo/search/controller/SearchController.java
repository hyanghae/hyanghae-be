package flaskspring.demo.search.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.recommend.dto.res.ResPlaceRecommendPaging;
import flaskspring.demo.search.dto.res.ResPlaceSearchPaging;
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

@Tag(name = "여행지 검색", description = "여행지 검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Slf4j
public class SearchController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "여행지 검색", description = "여행지 검색 API" +
            " <br> sort : 확정 필요 ")
    @GetMapping("")
    public ResponseEntity<BaseResponse<ResPlaceSearchPaging>> searchPlaceGet(
            @RequestParam(required = false, defaultValue = "similar", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(required = false, defaultValue = "", name = "searchQuery") String searchQuery
    ) {
        log.info("GET /api/search");


        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResPlaceSearchPaging()));
    }
}