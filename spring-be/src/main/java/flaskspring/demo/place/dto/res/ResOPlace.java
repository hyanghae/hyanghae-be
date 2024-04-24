package flaskspring.demo.place.dto.res;

import flaskspring.demo.place.domain.Place;
import lombok.Data;

@Data
public class ResOPlace {

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
    public ResOPlace(Place Place) {
        this.mapx = Place.getLocation().getMapX();
        this.mapy = Place.getLocation().getMapY();
        this.searchCount = Place.getSearchCount();
        this.city = Place.getCity();
        this.majorCategory = Place.getMajorCategory();
        this.region = Place.getRegion();
        this.roadAddress = Place.getRoadAddress();
        this.subCategory = Place.getSubCategory();
        this.touristSpotName = Place.getTouristSpotName();
    }
}
