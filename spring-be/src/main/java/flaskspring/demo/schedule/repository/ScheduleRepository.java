package flaskspring.demo.schedule.repository;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByMember(Member member);
    Optional<Schedule> findByMemberAndId(Member member, Long scheduleId);
    List<Schedule> findByMemberAndStartDateBefore(Member member, LocalDate currentDate);
    List<Schedule> findByMemberAndStartDateAfter(Member member, LocalDate currentDate);


    @Modifying
    @Query(value = "DELETE FROM schedule WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberHard(@Param("memberId") Long memberId);
}
