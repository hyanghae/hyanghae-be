package flaskspring.demo.image.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.image.util.CustomMultipartFile;
import flaskspring.demo.image.util.ImageUploadUtil;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
import flaskspring.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UploadImageService {

    private final UploadImageRepository uploadImageRepository;

    private final ImageFileService imageFileService;
    private final ImageUploadUtil imageUploadUtil;

    public void uploadImage(MultipartFile file, Member member) {

        imageUploadUtil.uploadImage(file, member);
        member.canRecommend();
        member.setRefreshNeeded();
    }

    public void deleteSettingImage(Member member) {
        Optional<UploadImage> uploadImage = uploadImageRepository.findByMemberAndIsSetting(member, true);
        uploadImage.ifPresent(upload -> {
            upload.deSetting();
            member.setRefreshNeeded(); // 삭제가 진행된 경우 refreshNeeded 설정
        });
    }



    public boolean isSettingImageExist(Member member) {
        return uploadImageRepository.findByMemberAndIsSetting(member, true).isPresent();
    }

    public String getSettingImageURL(Member member) {
        Optional<UploadImage> uploadImage = uploadImageRepository.findByMemberAndIsSetting(member, true);
        return uploadImage.map(UploadImage::getSavedImageUrl).orElse(null);
    }


    public MultipartFile getSettingImageFile(Member member) {

        UploadImage uploadImage = uploadImageRepository.findByMemberAndIsSetting(member, true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_IMAGE_EXCEPTION));

        return convertToMultipartFile(uploadImage);
    }

    private MultipartFile convertToMultipartFile(UploadImage uploadImage) {
        try {
            return imageFileService.getImageAsMultipartFile(uploadImage);
        } catch (IOException e) {
            log.error("Error occurred 이미지 읽기 오류 image to MultipartFile: ", e);
            throw new BaseException(BaseResponseCode.IMAGE_PROCESSING_ERROR);
        }
    }


//    private byte[] downloadImageFromUrl(String imageUrl) throws IOException {
//        URL url = new URL(imageUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.connect();
//
//        InputStream inputStream = connection.getInputStream();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//
//        inputStream.close();
//        return outputStream.toByteArray();
//    }
//
//    private MultipartFile convertToMultipartFile(byte[] imageBytes, String originalFilename, String contentType) {
//        return new CustomMultipartFile(imageBytes, originalFilename, "file", contentType);
//    }
}
