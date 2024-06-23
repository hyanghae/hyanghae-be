package flaskspring.demo.place.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.home.dto.res.ResPlaceBrief;
import flaskspring.demo.place.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ResPlaceDetail {

    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;

    @Schema(description = "지역 포함 여행지명", example = "하동 노량대교")
    private String touristSpotNameWithRegion;

    @Schema(description = "이미지 url 리스트")
    private List<String> placeImgUrlList;

    @Schema(description = "저장 여부", example = "false")
    private Boolean isSaved;

    @Schema(description = "출발지 x좌표", example = "127.619842753323")
    double mapX;

    @Schema(description = "출발지 y좌표", example = "37.2125169562113")
    double mapY;

    @Schema(description = "출발지 도로명 주소", example = "00시 00면 00도로")
    String roadAddress;

    @Schema(description = "주변 여행지")
    private List<ResPlaceBrief> nearbyPlaces;


    public ResPlaceDetail(Tuple tuple, List<ResPlaceBrief> nearbyPlaces) {
        Place place = tuple.get(0, Place.class);
        String tagIdsString = tuple.get(1, String.class);
        String tagNamesString = tuple.get(2, String.class); // 두 번째 항목인 String을 가져옵니다.
        Boolean isRegistered = tuple.get(3, Boolean.class); // Boolean으로 가져옵니다.


        this.placeId = place.getId();
        this.touristSpotNameWithRegion = createTouristSpotNameWithRegion(place);
        this.placeImgUrlList = List.of(place.getImagePath());
        this.isSaved = isRegistered;
        this.mapX = place.getLocation().getMapX();
        this.mapY = place.getLocation().getMapY();
        this.roadAddress = place.getRoadAddress();
        this.nearbyPlaces = nearbyPlaces;
    }

    private String createTouristSpotNameWithRegion(Place place) {
        String city = place.getCity();
        String touristSpotName = place.getTouristSpotName();

        String cityName = getCityNameWithoutSpecialOrMetropolitan(city);
        cityName = getShortenedProvinceName(cityName);

        if (isSpecialOrMetropolitanCity(city)) {
            String region = place.getRegion();
            return cityName + " " + region + " " + touristSpotName;
        } else {
            return cityName + " " + touristSpotName;
        }
    }

    // 특별시 또는 광역시를 체크하는 메서드
    private boolean isSpecialOrMetropolitanCity(String city) {
        // 대한민국 특별시 및 광역시 목록
        String[] specialOrMetropolitanCities = {"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시"};
        for (String specialOrMetropolitanCity : specialOrMetropolitanCities) {
            if (specialOrMetropolitanCity.equals(city)) {
                return true;
            }
        }
        return false;
    }

    // 특별시 및 광역시에서 "특별시" 또는 "광역시"를 제거하는 메서드
    private String getCityNameWithoutSpecialOrMetropolitan(String city) {
        if (city.endsWith("특별시")) {
            return city.substring(0, city.length() - 3); // "특별시" 제거
        } else if (city.endsWith("광역시")) {
            return city.substring(0, city.length() - 3); // "광역시" 제거
        }
        return city;
    }

    // 행정구역을 줄임말로 변환하는 메서드
    private String getShortenedProvinceName(String city) {
        switch (city) {
            case "경상북도":
                return "경북";
            case "경상남도":
                return "경남";
            case "전북특별자치도":
                return "전북";
            case "전라남도":
                return "전남";
            case "충청북도":
                return "충북";
            case "충청남도":
                return "충남";
            case "경기도":
                return "경기";
            case "강원특별자치도":
                return "강원";
            case "제주특별자치도":
                return "제주";
            case "세종특별자치시":
                return "세종";
            default:
                return city;
        }
    }

}