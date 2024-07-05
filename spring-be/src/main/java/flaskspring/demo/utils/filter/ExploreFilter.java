package flaskspring.demo.utils.filter;

import flaskspring.demo.place.domain.CityCode;
import lombok.Getter;

@Getter
public class ExploreFilter {
    private String sort;
    private String cityFilter; // 서울특별시, 인천광역시 등 정식 명칭

    public ExploreFilter(String sort, CityCode cityCode) {
        this.sort = sort;
        if(cityCode != null){
        this.cityFilter = cityCode.getKoreanName();
        }
    }
}