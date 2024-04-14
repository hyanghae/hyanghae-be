package flaskspring.demo.travel.dto.res;

import flaskspring.demo.travel.domain.NotFamousPlace;
import lombok.Data;

@Data
public class ResNotFamousPlace {

    private String mapx;
    private String mapy;
    private int searchCount;
    private String city;
    private String majorCategory;
    private String region;
    private String roadAddress;
    private String subCategory;
    private String touristSpotName;

    // 생성자, getter, setter 등 필요한 메서드 추가
    public ResNotFamousPlace(NotFamousPlace notFamousPlace) {
        this.mapx = notFamousPlace.getMapx();
        this.mapy = notFamousPlace.getMapy();
        this.searchCount = notFamousPlace.getSearchCount();
        this.city = notFamousPlace.getCity();
        this.majorCategory = notFamousPlace.getMajorCategory();
        this.region = notFamousPlace.getRegion();
        this.roadAddress = notFamousPlace.getRoadAddress();
        this.subCategory = notFamousPlace.getSubCategory();
        this.touristSpotName = notFamousPlace.getTouristSpotName();
    }
}
