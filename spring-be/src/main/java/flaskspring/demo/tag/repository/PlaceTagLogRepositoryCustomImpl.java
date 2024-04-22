package flaskspring.demo.tag.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.like.domain.QPlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.travel.domain.QPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class PlaceTagLogRepositoryCustomImpl implements PlaceTagLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceLike placeLike = QPlaceLike.placeLike;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;

    public List<Tuple> findByTagIn(List<Tag> tags, String sort, Member member) {

        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());

        List<Tuple> tuples  = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"), // tagId를 모음
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        Expressions.stringTemplate("count({0})", tag.tagName).as("sameTagCount"),
                        placeLike.place.isNotNull().as("isLiked"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(placeTagLog)
                .join(placeTagLog.tag, tag)
                .join(placeTagLog.place, place)
                .leftJoin(placeLike).on(placeLike.place.eq(place).and(placeLike.member.eq(member))) // 회원의 좋아요 정보를 확인하기 위한 조인.
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member))) // 여행지 등록 정보를 확인하기 위한 조인.
                .where(placeTagLog.tag.id.in(tagIds)) //제공된 TagId를 기반으로
                .groupBy(place.id)
                .orderBy(placeOrder(place, sort))
                .fetch();

        return tuples;
    }

    public OrderSpecifier[] placeOrder(QPlace place, String sort) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (StringUtils.hasText(sort) && sort.equals("alpha")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, place.touristSpotName));
        } else if (StringUtils.hasText(sort) && sort.equals("like")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, place.likeCount));
        } else if (StringUtils.hasText(sort) && sort.equals("register")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, place.registerCount));
        } else { //default :  같은 태그수 && 좋아요 수 정렬
            orderSpecifiers.add(Expressions.numberTemplate(Long.class, "sameTagCount").desc());
            orderSpecifiers.add(Expressions.numberTemplate(Long.class, "likeCount").desc());
        }
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
