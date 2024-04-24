package flaskspring.demo.tag.repository;

import com.querydsl.core.Tuple;

public interface FamousPlaceTagLogRepositoryCustom {

    Tuple getFamousPlaceTuple(Long famousPlaceId);
}
