package flaskspring.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlaskConfig {

    @Value("${flask.base-url}")
    private String flaskUrl;

    public String getBaseUrl() {
        return flaskUrl;
    }
}