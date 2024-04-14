package flaskspring.demo.travel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


@Entity
@Getter

public class FamousPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String majorCategory;
    private String region;
    private String roadAddress;
    private String subCategory;
    private String touristSpotName;

    private int hashTagCount;
    private int searchCount;

    // 생성자, getter 및 setter 메서드 생략
}
