package flaskspring.demo.travel.domain;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    String mapX;
    String mapY;

    // 생성자, getter, setter 등 필요한 메서드 추가
}