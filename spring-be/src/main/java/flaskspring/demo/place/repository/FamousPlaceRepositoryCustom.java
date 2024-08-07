package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.utils.filter.ExploreFilter;

import java.util.List;

public interface FamousPlaceRepositoryCustom {

    List<FamousPlace> findSimilarFamousPlaces(ExploreFilter filter, List<Long> tagIds);
}
