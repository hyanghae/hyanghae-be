package flaskspring.demo.webView;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class webViewController {

    @GetMapping("/terms-and-conditions")
    public String termsAndConditions() {
        log.info("웹뷰 진입");
        return "terms-and-conditions"; // This will serve terms-and-conditions.html
    }
}
