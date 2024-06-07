package flaskspring.demo.image.service;

import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.util.CustomMultipartFile;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


@Profile({"deploy", "docker"})
@Service
public class UrlImageFileService implements ImageFileService {

    @Override
    public MultipartFile getImageAsMultipartFile(UploadImage uploadImage) throws IOException {
        URL url = new URL(uploadImage.getSavedImageUrl());

        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] fileContent = inputStream.readAllBytes();
        String contentType = connection.getContentType();

        String originalFilename = uploadImage.getOriginalFileName(); // URL로부터 파일 이름을 유추할 수 있으면 사용

        return new CustomMultipartFile(fileContent, originalFilename, "file", contentType);
    }
}