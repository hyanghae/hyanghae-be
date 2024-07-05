package flaskspring.demo.place.register.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.QPlace;
import flaskspring.demo.place.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;


@RequiredArgsConstructor
public class PlaceRegisterRepositoryCustomImpl implements PlaceRegisterRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;


    @Override
    public List<Tuple> findSavedPlacesByMember(Member member, Pageable pageable) {
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"), // tagId를 모음
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(placeRegister)
                .join(placeRegister.place, place) // 등록된 여행지에 대한 별칭 지정
                .join(placeTagLog).on(placeRegister.place.eq(placeTagLog.place).and(placeTagLog.tagScore.ne(0)))
                .where(placeRegister.member.eq(member)) // member가 등록한 여행지
                .groupBy(place.id) // 여행지 별 그루핑
                .orderBy(placeRegister.createdTime.min().desc()); // createdTime을 기준으로 내림차순(최신순) 정렬

        if (!pageable.isUnpaged()) {
            query = query.offset(pageable.getOffset()) // 페이지 시작 오프셋 설정
                    .limit(pageable.getPageSize()); // 한 페이지당 가져올 개수 설정
        }

        return query.fetch();
    }



}
