package flaskspring.demo.image.service;

import flaskspring.demo.config.redis.cache.EvictRedisCache;
import flaskspring.demo.config.redis.cache.RedisCacheable;
import flaskspring.demo.config.redis.cache.RedisCachedKeyParam;
import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.repository.UploadImageRepository;
import flaskspring.demo.image.util.ImageUploadUtil;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.dto.Res.ResUploadedImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UploadImageService {

    private final UploadImageRepository uploadImageRepository;
    private final ImageFileService imageFileService;
    private final ImageUploadUtil imageUploadUtil;

    @EvictRedisCache(cacheName = "settingImageURL")
    public void uploadImage(MultipartFile file, @RedisCachedKeyParam(key = "member", fields = "memberId") Member member) {

        imageUploadUtil.uploadImage(file, member);
        member.canRecommend();
        member.setRefreshNeeded();
    }

    @EvictRedisCache(cacheName = "settingImageURL")
    public void deleteSettingImage(@RedisCachedKeyParam(key = "member", fields = "memberId") Member member) {
        Optional<UploadImage> uploadImage = uploadImageRepository.findByMemberAndIsSetting(member, true);
        uploadImage.ifPresent(upload -> {
            upload.deSetting();
            member.setRefreshNeeded(); // 삭제가 진행된 경우 refreshNeeded 설정
        });
    }

    public List<ResUploadedImage> getLatestUploadedImage(Member member, Integer limit) {
        List<UploadImage> uploadImages = null;
        if (limit == null) {
            uploadImages = uploadImageRepository.findByMemberOrderByCreatedTimeDesc(member, Pageable.unpaged());
        } else {
            if(limit < 1L){
                throw new BaseException(BaseResponseCode.INVALID_PAGE_NUMBER);
            }
            uploadImages = uploadImageRepository.findByMemberOrderByCreatedTimeDesc(member, PageRequest.of(0, limit));
        }
        return uploadImages.stream().map(ResUploadedImage::new).collect(Collectors.toList());
    }


    public boolean isSettingImageExist(Member member) {
        return uploadImageRepository.findByMemberAndIsSetting(member, true).isPresent();
    }

    //@RedisCacheable(cacheName = "settingImageURL", expireTime = 30)
    //@RedisCachedKeyParam(key = "member", fields = "memberId")
    public String getSettingImageURL( Member member) {
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
