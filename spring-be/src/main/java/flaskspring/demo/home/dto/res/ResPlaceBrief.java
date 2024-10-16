package flaskspring.demo.home.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.tag.dto.res.ResTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ResPlaceBrief {


    @Schema(description = "여행지 ID", example = "1")
    private Long placeId;

    @Schema(description = "지역", example = "강원 강릉")
    private String region;

    @Schema(description = "여행지명", example = "강문해변")
    private String touristSpotName;

    @Schema(description = "여행지 이미지 URL", example = "https://example.com/image.jpg")
    private String placeImgUrl;

    @Schema(description = "태그들")
    private List<ResTag> tags;

    @Schema(description = "저장 여부", example = "false")
    private Boolean isSaved;

    @Schema(description = "x좌표", example = "127.123123")
    private double mapX;

    @Schema(description = "y좌표", example = "38.12313")
    private double mapY;

    @Schema(description = "이전 여행지와의 거리", example = "12.5")
    private double distFromPrev;

    public ResPlaceBrief(Tuple tuple) {
        Place place = tuple.get(0, Place.class);
        String tagIdsString = tuple.get(1, String.class);
        String tagNamesString = tuple.get(2, String.class); // 두 번째 항목인 String을 가져옵니다.
        Boolean isRegistered = tuple.get(3, Boolean.class); // Boolean으로 가져옵니다.

        if (place == null) {
            throw new BaseException(BaseResponseCode.DATABASE_ERROR);
        }

        this.placeId = place.getId();
        this.region = createRegionString(place);
        this.touristSpotName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.mapX = place.getLocation().getMapX();
        this.mapY = place.getLocation().getMapY();
        this.tags = createResTags(tagIdsString, tagNamesString);
        this.isSaved = isRegistered;
    }

    public ResPlaceBrief(jakarta.persistence.Tuple tuple) {
        Place place = tuple.get(0, Place.class);
        String tagIdsString = tuple.get(1, String.class);
        String tagNamesString = tuple.get(2, String.class); // 두 번째 항목인 String을 가져옵니다.
        Boolean isRegistered = tuple.get(3, Boolean.class); // Boolean으로 가져옵니다.

        if (place == null || tagIdsString == null || tagNamesString == null) {
            throw new BaseException(BaseResponseCode.DATABASE_ERROR);
        }

        this.placeId = place.getId();
        this.region = place.getCity() + " " + place.getRegion();
        this.touristSpotName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();

        this.tags = createResTags(tagIdsString, tagNamesString);
        this.isSaved = isRegistered;
    }


    private List<ResTag> createResTags(String tagIdsString, String tagNamesString) {
        // null 체크: 둘 중 하나가 null이면 빈 리스트 반환
        if (tagIdsString == null || tagNamesString == null) {
            return new ArrayList<>();
        }

        List<ResTag> resTags = new ArrayList<>();
        String[] tagIds = tagIdsString.split(",");
        String[] tagNames = tagNamesString.split(",");

        // tagIds와 tagNames의 길이가 동일한지 확인
        if (tagIds.length != tagNames.length) {
            throw new BaseException(BaseResponseCode.TAG_MAPPING_ERROR);
        }

        for (int i = 0; i < tagIds.length; i++) {
            Long tagId = Long.parseLong(tagIds[i]);
            String tagName = tagNames[i];
            resTags.add(new ResTag(tagId, tagName));
        }

        return resTags;
    }


    private String createRegionString(Place place) {
        String city = place.getCity();
        String region = place.getRegion();

        // 줄임말로 변환된 도시 이름 얻기
        String shortCityName = getCityNameWithoutSpecialOrMetropolitan(city);
        shortCityName = getShortenedProvinceName(shortCityName);

        // 시/군/구 제거된 지역 이름 얻기
        String shortRegionName = getShortenedRegionName(region);

        // 지역 문자열 생성
        return shortCityName + " " + shortRegionName;
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

    // 지역 이름에서 "시", "군", "구"를 제거하는 메서드
    private String getShortenedRegionName(String region) {
        if (region.endsWith("시") || region.endsWith("군") || region.endsWith("구")) {
            return region.substring(0, region.length() - 1); // "시", "군", "구" 제거
        }
        return region;
    }

}
