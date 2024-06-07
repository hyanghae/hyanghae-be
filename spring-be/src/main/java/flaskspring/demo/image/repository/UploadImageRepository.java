package flaskspring.demo.image.repository;

import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {

    List<UploadImage> findByMemberOrderByCreatedTimeDesc(Member member);

    void deleteByMember(Member member);

    Optional<UploadImage> findByMemberAndIsSetting(Member member, boolean isSetting);

}
