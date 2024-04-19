package flaskspring.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 서버 응답시 json 을 자바스크립트에서 처리할 수 있음
        config.addAllowedOriginPattern("*"); // 모든 ip 에 응답 허용
        config.addAllowedHeader("*"); // 모든 header 응답 허용
        config.addExposedHeader("*");
        config.addAllowedMethod("*"); // 모든 요청 메소드 응답 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
/**
 * 1. corsFilter 설정 확인하기
 *
 * 프로젝트를 진행하던 당시 나는 이부분에서 삐끗했다.😕 응답 헤더에 토큰이 들어간 상태로 프론트에 잘 넘어갔는데 정보를 읽어올 수가 없다는 것이다.
 * 열심히 구글링을 해보니 이 설정이 빠져 있었다.
 */
