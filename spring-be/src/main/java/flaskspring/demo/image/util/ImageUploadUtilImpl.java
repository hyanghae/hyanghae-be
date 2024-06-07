package flaskspring.demo.image.util;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Profile({"local"})
@Service
@Transactional
@RequiredArgsConstructor
public class ImageUploadUtilImpl implements ImageUploadUtil {

    String UPLOAD_PATH = "C:/Temp/hyanghae/upload_image/";
    private final MemberRepository memberRepository;
    private final UploadImageRepository uploadImageRepository;

    @Override
    public String uploadImage(MultipartFile file, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));
        //파일 이름
        String originalFilename = file.getOriginalFilename();
        //파일 이름이 비어있으면 (assert 오류 반환)
        assert originalFilename != null;

        String fileName = createFileName();
        String fileExtension = extractExtension(file.getOriginalFilename());
        String savedImgUrl = getFilePath(UPLOAD_PATH, fileName, fileExtension);

        File saveFile = new File(savedImgUrl);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //기존 설정 이미지가 있다면 디세팅. 세팅 이미지는 하나여야 한다
        uploadImageRepository.findByMemberAndIsSetting(member, true).ifPresent(UploadImage::deSetting);


        UploadImage uploadImage = UploadImage.builder()
                .originalFileName(originalFilename)
                .saveFileName(fileName)
                .savedImageUrl(savedImgUrl)
                .extension(fileExtension)
                .member(member)
                .isSetting(true)
                .build();

        uploadImageRepository.save(uploadImage);
        return savedImgUrl;
    }

    @Override
    public String updateImage(MultipartFile file, String existingImageName, Long memberId) {
        deleteImage(existingImageName, memberId);
        return uploadImage(file, memberId);
    }


    @Override
    public void deleteImage(String savedImageName, Long memberId) {
        String fileExtension = extractExtension(savedImageName);
        String filename = extractFilename(savedImageName);
        String filePath = getFilePath(UPLOAD_PATH, filename, fileExtension);

        File existFile = new File(filePath);
        existFile.delete();
    }

    private String extractFilename(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(0, index);
    }

    private String createFileName() {
        return UUID.randomUUID().toString();
    }


    private String extractExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    private String getFilePath(String uploadPath, String fileName, String fileExtension) {
        return uploadPath + fileName + "." + fileExtension;
    }
}