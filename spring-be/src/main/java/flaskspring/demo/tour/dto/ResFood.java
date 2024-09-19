package flaskspring.demo.tour.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResFood {

    private String title;       // 음식점 제목
    private String address;     // 주소
    private String phone;       // 전화번호
    private double dist;    // 거리

    private String imageUrl;    // 대표 이미지 URL
    private double mapX;    // 거리
    private double mapY;    // 거리
}
