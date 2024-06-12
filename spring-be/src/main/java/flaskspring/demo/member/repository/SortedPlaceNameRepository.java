package flaskspring.demo.member.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.SortedPlaceName;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SortedPlaceNameRepository extends JpaRepository<SortedPlaceName, Long>, SortedPlaceNameRepositoryCustom {

    @Modifying
    @Query("DELETE FROM SortedPlaceName s WHERE s.member = :member")
    void deleteByMember(@Param("member")Member member);
}
