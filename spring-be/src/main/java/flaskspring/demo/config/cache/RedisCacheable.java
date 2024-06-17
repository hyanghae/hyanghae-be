package flaskspring.demo.config.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisCacheable {

    String cacheName();
    long expireTime() default -1;
    String key() default ""; // key 속성 추가

}
