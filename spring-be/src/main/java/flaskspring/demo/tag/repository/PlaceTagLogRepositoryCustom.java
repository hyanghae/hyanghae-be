package flaskspring.demo.tag.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.Tag;
import org.springframework.data.repository.query.Param;

import javax.swing.plaf.TableUI;
import java.util.List;

public interface PlaceTagLogRepositoryCustom {

    List<Tuple> findByTagIn(List<Tag> tags, String sort, Member members);
}
