package flaskspring.demo.tag.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.travel.domain.Place;
import flaskspring.demo.travel.domain.QPlace;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class PlaceTagLogRepositoryCustomImpl implements PlaceTagLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;


    public List<Tuple> findByTagIn(List<Tag> tags) {

        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());

        return jpaQueryFactory
                .select(place,
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"))
                .from(placeTagLog)
                .join(placeTagLog.tag, tag)
                .join(placeTagLog.place, place)
                .where(placeTagLog.tag.id.in(tagIds))
                .groupBy(place.id)
                .fetch();
    }


}
