package flaskspring.demo.image.service;

import flaskspring.demo.image.domain.UploadImage;
import flaskspring.demo.image.util.CustomMultipartFile;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Profile({"local"})
@Service
public class LocalImageFileService implements ImageFileService {

    @Override
    public MultipartFile getImageAsMultipartFile(UploadImage uploadImage) throws IOException {
        File file = new File(uploadImage.getSavedImageUrl());
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String contentType = Files.probeContentType(file.toPath());
        String originalFilename = file.getName();

        return new CustomMultipartFile(fileContent, originalFilename, "file", contentType);
    }
}