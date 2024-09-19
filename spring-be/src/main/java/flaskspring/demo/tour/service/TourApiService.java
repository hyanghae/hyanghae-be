package flaskspring.demo.tour.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import flaskspring.demo.tour.dto.ApiResponse;
import flaskspring.demo.tour.dto.ResFood;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourApiService {


    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tour.api.key}")
    private String apiKey;


    public List<ResFood> getNearbyFoods(double mapx, double mapy) throws URISyntaxException, JsonProcessingException {
        // API 요청 URL 구성
        String url = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1?" +
                "ServiceKey=" + apiKey +
                "&mapX=" + mapx +
                "&mapY=" + mapy +
                "&radius=20000" +  // 반경 20km 내 검색
                "&contentTypeId=39" + // 음식점
                "&numOfRows=10" +
                "&pageNo=1" +
                "&MobileOS=ETC&MobileApp=AppTesting&_type=json";

        System.out.println(url);

        // API 호출
        URI uri = new URI(url);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 응답을 String으로 받아오기
        String response = restTemplate.getForObject(uri, String.class);

        // JSON 응답 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        List<ResFood> foodList = new ArrayList<>();
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                ResFood resFood = new ResFood(
                        itemNode.path("title").asText(),
                        itemNode.path("addr1").asText(),
                        itemNode.path("tel").asText(),
                        itemNode.path("dist").asDouble(),
                        itemNode.path("firstimage").asText(),
                        itemNode.path("mapx").asDouble(),
                        itemNode.path("mapy").asDouble()
                );
                foodList.add(resFood);
            }
        }

        return foodList;
    }

}
