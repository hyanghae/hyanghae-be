package flaskspring.demo.place.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTagLogRepository extends JpaRepository<MemberTagLog, Long> {

    List<MemberTagLog> findByMember(Member member);

    /*@Query("SELECT DISTINCT mtl.tag FROM MemberTagLog mtl JOIN FETCH mtl.tag WHERE mtl.member = :member")
    List<MemberTagLog> findTagsByMember(@Param("member") Member member);*/

    boolean existsByMemberAndTag(Member member, Tag tag);

    void deleteByMember(Member member);
}
