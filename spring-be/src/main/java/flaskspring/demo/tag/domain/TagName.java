package flaskspring.demo.tag.domain;

import lombok.Getter;

@Getter
public enum TagName {
    Activity("액티비티"),
    ExperienceFacility("체험시설"),
    Walking("산책"),
    Hiking("등산"),
    AccommodationFacility("숙박시설"),
    Beach("해수욕장"),
    Valley("계곡"),
    Park("공원"),
    Performance("공연"),
    Street("거리"),
    Village("마을"),
    Art("예술"),
    CulturalHeritage("문화유적"),
    Museum("박물관"),
    NaturalHeritage("자연유산"),
    ReligiousHeritage("종교유적"),
    Restaurant("맛집"),
    Cafe("카페"),
    LocalSpecialty("특산물"),
    NightMarket("야시장"),
    OceanView("오션뷰"),
    CityView("도시뷰"),
    ForestView("숲뷰"),
    Observatory("전망대");

    private final String name;

    TagName(String name) {
        this.name = name;
    }

}
