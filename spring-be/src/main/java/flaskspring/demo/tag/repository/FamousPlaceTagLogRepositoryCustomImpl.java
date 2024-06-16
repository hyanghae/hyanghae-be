package flaskspring.demo.tag.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.place.domain.QFamousPlace;
import flaskspring.demo.tag.domain.QFamousPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FamousPlaceTagLogRepositoryCustomImpl implements FamousPlaceTagLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QFamousPlaceTagLog famousPlaceTagLog = QFamousPlaceTagLog.famousPlaceTagLog;
    QTag tag = QTag.tag;
    QFamousPlace famousPlace = QFamousPlace.famousPlace;

    @Override
    public Tuple getFamousPlaceTuple(Long famousPlaceId) {
        return jpaQueryFactory
                .select(
                        famousPlace,
                        Expressions.stringTemplate("group_concat({0} order by {0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0} order by {0})", tag.tagName).as("tagNames")
                )
                .from(famousPlaceTagLog)
                .join(famousPlaceTagLog.famousPlace, famousPlace)
                .join(famousPlaceTagLog.tag, tag)
                .where(famousPlace.id.eq(famousPlaceId))
                .groupBy(famousPlace.id)
                .fetchOne(); // 튜플 하나만 가져오므로 fetchOne()을 사용합니다.
    }



}
