package flaskspring.demo.member.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.SortedPlaceName;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface SortedPlaceNameRepositoryCustom {

    List<Tuple> findExplorePlaceSorted(Member member, ExploreFilter filter, ExploreCursor cursor, int size);

    void saveAllSortedPlace(List<SortedPlaceName> placeNames);
}
