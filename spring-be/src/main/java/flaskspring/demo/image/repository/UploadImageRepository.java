package flaskspring.demo.image.repository;

import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {

    List<UploadImage> findByMemberOrderByCreatedTimeDesc(Member member, Pageable pageable);

    @Modifying
    @Query("DELETE FROM UploadImage u WHERE u.member = :member")
    void deleteByMember(@Param("member") Member member); //멤버가 등록한 모든 이미지 삭제

    @Modifying
    @Query(value = "DELETE FROM upload_image WHERE member_id = :memberId", nativeQuery = true)
    void deleteByMemberHard(@Param("memberId") Long memberId);

    Optional<UploadImage> findByMemberAndIsSetting(Member member, boolean isSetting);


}
