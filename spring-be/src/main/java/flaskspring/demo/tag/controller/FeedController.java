package flaskspring.demo.tag.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.*;
import flaskspring.demo.home.dto.res.ResImageStandardScore;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.place.dto.res.ResPlaceWithSim;
import flaskspring.demo.home.dto.res.ImgRecommendationDto;
import flaskspring.demo.tag.service.FeedService;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.utils.FlaskConfig;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Tag(name = "여행지 피드 Test 용", description = "사용하지 마시오")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
@Slf4j
public class FeedController {


    private final FeedService feedService;
    private final UploadImageService uploadImageService;
    private final FlaskService flaskService;


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


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "이미지 기반 피드", description = "이미지 유사도 기반 추천")
    @PostMapping(value = "/image")
    public ResponseEntity<BaseResponse<BaseObject<ResPlaceWithSim>>> recommendByImage(@AuthenticationPrincipal MemberDetails memberDetails) {

        Long myMemberId = memberDetails.getMemberId();

        MultipartFile settingImage = uploadImageService.getSettingImageFile(myMemberId);

        //멀티파트 변환 후 다시 보낼 필요는 없음

        List<ResImageStandardScore> resultFromFlask = flaskService.getResultFromFlask(settingImage);
        for (ResImageStandardScore recommendation : resultFromFlask) {
            System.out.println("Name: " + recommendation.getName() + ", Similarity: " + recommendation.getScore());
        }

        // List<ResPlaceWithSim> recommendFeed = feedService.getRecommendFeed(myMemberId, resultFromFlask);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(null)));
    }


}
