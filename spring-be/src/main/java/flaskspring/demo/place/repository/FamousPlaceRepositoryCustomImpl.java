package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.domain.QFamousPlace;
import flaskspring.demo.tag.domain.QFamousPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.utils.filter.ExploreFilter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FamousPlaceRepositoryCustomImpl implements FamousPlaceRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;

    QFamousPlaceTagLog famousPlaceTagLog = QFamousPlaceTagLog.famousPlaceTagLog;
    QFamousPlace famousPlace = QFamousPlace.famousPlace;
    QTag tag = QTag.tag;


    public List<FamousPlace> findSimilarFamousPlaces(ExploreFilter filter, List<Long> tagIds) {

        JPAQuery<FamousPlace> query = jpaQueryFactory
                .select(
                        famousPlace
                )
                .from(famousPlace)
                .join(famousPlaceTagLog).on(famousPlaceTagLog.famousPlace.eq(famousPlace).and(famousPlaceTagLog.tagScore.ne(0))) // tagScore가 0이 아닌 경우 필터링
                .where(famousPlaceTagLog.tag.id.in(tagIds));

        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            query.where(famousPlace.city.eq(filter.getCityFilter()));
        }

        return query
                .groupBy(famousPlace.id) // 그루핑
                .fetch();
    }


}
