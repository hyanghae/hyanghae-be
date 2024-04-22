package flaskspring.demo.image.util;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadUtil {

    String uploadImage(MultipartFile file, Long memberId);

    String updateImage(MultipartFile file, String existingImageName, Long memberId);

    void deleteImage(String savedImageName, Long memberId);
}
