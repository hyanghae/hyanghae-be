package flaskspring.demo.utils;

import flaskspring.demo.exception.BaseException;
import flaskspring.demo.exception.BaseResponseCode;
import flaskspring.demo.home.dto.res.ImgRecommendationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlaskService {

    private final FlaskConfig flaskConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<ImgRecommendationDto> getResultFromFlask(MultipartFile placeImage) {

        try {
            byte[] imageBytes = convertImageToByteArray(placeImage);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = createRequestEntity(placeImage, imageBytes);
            log.info("이미지 요청 들어옴");
            ResponseEntity<List<ImgRecommendationDto>> response = sendImageToFlask(requestEntity);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to share image: {}", e.getMessage());
            throw new BaseException(BaseResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] convertImageToByteArray(MultipartFile placeImage) throws IOException {
        return placeImage.getBytes();
    }

    private HttpEntity<MultiValueMap<String, Object>> createRequestEntity(MultipartFile placeImage, byte[] imageBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource contentsAsResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return placeImage.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("photo", contentsAsResource);

        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<List<ImgRecommendationDto>> sendImageToFlask(HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        return restTemplate.exchange(

                flaskConfig.getBaseUrl() + "/img-recommends/upload-image",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<ImgRecommendationDto>>() {
                }
        );
    }

}
