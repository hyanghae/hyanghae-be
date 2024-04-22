package flaskspring.demo.myPage;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지 기능", description = "마이페이지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {


}
