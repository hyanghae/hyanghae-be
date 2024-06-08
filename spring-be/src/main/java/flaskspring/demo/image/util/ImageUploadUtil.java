package flaskspring.demo.image.util;

import flaskspring.demo.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadUtil {

    String uploadImage(MultipartFile file, Member member);

    String updateImage(MultipartFile file, String existingImageName, Member member);

    void deleteImage(String savedImageName, Member member);
}
