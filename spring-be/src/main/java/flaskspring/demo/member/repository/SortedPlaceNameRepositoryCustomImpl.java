package flaskspring.demo.member.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import flaskspring.demo.like.domain.QPlaceLike;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.QSortedPlaceName;
import flaskspring.demo.member.domain.SortedPlaceName;
import flaskspring.demo.place.domain.QPlace;
import flaskspring.demo.register.domain.QPlaceRegister;
import flaskspring.demo.tag.domain.QPlaceTagLog;
import flaskspring.demo.tag.domain.QTag;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SortedPlaceNameRepositoryCustomImpl implements SortedPlaceNameRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    QPlaceTagLog placeTagLog = QPlaceTagLog.placeTagLog;
    QTag tag = QTag.tag;
    QPlace place = QPlace.place;
    QPlaceRegister placeRegister = QPlaceRegister.placeRegister;
    QSortedPlaceName sortedPlaceName = QSortedPlaceName.sortedPlaceName;

    public List<Tuple> findExplorePlaceSorted(Member member, ExploreFilter filter, ExploreCursor cursor, int size) {

        JPAQuery<Tuple> query = jpaQueryFactory.select(
                        place,
                        Expressions.stringTemplate("group_concat({0})", tag.id).as("tagIds"),
                        Expressions.stringTemplate("group_concat({0})", tag.tagName).as("tagNames"),
                        placeRegister.place.isNotNull().as("isRegistered"),
                        sortedPlaceName.id
                )
                .from(sortedPlaceName)
                .join(place).on(place.touristSpotName.eq(sortedPlaceName.placeName))
                .join(placeTagLog).on(placeTagLog.place.eq(place).and(placeTagLog.tagScore.ne(0)))
                .join(placeTagLog.tag, tag)
                .leftJoin(placeRegister).on(placeRegister.place.eq(place).and(placeRegister.member.eq(member)))
                .where(sortedPlaceName.member.eq(member));

        // 지역 필터 적용
        if (filter.getCityFilter() != null) {
            query.where(place.city.eq(filter.getCityFilter()));
        }

        if (cursor.getIdCursor() != null) {
            query.where(sortedPlaceName.id.gt(cursor.getIdCursor()));
        }

        return query
                .groupBy(sortedPlaceName.id, place.id) // 중복 방지를 위해 그룹화
                .limit(size)
                .fetch();
    }

    private final JdbcTemplate jdbcTemplate;

    public void saveAllSortedPlace(List<SortedPlaceName> placeNames) {
        jdbcTemplate.batchUpdate("insert into " +
                        "sorted_place_name " +
                        "(place_name, member_id, tag_score, image_score, sum) " +
                        "values (?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        SortedPlaceName placeName = placeNames.get(i);
                        ps.setString(1, placeName.getPlaceName());
                        ps.setLong(2, placeName.getMember().getMemberId());

                        // 태그 스코어가 null이면 null을 설정하고, 그렇지 않으면 값 설정
                        if (placeName.getTagScore() != null) {
                            ps.setDouble(3, placeName.getTagScore());
                        } else {
                            ps.setNull(3, Types.DOUBLE);
                        }

                        // 이미지 스코어가 null이면 null을 설정하고, 그렇지 않으면 값 설정
                        if (placeName.getImageScore() != null) {
                            ps.setDouble(4, placeName.getImageScore());
                        } else {
                            ps.setNull(4, Types.DOUBLE);
                        }

                        // 합계가 null이면 null을 설정하고, 그렇지 않으면 값 설정
                        if (placeName.getSum() != null) {
                            ps.setDouble(5, placeName.getSum());
                        } else {
                            ps.setNull(5, Types.DOUBLE);
                        }
                    }


                    @Override
                    public int getBatchSize() {
                        return placeNames.size();
                    }
                });
    }
}
