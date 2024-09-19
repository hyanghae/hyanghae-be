package flaskspring.demo.member.repository;

import flaskspring.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

    boolean existsByAccount(String account);

    boolean existsByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);

    Optional<Member> findByNickname(String nickname);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Member m WHERE member_id = :memberId", nativeQuery = true)
    void deleteMemberFromDB(@Param("memberId") Long memberId);
}
