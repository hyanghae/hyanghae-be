package flaskspring.demo.member.controller;

import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.member.dto.Res.UserStatus;
import flaskspring.demo.member.service.MemberService;
import flaskspring.demo.member.service.StatusService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "유저 상태 변경", description = "유저의 상태를 변경하는 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/status")
@Slf4j
public class StatusController {


    private final StatusService statusService;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.ERROR,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class)))
    })
    @Operation(summary = "유저 상태 변경", description = "유저 상태 설정 API" +
            "<br> nickname : 카카오 닉네임 (ex: 이원준) " +
            "<br> status : unsetting 입력 -> (설정 초기화)" +
            "<br> status : setting 입력 -> (설정완료. 태그, 이미지 선택 없음)")
    @PostMapping("")
    public ResponseEntity<BaseResponse<Object>> updateStatus(@RequestParam(name = "nickname") String nickname,
                                                             @RequestParam(name = "status") String status) {
        log.info("POST /api/status");

        statusService.updateStatus(nickname, status);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, new HashMap<>()));
    }

}
