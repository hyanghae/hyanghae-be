package flaskspring.demo.travel.dto.res;

import flaskspring.demo.travel.domain.NotFamousPlace;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResRecommend {

    List<ResNotFamousPlace> notFamousPlaces;

    public ResRecommend(List<NotFamousPlace> notFamousPlaces) {
        // NotFamousPlace 객체들을 ResNotFamousPlace DTO 객체로 변환하여 리스트에 추가
        this.notFamousPlaces = notFamousPlaces.stream()
                .map(ResNotFamousPlace::new)
                .collect(Collectors.toList());
    }
}
