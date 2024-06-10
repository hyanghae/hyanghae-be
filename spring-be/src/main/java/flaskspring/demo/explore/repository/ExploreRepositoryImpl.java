package flaskspring.demo.explore.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.like.domain.QPlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.domain.QPlace;
import flaskspring.demo.place.dto.res.ResPlace;
import flaskspring.demo.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ExploreRepositoryImpl implements ExploreRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceLike placeLike = QPlaceLike.placeLike;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;

    public List<Tuple> findExplorePlaceWithoutTags(Member member, ExploreFilter filter, ExploreCursor cursor, int size) {

        JPAQuery<Tuple> query = jpaQueryFactory.select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where();

        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            query.where(place.city.eq(filter.getCityFilter()));
        }

        applyCursor(cursor, query); //필터 적용
        applyFilter(filter, query); // 정렬

        return query
                .groupBy(place.id) // 중복 방지를 위해 그룹화
                .limit(size)
                .fetch();
    }

    @Override
    public List<Tuple> findExplorePlaceWithTags(Member member, List<Long> tagIds) {
        // 서브쿼리를 사용하여 특정 tagIds에 해당하는 tagScore의 합을 계산
        NumberExpression<Integer> totalTagScore = new CaseBuilder()
                .when(tag.id.in(tagIds))
                .then(placeTagLog.tagScore)
                .otherwise(0)
                .sum();

        return jpaQueryFactory
                .select(
                        place,
                        totalTagScore.as("totalTagScore") // 조건부 tagScore의 합계
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member))) // 여행지 등록 정보를 확인하기 위한 조인
                .groupBy(place.id)
                .fetch();
    }



    public List<Tuple> findExplorePlaceWithRecommend(Member member, List<String> placeNames) {
        List<Tuple> results = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"), // tagId를 모음
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered"),
                        placeTagLog.tagScore.sum().as("totalTagScore") // tagScore의 합계
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member))) // 여행지 등록 정보를 확인하기 위한 조인.
                .where(place.touristSpotName.in(placeNames)) // 제공된 placeNames를 기반으로
                .groupBy(place.id)
                .fetch();

        // 각 Tuple의 touristSpotName을 출력
        results.forEach(tuple -> System.out.println("touristSpotName: " + tuple.get(0, Place.class).getTouristSpotName()));

        // Map으로 변환하여 조회한 이름에 맞게 정렬
        Map<String, Tuple> resultMap = results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, Place.class).getTouristSpotName(),
                        tuple -> tuple,
                        (existing, replacement) -> existing // 중복된 경우 기존 값을 유지
                ));

        return placeNames.stream()
                .map(resultMap::get)
                .collect(Collectors.toList());
    }


    private void applyFilter(ExploreFilter filter, JPAQuery<Tuple> query) {
        if ("alpha".equalsIgnoreCase(filter.getSort())) {
            query.orderBy(place.touristSpotName.asc());
        } else if ("popular".equalsIgnoreCase(filter.getSort())) {
            query.orderBy(place.registerCount.desc());
        }
    }

    private void applyCursor(ExploreCursor cursor, JPAQuery<Tuple> query) {
        if (cursor.getCountCursor() != null) {
            query.where(place.registerCount.goe(cursor.getCountCursor()));
        }
        if (cursor.getIdCursor() != null) {
            query.where(place.id.gt(cursor.getIdCursor()));
        }
        if (cursor.getNameCursor() != null) {
            query.where(place.touristSpotName.gt(cursor.getNameCursor()));
        }
    }
}
