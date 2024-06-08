package flaskspring.demo.place.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.like.domain.QPlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.Place;
import flaskspring.demo.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.place.domain.QPlace;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
                .join(placeTagLog).on(placeTagLog.place.eq(place))
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
                .join(placeTagLog).on(placeTagLog.place.eq(place))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(
                        place.registerCount.goe(effectiveCountCursor),
                        place.id.gt(effectivePlaceId)
                )
                .groupBy(place.id) //그루핑
                .orderBy(place.registerCount.desc()) //등록 순 내림차순
                .limit(size)
                .fetch();
    }

}
