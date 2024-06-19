package flaskspring.demo.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class RedisInfo {

    @Value("${spring.data.redis.cluster.nodes}")
    private List<String> nodes;

    @Value("${spring.data.redis.cluster.max-redirects}")
    private int maxRedirects;
}