package flaskspring.demo.tag.repository;

import com.querydsl.core.Tuple;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.Tag;

import java.util.List;

public interface PlaceTagLogRepositoryCustom {

    List<Tuple> getFeedByTags(List<Tag> tags, String sort, Member members);
}
