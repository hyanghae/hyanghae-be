package flaskspring.demo.member.repository;

import flaskspring.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

    boolean existsByAccount(String account);

    boolean existsByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);

    Optional<Member> findByNickname(String nickname);
}
