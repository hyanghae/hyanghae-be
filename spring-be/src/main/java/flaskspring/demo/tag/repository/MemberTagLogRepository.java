package flaskspring.demo.tag.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.tag.domain.MemberTagLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTagLogRepository extends JpaRepository<MemberTagLog, Long> {

    List<MemberTagLog> findByMember(Member member);
}
