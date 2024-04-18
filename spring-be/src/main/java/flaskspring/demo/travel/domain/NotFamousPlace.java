package flaskspring.demo.travel.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class NotFamousPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_famous_place_id")
    private Long id;

    @Embedded
    private Location location; // 위도와 경도를 저장하는 double 타입의 Location 클래스 사용

    @Column(name = "search_count")
    private int searchCount;

    @Column(length = 50) // 예상되는 도시명의 최대 길이에 맞게 지정
    private String city;

    @Column(name = "major_category", length = 50) // 적절한 길이로 설정
    private String majorCategory;

    @Column(length = 50) // 예상되는 지역명의 최대 길이에 맞게 지정
    private String region;

    @Column(name = "road_address", length = 100) // 적절한 길이로 설정
    private String roadAddress;

    @Column(name = "sub_category", length = 50) // 적절한 길이로 설정
    private String subCategory;

    @Column(name = "tourist_spot_name", length = 100) // 적절한 길이로 설정
    private String touristSpotName;

    @Column(name = "image_path", length = 200) // 이미지 경로의 최대 길이에 맞게 지정
    private String imagePath;

    // 생성자, getter, setter 등 필요한 메서드 추가
}
