package flaskspring.demo.recommend.dto.res;

import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.dto.res.ResOPlace;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResRecommend {

    List<ResOPlace> Places;

    public ResRecommend(List<Place> Places) {
        // Place 객체들을 ResOPlace DTO 객체로 변환하여 리스트에 추가
        this.Places = Places.stream()
                .map(ResOPlace::new)
                .collect(Collectors.toList());
    }
}
