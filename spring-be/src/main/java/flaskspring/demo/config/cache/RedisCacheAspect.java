package flaskspring.demo.config.cache;

import flaskspring.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class RedisCacheAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(RedisCacheable)")
    public Object cacheableProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisCacheable redisCacheable = getCacheable(joinPoint);
        final String cacheKey = generateKey(joinPoint);

        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)) && !confirmBypass()) {
            // 캐시에서 데이터를 가져왔음을 로그로 출력
            log.info("Getting data from cache: {}", cacheKey);
            return redisTemplate.opsForValue().get(cacheKey);
        }

        // 캐시에 없어서 메서드를 실행했음을 로그로 출력
        log.info("Cache miss for key: {}, executing method", cacheKey);
        final Object methodReturnValue = joinPoint.proceed();
        final long cacheTTL = redisCacheable.expireTime();

        if (cacheTTL < 0) {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue);
            // 캐시에 데이터를 저장했음을 로그로 출력
            log.info("Storing data in cache: {}", cacheKey);
        } else {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS);
            // 캐시에 데이터를 저장했음을 로그로 출력
            log.info("Storing data in cache: {}, TTL: {} seconds", cacheKey, cacheTTL);
        }

        return methodReturnValue;
    }




    private RedisCacheable getCacheable(ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();

        return AnnotationUtils.getAnnotation(method, RedisCacheable.class);
    }



    private String generateKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisCacheable redisCacheable = AnnotationUtils.getAnnotation(method, RedisCacheable.class);

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(redisCacheable.cacheName());

        if (StringUtils.hasText(redisCacheable.key())) {
            keyBuilder.append(":").append(redisCacheable.key());
        } else {
            keyBuilder.append(":").append(StringUtils.arrayToCommaDelimitedString(joinPoint.getArgs()));
        }

        return keyBuilder.toString();
    }

    @Around("@annotation(EvictTagsCache)")
    public Object evictTagsCache(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        EvictTagsCache evictTagsCache = AnnotationUtils.findAnnotation(method, EvictTagsCache.class);

        // 캐시 제거 로직
        String cacheKey = generateCacheKey(evictTagsCache, joinPoint);
        redisTemplate.delete(cacheKey);
        log.info("Evicted cache for key: {}", cacheKey);

        return joinPoint.proceed();
    }

    private String generateCacheKey(EvictTagsCache evictTagsCache, ProceedingJoinPoint joinPoint) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(evictTagsCache.cacheName());

        if (StringUtils.hasText(evictTagsCache.key())) {
            keyBuilder.append(":").append(evictTagsCache.key());
        } else {
            keyBuilder.append(":").append(StringUtils.arrayToCommaDelimitedString(joinPoint.getArgs()));
        }

        return keyBuilder.toString();
    }


    private boolean confirmBypass() {
        // Implement your logic to determine if cache should be bypassed
        return false;  // Default implementation
    }
}