package flaskspring.demo.place.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.like.domain.QPlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.QPlace;
import flaskspring.demo.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.utils.filter.ExploreFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceLike placeLike = QPlaceLike.placeLike;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;

    @Override
    public List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member) {
        List<Tuple> tuples = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        Expressions.stringTemplate("count({0})", tag.tagName).as("sameTagCount"),
                        placeLike.place.isNotNull().as("isLiked"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeLike).on(placeLike.place.eq(place).and(placeLike.member.eq(member)))
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.touristSpotName.in(placeNames))
                .groupBy(place.id)
                .fetch();

        return tuples;
    }

    @Override
    public List<Tuple> findRisingPlaces(Member member, Long countCursor, Long placeId, int size) {

        // Null 체크 후 Optional로 감싸기
        Optional<Long> optionalCountCursor = Optional.ofNullable(countCursor);
        Optional<Long> optionalPlaceId = Optional.ofNullable(placeId);

        // Null일 경우 기본값 설정
        Long effectiveCountCursor = optionalCountCursor.orElse(0L);
        Long effectivePlaceId = optionalPlaceId.orElse(0L);

        return jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(
                        place.registerCount.goe(effectiveCountCursor),
                        place.id.gt(effectivePlaceId)
                )
                .groupBy(place.id) //그루핑
                .orderBy(place.registerCount.desc(), // 등록 순 내림차순
                        place.id.asc()) // id 순 오름차순
                .limit(size)
                .fetch();
    }

    @Override
    public List<Tuple> findSimilarPlaces(Member member, List<Long> placeIds) {
        return jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0))) // tagScore가 0이 아닌 경우 필터링
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(
                        place.id.in(placeIds)
                )
                .groupBy(place.id) // 그루핑
                .fetch();
    }


    public List<Tuple> findSimilarPlacesByKNN(Member member, Long countCursor, int[] queryPoint, int size) {
        if (queryPoint.length != 24) {
            throw new IllegalArgumentException("The length of queryPoint must be 24.");
        }

        // Create a template for the distance calculation expression
        String distanceCalculation = IntStream.range(0, 24)
                .mapToObj(i -> String.format(
                        "POW(CASE WHEN tag.id = %d THEN (placeTagLog.tagScore - %d) ELSE 0 END, 2)",
                        i + 1, queryPoint[i])
                )
                .collect(Collectors.joining(" + "));

        // Create the final distance expression
        String distanceExpression = String.format("SQRT(SUM(%s))", distanceCalculation);

        // Construct the query
        List<Tuple> result = jpaQueryFactory
                .select(place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered"),
                        Expressions.numberTemplate(Double.class, distanceExpression).as("distance"))
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.id.gt(countCursor))
                .groupBy(place.id)
                .orderBy(Expressions.numberTemplate(Double.class, distanceExpression).asc(), place.registerCount.desc(), place.id.asc())
                .limit(size)
                .fetch();

        return result;
    }



    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<jakarta.persistence.Tuple> findSimilarPlacesByKNN2(Member member, ExploreFilter filter, Long countCursor, TagScoreDto tagScoreDto, int size) {
        StringBuilder jpql = new StringBuilder("SELECT p AS place, " +
                "GROUP_CONCAT(CASE WHEN ptl.tagScore <> 0 THEN t.id ELSE NULL END) AS tagIds, " +
                "GROUP_CONCAT(CASE WHEN ptl.tagScore <> 0 THEN t.tagName ELSE NULL END) AS tagNames, " +
                "CASE WHEN COUNT(pr) > 0 THEN TRUE ELSE FALSE END AS isRegistered, " +
                "SQRT(SUM(");

        for (int i = 1; i <= 24; i++) {
            jpql.append("CASE WHEN t.id = :tagId").append(i)
                    .append(" THEN POWER(ptl.tagScore - :score").append(i)
                    .append(", 2) ELSE 0 END");

            if (i < 24) {
                jpql.append(" + ");
            }
        }

        jpql.append(")) AS distance " +
                "FROM Place p " +
                "JOIN PlaceTagLog ptl ON ptl.place = p " +
                "JOIN ptl.tag t " +
                "LEFT JOIN PlaceRegister pr ON pr.place = p AND pr.member = :member ");

        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            jpql.append("WHERE p.city = :cityFilter ");
        } else {
            jpql.append("WHERE 1 = 1 "); // 필터가 없을 때는 기본적으로 모든 결과를 고려
        }

        jpql.append("GROUP BY p.id " +
                "ORDER BY distance ASC, p.registerCount DESC, p.id ASC");

        TypedQuery<jakarta.persistence.Tuple> query = entityManager.createQuery(jpql.toString(), jakarta.persistence.Tuple.class);

        // 지역 필터 매개변수 설정
        if (filter.getCityFilter() != null) {
            query.setParameter("cityFilter", filter.getCityFilter());
        }

        // 태그 스코어 매개변수 설정
        for (int i = 1; i <= 24; i++) {
            query.setParameter("tagId" + i, i);
            query.setParameter("score" + i, tagScoreDto.getTagScore(i));
        }
        query.setParameter("member", member);

        // 페이지 계산
        int offset = (int) ((countCursor - 1) * size);
        query.setFirstResult(offset);
        query.setMaxResults(size);

        // 쿼리 실행 및 결과 반환
        return query.getResultList();
    }



}
