package flaskspring.demo.home.dto.res;

import com.querydsl.core.Tuple;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.tag.dto.res.ResTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

    public ResPlaceBrief(Tuple tuple) {
        Place place = tuple.get(0, Place.class);
        String tagIdsString = tuple.get(1, String.class);
        String tagNamesString = tuple.get(2, String.class); // 두 번째 항목인 String을 가져옵니다.
        Boolean isRegistered = tuple.get(3, Boolean.class); // Boolean으로 가져옵니다.

        if (place == null || tagIdsString == null || tagNamesString == null) {
            throw new BaseException(BaseResponseCode.DATABASE_ERROR);
        }

        this.placeId = place.getId();
        this.region = createRegionString(place);
        this.touristSpotName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();

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
        List<ResTag> resTags = new ArrayList<>();
        String[] tagIds = tagIdsString.split(",");
        String[] tagNames = tagNamesString.split(",");

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
