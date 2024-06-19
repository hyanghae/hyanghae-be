package flaskspring.demo.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
@Configuration
public class RedisInfo {
    private List<String> nodes;
   // private String password;
    private int maxRedirects;

   /* @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClusterInfo {
        private String nodes;
        private String clientName;
        private int maxRedirects;
    }*/
}