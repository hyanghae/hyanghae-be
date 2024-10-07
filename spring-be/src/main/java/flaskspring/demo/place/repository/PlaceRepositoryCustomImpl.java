package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.home.dto.req.TagScoreDto;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.place.domain.QPlace;
import flaskspring.demo.place.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;

    @Override
    public List<Tuple> getFeedByPlaceNames(List<String> placeNames, Member member) {
        List<Tuple> tuples = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        Expressions.stringTemplate("count({0})", tag.tagName).as("sameTagCount"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.touristSpotName.in(placeNames))
                .groupBy(place.id)
                .fetch();

        return tuples;
    }

    @Override
    public Tuple findPlaceDetail(Member member, Long placeId) {

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
                        place.id.eq(placeId)
                )
                .groupBy(place.id) //그루핑
                .fetchFirst();
    }

    @Override
    public List<Tuple> findByIds(Member member, List<Long> ids) {
        return jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .leftJoin(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .leftJoin(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(
                        place.id.in(ids)
                )
                .groupBy(place.id) //그루핑
                .fetch();
    }

    @Override
    public List<Tuple> findNearbyPlaces(Member member, Place targetPlace) {

        double mapX = targetPlace.getLocation().getMapX();
        double mapY = targetPlace.getLocation().getMapY();


        NumberExpression<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                mapY, place.location.mapY, place.location.mapX, mapX);

        return jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered"),
                        distanceExpression.as("distance")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .orderBy(distanceExpression.asc()) // 가까운 거리 순으로 정렬
                .offset(1) // 첫 번째 결과를 건너뛰기
                .limit(3) // 두 번째부터 네 번째까지 선택
                .groupBy(place.id) // 그룹화
                .fetch();
    }

    @Override
    public List<Tuple> findRisingPlaces(Member member, ExploreCursor exploreCursor, int size) {

        // Null 체크 후 Optional로 감싸기
        Optional<Long> optionalCountCursor = Optional.ofNullable(exploreCursor.getCountCursor());
        Optional<Long> optionalPlaceId = Optional.ofNullable(exploreCursor.getIdCursor());

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

    @Override
    public List<Tuple> findPlacesByRegionQuery(Member member, ExploreFilter filter, String regionQuery, ExploreCursor cursor, int size) {
        Long countCursor = cursor.getCountCursor();

        JPAQuery<Tuple> query = jpaQueryFactory.select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.region.containsIgnoreCase(regionQuery));

        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            query.where(place.city.eq(filter.getCityFilter()));
        }

        // 커서와 사이즈를 사용하여 페이징 적용
        query.offset(countCursor)
                .limit(size);

        return query
                .groupBy(place.id) // 예시로 group by 추가
                .fetch();
    }


    @Override
    public List<Tuple> findPlacesByCityQuery(Member member, ExploreFilter filter, String citiQuery, ExploreCursor cursor, int size) {
        Long countCursor = cursor.getCountCursor();

        JPAQuery<Tuple> query = jpaQueryFactory.select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.city.containsIgnoreCase(citiQuery));
        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            query.where(place.city.eq(filter.getCityFilter()));
        }

        // 커서와 사이즈를 사용하여 페이징 적용
        query.offset(countCursor)
                .limit(size);

        return query
                .groupBy(place.id) // 예시로 group by 추가
                .fetch();
    }

    @Override
    public List<Tuple> findPlacesByNameQuery(Member member, ExploreFilter filter, String nameQuery, ExploreCursor cursor, int size) {
        Long countCursor = cursor.getCountCursor();

        JPAQuery<Tuple> query = jpaQueryFactory.select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.touristSpotName.containsIgnoreCase(nameQuery));
        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            query.where(place.city.eq(filter.getCityFilter()));
        }

        // 커서와 사이즈를 사용하여 페이징 적용
        query.offset(countCursor)
                .limit(size);

        return query
                .groupBy(place.id) // 예시로 group by 추가
                .fetch();
    }

    @Override
    public List<Tuple> findPlacesByTag(Member member, ExploreFilter filter, Long tagId, ExploreCursor cursor, int size) {
        Long countCursor = cursor.getCountCursor();

        // 서브쿼리로 해당 태그 아이디를 가진 여행지의 ID를 가져옵니다.
        JPAQuery<Long> subQuery = jpaQueryFactory.select(place.id)
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .where(QPlaceTagLog.placeTagLog.tag.id.eq(tagId));

        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            subQuery.where(place.city.eq(filter.getCityFilter()));
        }

        // 메인 쿼리에서 서브쿼리의 결과를 사용
        JPAQuery<Tuple> query = jpaQueryFactory.select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(place)
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(place.id.in(subQuery));

        query.offset(countCursor)
                .limit(size);

        return query.groupBy(place.id).fetch();
    }


}
