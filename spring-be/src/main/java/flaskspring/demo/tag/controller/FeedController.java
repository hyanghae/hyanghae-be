package flaskspring.demo.tag.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResImageStandardScore;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.place.dto.res.ResPlaceWithSim;
import flaskspring.demo.tag.service.FeedService;
import flaskspring.demo.utils.FlaskService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "여행지 피드 Test 용", description = "사용하지 마시오")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
@Slf4j
public class FeedController {


    private final FeedService feedService;


    @Operation(summary = "유저 태그 기반 비인기 여행지 피드", description = "저장된 태그 기반 비인기 여행지 피드" +
            "<br> sort : \"register\"(등록순), \"like\"(좋아요순), \"alpha\"(가나다순), \"recommend(default)(추천순)\"  ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("/tag")
    public ResponseEntity<BaseResponse<BaseObject<ResPlace>>> FeedGet(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                      @RequestParam(name = "sort", required = false, defaultValue = "recommend") String sort) {
        Long memberId = memberDetails.getMemberId();
        List<ResPlace> recommendFeed = feedService.getRecommendFeed(memberId, sort);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(recommendFeed)));
    }



}
