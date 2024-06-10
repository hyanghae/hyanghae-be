package flaskspring.demo.utils;

import com.querydsl.core.Tuple;
import flaskspring.demo.home.dto.res.ResPlaceBrief;

import java.util.List;

public class ConvertUtil {
    public static List<ResPlaceBrief> convertToPlaceBriefList(List<Tuple> places) {
        return places.stream()
                .map(ResPlaceBrief::new)
                .toList();
    }
}
