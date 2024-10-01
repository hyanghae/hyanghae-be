package flaskspring.demo.place.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;


@Entity
@Getter
@ToString
public class FamousPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "famous_place_id")
    private Long id;

    private String city;
    private String majorCategory;
    private String region;
    private String roadAddress;
    private String subCategory;
    private String touristSpotName;
    @Column(name = "en_city_name")
    private String enCityName;

    private String imgUrl;

    private int hashTagCount;
    private int searchCount;

    // 생성자, getter 및 setter 메서드 생략
}
