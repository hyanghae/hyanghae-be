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
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
@Configuration
public class RedisInfo {
    private int maxRedirects;
    private String password;
    private String connectIp;
    private List<String> nodes;
}