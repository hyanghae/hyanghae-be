package flaskspring.demo.place.domain;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import lombok.Getter;

@Getter
public enum CityCode {
    SEOUL("서울특별시", "SEOUL", "서울"),
    BUSAN("부산광역시", "BUSAN","부산"),
    DAEGU("대구광역시", "DAEGU", "대구"),
    INCHEON("인천광역시", "INCHEON", "인천"),
    GWANGJU("광주광역시", "GWANGJU" , "광주"),
    DAEJEON("대전광역시", "DAEJEON", "대전"),
    ULSAN("울산광역시", "ULSAN", "울산"),
    GYEONGGI("경기도", "GYEONGGI", "경기"),
    GANGWON("강원특별자치도", "GANGWON", "강원"),
    CHUNGBUK("충청북도", "CHUNGBUK", "충북"),
    CHUNGNAM("충청남도", "CHUNGNAM", "충남"),
    JEONBUK("전북특별자치도", "JEONBUK", "전북"),
    JEONNAM("전라남도", "JEONNAM", "전남"),
    GYEONGBUK("경상북도", "GYEONGBUK", "경북"),
    GYEONGNAM("경상남도", "GYEONGNAM", "경남"),
    JEJU("제주특별자치도", "JEJU", "제주");

    private final String koreanName;
    private final String paramName;
    private final String shortenedName;

    CityCode(String koreanName, String englishName, String shortenedName) {
        this.koreanName = koreanName;
        this.paramName = englishName;
        this.shortenedName = shortenedName;
    }

    public static CityCode fromCityParameterName(String cityParamName) {
        if ("ALL".equalsIgnoreCase(cityParamName)) {
            return null;
        }
        for (CityCode code : values()) {
            if (code.paramName.equalsIgnoreCase(cityParamName)) {
                return code;
            }
        }
        throw new BaseException(BaseResponseCode.INVALID_CITY_FILTER);
    }
    public static CityCode fromCityKoreanName(String cityKorName) {
        for (CityCode code : values()) {
            if (code.koreanName.equalsIgnoreCase(cityKorName)) {
                return code;
            }
        }
        throw new BaseException(BaseResponseCode.INVALID_CITY_FILTER);
    }
}
