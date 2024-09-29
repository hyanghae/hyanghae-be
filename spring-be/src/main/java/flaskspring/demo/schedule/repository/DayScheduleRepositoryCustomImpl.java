package flaskspring.demo.schedule.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.place.domain.QPlace;
import flaskspring.demo.place.register.domain.QPlaceRegister;
import flaskspring.demo.schedule.domain.QDaySchedule;
import flaskspring.demo.schedule.domain.QDaySchedulePlaceTag;
import flaskspring.demo.schedule.domain.QSchedule;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DayScheduleRepositoryCustomImpl implements DayScheduleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;

    QDaySchedule daySchedule = QDaySchedule.daySchedule;
    QDaySchedulePlaceTag daySchedulePlaceTag = QDaySchedulePlaceTag.daySchedulePlaceTag;

    @Override
    public List<Tuple> getScheduleDetail(Member member, Long dayScheduleId) {

        List<Tuple> tuples = jpaQueryFactory
                .select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered")
                )
                .from(daySchedulePlaceTag)
                .join(daySchedulePlaceTag.daySchedule, daySchedule)
                .join(daySchedulePlaceTag.place, place)
                .leftJoin(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .leftJoin(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(daySchedule.id.eq(dayScheduleId))
                .groupBy(place)
                .fetch();

        return tuples;
    }
}
