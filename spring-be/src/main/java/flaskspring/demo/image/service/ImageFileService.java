package flaskspring.demo.image.service;

import flaskspring.demo.image.domain.UploadImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageFileService {
    MultipartFile getImageAsMultipartFile(UploadImage uploadImage) throws IOException;
}