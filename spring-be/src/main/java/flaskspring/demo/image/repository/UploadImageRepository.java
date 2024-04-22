package flaskspring.demo.image.repository;

import flaskspring.demo.image.domain.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}
