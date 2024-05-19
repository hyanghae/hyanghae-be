package flaskspring.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;


@Configuration
public class WebConfig {

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/"); // 루트 경로를 설정합니다.
        resolver.setSuffix(".html"); // 파일 확장자를 설정합니다.
        resolver.setViewClass(InternalResourceView.class); // InternalResourceView를 사용합니다.
        resolver.setOrder(1); // 뷰 리졸버의 우선순위 (낮을수록 우선)
        return resolver;
    }

}
