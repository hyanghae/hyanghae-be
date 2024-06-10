package flaskspring.demo.member.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.image.util.ImageUploadUtil;
import flaskspring.demo.recommend.dto.res.ResConfigInfo;
import flaskspring.demo.tag.dto.req.ReqTagIndexes;
import flaskspring.demo.tag.dto.res.ResRegisteredTag;
import flaskspring.demo.tag.service.TagService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;


@Tag(name = "취향 기능", description = "취향 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend/setting")
@Slf4j
public class SettingController {


    private final TagService tagService;

    private final UploadImageService uploadImageService;

    @Operation(summary = "온보딩 설정 완료", description = "온보딩 태그, 이미지 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<Object>> recommendSetting(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                 @RequestPart(value = "data", required = false) ReqTagIndexes tagRequest,
                                                                 @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("POST /api/recommend/setting");

        Long myMemberId = memberDetails.getMemberId();
        System.out.println("tagRequest = " + tagRequest);
        System.out.println("image = " + image);

        tagService.saveMemberTags(myMemberId, tagRequest.getTagIndexes()); //무조건 진입
        if (image != null) {
            uploadImageService.uploadImage(image, myMemberId);
        } else {
            uploadImageService.deleteSettingImage(myMemberId);
        }
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "여행지 취향 설정 정보", description = "여행지 취향 설정 정보 API" +
            "<br> 설정 정보 없을 경우: data : null 리턴" +
            "<br> 둘 중 하나가 없을 경우: configuredImageUrl : null 또는 configuredTags ; []  ")
    @GetMapping("")
    public ResponseEntity<BaseResponse<ResConfigInfo>> recommendSettingGet(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        log.info("GET /api/recommend/setting");

        Long memberId = memberDetails.getMemberId();
        String settingImageURL = uploadImageService.getSettingImageURL(memberId);
        List<ResRegisteredTag> registeredTag = tagService.getRegisteredTag(memberId);
        System.out.println("registeredTag = " + registeredTag);

        if (settingImageURL == null && registeredTag.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, null));
        }
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new ResConfigInfo(settingImageURL, registeredTag)));
    }
}
