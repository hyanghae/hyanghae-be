package flaskspring.demo.member.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseObject;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.image.service.UploadImageService;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.member.dto.Res.ResUploadedImage;
import flaskspring.demo.place.register.service.PlaceRegisterService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "회원 기능", description = "회원 개인 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final UploadImageService uploadImageService;
    private final PlaceRegisterService placeRegisterService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "회원이 업로드한 이미지 목록", description = "size에 따라 마이페이지 화면 활용" +
            "<br> 모두 불러오려면size 파라미터 생략")
    @GetMapping("/upload-image")
    public ResponseEntity<BaseResponse<BaseObject<ResUploadedImage>>> uploadImageGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(required = false, name = "size") Integer size) {
        log.info("GET /api/member/upload-image");

        Long memberId = memberDetails.getMemberId();
        Member myMember = memberService.findMemberById(memberId);
        List<ResUploadedImage> uploadedImage = uploadImageService.getLatestUploadedImage(myMember, size);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(uploadedImage)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "회원이 저장한 여행지 목록", description = "size에 따라 마이페이지 화면 활용, " +
            "<br> 모두 불러오려면size 파라미터 생략")
    @GetMapping("/saved")
    public ResponseEntity<BaseResponse<BaseObject<ResPlaceBrief>>> savedPlacesGet(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestParam(required = false, name = "size") Integer size) {
        log.info("GET /api/member/saved");

        Long memberId = memberDetails.getMemberId();
        Member myMember = memberService.findMemberById(memberId);
        List<ResPlaceBrief> savedPlaces = placeRegisterService.getLatestSavedPlaces(myMember, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new BaseObject<>(savedPlaces)));
    }
}
