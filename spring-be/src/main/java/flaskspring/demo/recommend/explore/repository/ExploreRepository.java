package flaskspring.demo.recommend.explore.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.utils.cursor.ExploreCursor;
import flaskspring.demo.utils.filter.ExploreFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExploreRepository {

    List<Tuple> findExplorePlaceWithoutTags(Member member, ExploreFilter filter, ExploreCursor cursor, int size);

    List<Tuple> findExplorePlaceWithTags(Member member, List<Long> tagIds);

    List<Tuple> findExplorePlaceWithRecommend(Member member, List<String> placeNames);
}
