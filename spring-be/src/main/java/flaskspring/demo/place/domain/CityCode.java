package flaskspring.demo.place.domain;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import lombok.Getter;

@Getter
public enum CityCode {
    SEOUL("서울특별시", "SEOUL"),
    BUSAN("부산광역시", "BUSAN"),
    DAEGU("대구광역시", "DAEGU"),
    INCHEON("인천광역시", "INCHEON"),
    GWANGJU("광주광역시", "GWANGJU"),
    DAEJEON("대전광역시", "DAEJEON"),
    ULSAN("울산광역시", "ULSAN"),
    GYEONGGI("경기도", "GYEONGGI"),
    GANGWON("강원특별자치도", "GANGWON"),
    CHUNGBUK("충청북도", "CHUNGBUK"),
    CHUNGNAM("충청남도", "CHUNGNAM"),
    JEONBUK("전북특별자치도", "JEONBUK"),
    JEONNAM("전라남도", "JEONNAM"),
    GYEONGBUK("경상북도", "GYEONGBUK"),
    GYEONGNAM("경상남도", "GYEONGNAM"),
    JEJU("제주특별자치도", "JEJU");

    private final String koreanName;
    private final String englishName;

    CityCode(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public static CityCode fromCityName(String cityName) {
        if ("ALL".equalsIgnoreCase(cityName)) {
            return null;
        }
        for (CityCode code : values()) {
            if (code.englishName.equalsIgnoreCase(cityName)) {
                return code;
            }
        }
        throw new BaseException(BaseResponseCode.INVALID_CITY_FILTER);
    }
}
