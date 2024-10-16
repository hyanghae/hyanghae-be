package flaskspring.demo.place.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.MemberTagLog;
import flaskspring.demo.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MemberTagLogRepository extends JpaRepository<MemberTagLog, Long> {

    List<MemberTagLog> findByMember(Member member);

    void deleteByMemberAndTagIdIn(Member member, Set<Long> removedTagIds);

    /*@Query("SELECT DISTINCT mtl.tag FROM MemberTagLog mtl JOIN FETCH mtl.tag WHERE mtl.member = :member")
    List<MemberTagLog> findTagsByMember(@Param("member") Member member);*/

    boolean existsByMemberAndTag(Member member, Tag tag);

    @Modifying
    @Query(value = "DELETE FROM member_tag_log WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberHard(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM MemberTagLog m WHERE m.member = :member")
    void deleteByMember(@Param("member") Member member);
}
