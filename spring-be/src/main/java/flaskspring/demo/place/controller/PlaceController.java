package flaskspring.demo.place.controller;

import flaskspring.demo.config.auth.MemberDetails;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.place.dto.res.ResPlaceDetail;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.utils.FlaskConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;


//@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    RestTemplate restTemplate = new RestTemplate();
    private final FlaskConfig flaskConfig;
    private final PlaceService placeService;

    @GetMapping("detail/{placeId}")
    public ResponseEntity<BaseResponse<ResPlaceDetail>> placeDetail(@AuthenticationPrincipal MemberDetails memberDetails,
                                                    @PathVariable("placeId") Long placeId) {
        Long myMemberId = memberDetails.getMemberId();

        ResPlaceDetail placeDetail = placeService.getPlaceDetail(placeId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, placeDetail));
    }

}
