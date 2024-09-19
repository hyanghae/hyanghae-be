package flaskspring.demo.schedule.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByMember(Member member);

    @Modifying
    @Query(value = "DELETE FROM schedule WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberHard(@Param("memberId") Long memberId);
}
