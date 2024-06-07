package flaskspring.demo.image.service;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.image.util.CustomMultipartFile;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UploadImageService {

    private final UploadImageRepository uploadImageRepository;
    private final MemberRepository memberRepository;
    private final ImageFileService imageFileService;

    public MultipartFile getSettingImageFile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

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
