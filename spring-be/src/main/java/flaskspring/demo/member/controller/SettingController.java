package flaskspring.demo.member.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.util.ImageUploadUtil;
import flaskspring.demo.tag.dto.req.ReqTagIndexes;
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


@Tag(name = "온보딩 기능", description = "온보딩 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend/setting")
@Slf4j
public class SettingController {


    private final TagService tagService;
    private final ImageUploadUtil imageUploadUtil;

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

        if (tagRequest != null && tagRequest.getTagIndexes() != null) {
            tagService.saveMemberTags(myMemberId, tagRequest.getTagIndexes());
        }
        if (image != null) {
            imageUploadUtil.uploadImage(image, myMemberId);
        }

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }
}
