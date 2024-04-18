package flaskspring.demo.tag.repository;

import flaskspring.demo.tag.domain.MemberTagLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTagLogRepository extends JpaRepository<MemberTagLog, Long> {
}
