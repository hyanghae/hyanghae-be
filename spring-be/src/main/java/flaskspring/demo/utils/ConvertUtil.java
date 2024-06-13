package flaskspring.demo.utils;

import com.querydsl.core.Tuple;
import flaskspring.demo.home.dto.res.ResPlaceBrief;

import java.util.List;
import java.util.stream.Collectors;

public class ConvertUtil {

    public static List<ResPlaceBrief> convertToPlaceBriefList(List<Tuple> places) {
        return places.stream()
                .map(ResPlaceBrief::new)
                .toList();
    }

    public static List<ResPlaceBrief> convertToPlaceBriefList2(List<jakarta.persistence.Tuple> places) {
         return places.stream()
                .map(ResPlaceBrief::new)
                .toList();
    }
}
