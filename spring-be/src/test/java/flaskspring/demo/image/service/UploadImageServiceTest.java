package flaskspring.demo.image.service;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.service.MemberService;
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
    @Autowired
    MemberService memberService;

    @Test
    void getSettingImageFileTest() throws IOException {
        Member member = memberService.findMemberById(1L);
        MultipartFile multipartFile = uploadImageService.getSettingImageFile(member);

        assertThat(multipartFile).isNotNull();
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        assertThat(multipartFile.getContentType()).isEqualTo("image/jpeg"); // 파일 타입에 따라 다를 수 있음
        assertThat(multipartFile.getSize()).isGreaterThan(0);
    }
}