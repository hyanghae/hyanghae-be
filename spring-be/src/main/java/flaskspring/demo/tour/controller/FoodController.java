package flaskspring.demo.tour.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponse;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ResFamous;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.service.PlaceService;
import flaskspring.demo.tour.dto.ResFood;
import flaskspring.demo.tour.service.TourApiService;
import flaskspring.demo.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FoodController {


    private final PlaceService placeService;
    private final TourApiService tourApiService;

    @Operation(summary = "여행지 소개", description = "여행지 소개 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MessageUtils.SUCCESS),
            @ApiResponse(responseCode = "400", description = MessageUtils.BAD_REQUEST,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = MessageUtils.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = BaseExceptionResponse.class))),
    })
    @GetMapping("/food/{placeId}")
    public ResponseEntity<BaseResponse<List<ResFood>>> getNearbyFoodPlaces(@PathVariable("placeId") Long placeId) throws URISyntaxException, JsonProcessingException {
        // placeId로 Place 객체 조회 (service 등에서 구현)
        Place place = placeService.findPlaceById(placeId);
        double mapX = place.getLocation().getMapX();
        double mapY = place.getLocation().getMapY();

        // RestTemplate 사용해 API 호출
        // 외부 API 호출 및 음식점 정보 조회
        List<ResFood> foodPlaces = tourApiService.getNearbyFoods(mapX, mapY);

        // 거리순 상위 3개의 음식점을 필터링하고, ResFood 객체로 변환
        List<ResFood> resFoods = foodPlaces.stream()
                .sorted(Comparator.comparingDouble(ResFood::getDist))  // 거리순 정렬
                .limit(3)  // 상위 3개 선택
                .map(fp -> new ResFood(fp.getTitle(), fp.getAddress(), fp.getPhone(), fp.getDist(), fp.getImageUrl(), fp.getMapX(), fp.getMapY()))
                .collect(Collectors.toList());

        // 반환
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseCode.OK, resFoods));
    }
}
