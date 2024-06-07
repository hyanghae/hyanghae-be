package flaskspring.demo.image.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UploadImageServiceTest {

    @Autowired
    UploadImageService uploadImageService;

    @Test
    void getSettingImageFileTest() throws IOException {
        MultipartFile multipartFile = uploadImageService.getSettingImageFile(1L);

        assertThat(multipartFile).isNotNull();
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        assertThat(multipartFile.getContentType()).isEqualTo("image/jpeg"); // 파일 타입에 따라 다를 수 있음
        assertThat(multipartFile.getSize()).isGreaterThan(0);
    }
}