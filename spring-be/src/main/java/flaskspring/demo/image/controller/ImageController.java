package flaskspring.demo.image.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.image.util.ImageUploadUtil;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Tag(name = "이미지 업로드 기능", description = "이미지 업로드 API")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final UploadImageService uploadImageService;

    @Operation(summary = "이미지 업로드", description = "이미지 업로드 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<Object>> imageUpload(@AuthenticationPrincipal MemberDetails memberDetails,
                                                            @RequestPart("image") MultipartFile placeImage) {
        Long myMemberId = memberDetails.getMemberId();
        uploadImageService.uploadImage(placeImage, myMemberId);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }
}
