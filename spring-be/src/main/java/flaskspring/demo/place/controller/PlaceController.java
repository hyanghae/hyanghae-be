package flaskspring.demo.place.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.domain.CityCode;
import flaskspring.demo.place.dto.res.ResPlaceDetail;
import flaskspring.demo.place.dto.res.ResSimilarity;
import flaskspring.demo.place.register.service.PlaceRegisterService;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.utils.MessageUtils;
import flaskspring.demo.utils.filter.ExploreFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Tag(name = "여행지 기능", description = "여행지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
public class PlaceController {

    private final PlaceRegisterService placeRegisterService;
    private final MemberService memberService;
    private final PlaceService placeService;

    @Operation(summary = "여행지 등록", description = "여행지를 등록합니다" +
            "<br> 200 : 여행지 등록 취소" +
            "<br> 201 : 여행지 등록 성공" +
            "<br> 400 : 여행지 등록 개수 초과")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "200", description = "등록 해제"),
            @ApiResponse(responseCode = "400", description = "여행지 등록 개수 초과",
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @PostMapping("/save/{placeId}")
    public ResponseEntity<BaseResponse<Object>> register(@AuthenticationPrincipal MemberDetails memberDetails,
                                                         @PathVariable("placeId") Long placeId) {
        Long myMemberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(myMemberId);

        boolean isRegisterAction = placeRegisterService.registerPlace(member, placeId);

        // 등록 경우 : 201 CREATED, 등록 해제의 경우 : 200 OK
        BaseResponseCode baseResponseCode = isRegisterAction ? BaseResponseCode.OK_REGISTER : BaseResponseCode.OK_UNREGISTER;
        BaseResponse<Object> response = new BaseResponse<>(baseResponseCode, new HashMap<>());
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "여행지 소개", description = "여행지 소개 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("detail/{placeId}")
    public ResponseEntity<BaseResponse<ResPlaceDetail>> placeDetailGet(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                       @PathVariable("placeId") Long placeId) {
        Long myMemberId = memberDetails.getMemberId();
        Member member = memberService.findMemberById(myMemberId);

        ResPlaceDetail placeDetail = placeService.getPlaceDetail(member, placeId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, placeDetail));
    }


    @Operation(summary = "여행지와 유사한 유명여행지", description = "유사 유명여행지 api" +
            "<br> city :" +
            "<br> 'SEOUL', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU', 'DAEJEON', 'ULSAN', 'GYEONGGI', 'GANGWON', 'CHUNGBUK', 'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("detail/{placeId}/similar")
    public ResponseEntity<BaseResponse<BaseObject<ResFamous>>> SimilarFamousPlaceGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable("placeId") Long placeId,
            @RequestParam(required = false, defaultValue = "ALL", name = "city") String cityFilter
    ) {
        Long myMemberId = memberDetails.getMemberId();
        ExploreFilter filter = new ExploreFilter("alpha", CityCode.fromCityName(cityFilter));
        List<ResFamous> similarFamousPlace = placeService.getSimilarFamousPlace(filter, placeId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(similarFamousPlace)));
    }


    @Operation(summary = "여행지와 유사한 유명여행지 유사도", description = "유사 유명여행지 유사도 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("detail/{placeId}/similar/{famousPlaceId}")
    public ResponseEntity<BaseResponse<ResSimilarity>> SimilarityDetailGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable("placeId") Long placeId,
            @PathVariable("famousPlaceId") Long famousPlaceId
    ) {
        Long myMemberId = memberDetails.getMemberId();
        ResSimilarity similarity = placeService.getSimilarity(placeId, famousPlaceId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, similarity));
    }

}
