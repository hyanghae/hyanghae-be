package flaskspring.demo.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static flaskspring.demo.utils.Constant.BASE_FLASK_REDIRECT_URL;


//@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class ToPythonController {

    String url = BASE_FLASK_REDIRECT_URL + "members/";

    @GetMapping("/to-python/{id}")
    public ResponseEntity<Void> toPythonGet(@PathVariable Long id) {

        System.out.println("python Get");


        //요청을 리다이렉트 GET으로만 보낼 수 있다
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, url + id)
                .build();
    }

   /* @PostMapping("/to-python")
    public ResponseEntity<String> toPythonPost(@RequestBody MemberDto memberDto) {
        System.out.println("python POST");

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, url)
                .body("asdasd");
    }

    @PutMapping("/to-python/{id}")
    public ResponseEntity<ApiResponse> toPythonPut(@PathVariable Long id, @RequestBody(required = false) MemberDto memberDto) {

        RestTemplate restTemplate = new RestTemplate();



        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 설정
        HttpEntity<MemberDto> requestEntity = new HttpEntity<>(memberDto, headers);

        // PUT 요청 보내기
        // PUT 요청 보내기
        ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                url + id,
                HttpMethod.PUT,
                requestEntity,
                ApiResponse.class
        );

        ApiResponse body = responseEntity.getBody();
        System.out.println("body = " + body);

        return ResponseEntity.ok(body);
    }*/

    @DeleteMapping("/to-python/{id}")
    public ResponseEntity<Void> toPythonDelete(@PathVariable(required = false) Long id) {
        if (id != null) {
            System.out.println("python DELETE " + id);
        } else {
            System.out.println("python DELETE");
        }

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, url + id)
                .build();
    }


}
