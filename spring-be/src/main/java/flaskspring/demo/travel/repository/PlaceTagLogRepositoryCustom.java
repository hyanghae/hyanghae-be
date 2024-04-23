package flaskspring.demo.travel.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.Tag;
import flaskspring.demo.travel.dto.res.ResPlace;
import org.springframework.data.repository.query.Param;

import javax.swing.plaf.TableUI;
import java.util.List;

public interface PlaceTagLogRepositoryCustom {

    List<Tuple> getFeedByTags(List<Tag> tags, String sort, Member members);
}
