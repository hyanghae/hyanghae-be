package flaskspring.demo.tag.controller;

import com.querydsl.core.Tuple;
import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.tag.service.FeedService;
import flaskspring.demo.travel.dto.res.ResPlace;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "여행지 피드 목록", description = "여행지 피드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place/recommend")
public class FeedController {

    private final FeedService feedService;


    @Operation(summary = "비인기 여행지 피드", description = "비인기 여행지 피드" +
         "<br> sort : \"register\", \"like\", \"alpha\", \"recommend(default)\"  ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED),
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<ResPlace>>> FeedGet(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                @RequestParam(required = false, defaultValue = "recommend") String sort) {
        Long memberId = memberDetails.getMemberId();
        List<ResPlace> places = feedService.getRecommendFeed(memberId, sort);

        BaseResponse<List<ResPlace>> response = new BaseResponse<>(BaseResponseCode.OK, places);
        return ResponseEntity.ok(response);
    }

}
