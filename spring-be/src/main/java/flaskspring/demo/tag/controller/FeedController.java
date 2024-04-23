package flaskspring.demo.tag.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.*;
import flaskspring.demo.recommend.dto.res.ImgRecommendationDto;
import flaskspring.demo.tag.service.FeedService;
import flaskspring.demo.travel.dto.res.ResPlace;
import flaskspring.demo.utils.FlaskConfig;
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

import static flaskspring.demo.utils.Constant.BASE_FLASK_URL;
import static flaskspring.demo.utils.Constant.LOCAL_BASE_FLASK_URL;


@Tag(name = "여행지 추천 피드", description = "여행지 피드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
@Slf4j
public class FeedController {

    private final FlaskConfig flaskConfig;
    private final FeedService feedService;
    RestTemplate restTemplate = new RestTemplate();


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
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<BaseObject<ResPlace>>> recommendByImage(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                               @RequestParam("photo") MultipartFile placeImage) {

        Long myMemberId = memberDetails.getMemberId();
        List<ImgRecommendationDto> resultFromFlask = getResultFromFlask(placeImage);

        List<ResPlace> recommendFeed = feedService.getRecommendFeed(myMemberId, resultFromFlask);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(recommendFeed)));
    }

    private List<ImgRecommendationDto> getResultFromFlask(MultipartFile placeImage) {
        try {
            byte[] imageBytes = convertImageToByteArray(placeImage);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = createRequestEntity(placeImage, imageBytes);
            log.info("이미지 요청 들어옴");
            ResponseEntity<List<ImgRecommendationDto>> response = sendImageToFlask(requestEntity);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to share image: {}", e.getMessage());
            throw new BaseException(BaseResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] convertImageToByteArray(MultipartFile placeImage) throws IOException {
        return placeImage.getBytes();
    }

    private HttpEntity<MultiValueMap<String, Object>> createRequestEntity(MultipartFile placeImage, byte[] imageBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource contentsAsResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return placeImage.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("photo", contentsAsResource);

        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<List<ImgRecommendationDto>> sendImageToFlask(HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        return restTemplate.exchange(

                flaskConfig.getBaseUrl() + "/img-recommends/upload-image",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<ImgRecommendationDto>>() {
                }
        );
    }


}
