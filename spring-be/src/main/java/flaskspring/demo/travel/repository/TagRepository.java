package flaskspring.demo.travel.repository;

import flaskspring.demo.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t JOIN FETCH t.category")
    List<Tag> findAllWithCategory();

    List<Tag> findByIdIn(List<Long> indexes);
}
