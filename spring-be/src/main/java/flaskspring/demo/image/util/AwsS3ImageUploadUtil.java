package flaskspring.demo.image.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import jakarta.persistence.SequenceGenerators;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@Profile({"deploy", "docker"})
public class AwsS3ImageUploadUtil implements ImageUploadUtil {

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;
    private final UploadImageRepository uploadImageRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;/**/

    @Override
    public String uploadImage(MultipartFile file, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        //파일 이름
        String originalFilename = file.getOriginalFilename();
        //파일 이름이 비어있으면 (assert 오류 반환)
        assert originalFilename != null;

        /* --- 이름 생성 --- */
        String filename = createFilename();
        String fileExtension = extractExtension(file.getOriginalFilename());
        String savedFilename = "image/" + getSaveFilename(filename, fileExtension);

        /* --- 메타 정보 생성 --- */
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String s3name = "hyanghae/" + savedFilename;

        try {
            amazonS3Client.putObject(bucket, s3name, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.debug("uploading error = ", e);
        }

        String savedImgUrl = amazonS3.getUrl(bucket, s3name).toString().replaceAll("\\+", "+");

        uploadImageRepository.findByMemberAndIsSetting(member, true).ifPresent(UploadImage::deSetting);

        UploadImage uploadImage = UploadImage.builder()
                .originalFileName(originalFilename)
                .saveFileName(savedFilename)
                .savedImageUrl(savedImgUrl)
                .extension(fileExtension)
                .member(member)
                .isSetting(true)
                .build();

        uploadImageRepository.save(uploadImage);

        return savedImgUrl;
    }

    private String getSaveFilename(String filename, String fileExtension) {
        return filename + "." + fileExtension;
    }

    private String createFilename() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String updateImage(MultipartFile file, String existingImageName, Long memberId) {
        deleteImage(existingImageName, memberId);
        return uploadImage(file, memberId);
    }

    @Override
    public void deleteImage(String savedImageName, Long memberId) {
        amazonS3Client.deleteObject(bucket, savedImageName);
    }

    private String extractExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }
}
