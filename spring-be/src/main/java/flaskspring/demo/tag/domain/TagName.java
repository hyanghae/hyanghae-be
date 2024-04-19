package flaskspring.demo.tag.domain;

import lombok.Getter;

@Getter
public enum TagName {
    액티비티("액티비티"),
    체험시설("체험시설"),
    산책("산책"),
    등산("등산"),
    숙박시설("숙박시설"),
    해수욕장("해수욕장"),
    계곡("계곡"),
    공원("공원"),
    공연("공연"),
    거리("거리"),
    마을("마을"),
    예술("예술"),
    문화유적("문화유적"),
    박물관("박물관"),
    자연유산("자연유산"),
    종교유적("종교유적"),
    맛집("맛집"),
    카페("카페"),
    특산물("특산물"),
    야시장("야시장"),
    오션뷰("오션뷰"),
    도시뷰("도시뷰"),
    숲뷰("숲뷰"),
    전망대("전망대");

    private final String value;

    TagName(String value) {
        this.value = value;
    }
}
