package flaskspring.demo.like.repopsitory;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.like.domain.QPlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.place.domain.QPlace;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PlaceLikeRepositoryCustomImpl implements PlaceLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceLike placeLike = QPlaceLike.placeLike;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;

    @Override
    public List<Tuple> findLikedPlacesByMember(Member member) {
        List<Tuple> tuples = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"), // tagId를 모음
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered"),
                        placeLike.place.isNotNull().as("isLiked"),
                        placeLike.createdTime.min() // 최소 createdTime 선택
                )
                .from(placeLike)
                .join(placeLike.place, place) // 좋아요한 여행지에 대한 별칭 지정
                .join(placeTagLog).on(placeLike.place.eq(placeTagLog.place))
                .leftJoin(placeRegister).on(placeRegister.place.eq(placeTagLog.place).and(placeRegister.member.eq(member))) // 회원의 여행지 등록 정보를 확인하기 위한 조인.
                .where(placeLike.member.eq(member)) // member가 좋아요한 여행지
                .groupBy(place.id) // 여행지 별 그루핑
                .orderBy(placeLike.createdTime.min().desc()) // 가장 오래된 createdTime을 기준으로 내림차순 정렬
                .fetch();

        return tuples;
    }

}
