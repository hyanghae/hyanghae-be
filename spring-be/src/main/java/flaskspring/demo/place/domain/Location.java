package flaskspring.demo.place.domain;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    String mapX;
    String mapY;




    // 위도와 경도를 사용하여 두 지점 간의 거리를 계산하는 메서드
    public double calculateDistance(String otherMapX, String otherMapY) {

        double startLat = Double.parseDouble(this.mapY);
        double startLon = Double.parseDouble(this.mapX);
        double endLat = Double.parseDouble(otherMapY);
        double endLon = Double.parseDouble(otherMapX);

        double dLat = Math.toRadians(endLat - startLat);
        double dLon = Math.toRadians(endLon - startLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }

    // 생성자, getter, setter 등 필요한 메서드 추가
//    public double getDistance(String mapX, String mapY) {
//        double delX = Double.parseDouble(this.mapX) - Double.parseDouble(mapX);
//        double delY = Double.parseDouble(this.mapY) - Double.parseDouble(mapY);
//        return  Math.sqrt(delX * delX + delY * delY);
//    }
}