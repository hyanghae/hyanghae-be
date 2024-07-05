package flaskspring.demo.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchRankingCleanupService {

    private static final String RANKING_KEY = "ranking";
    private static final long WINDOW_SIZE = 24 * 60 * 60 * 1000; // 24시간을 밀리초로
    private static final long SCHEDULE_TIME = 1 * 60 * 60 * 1000; // 1시간을 밀리초로

    private final RedisTemplate<String, String> redisTemplate;


    @Scheduled(fixedRate = SCHEDULE_TIME) // 1시간마다 실행
    public void cleanupOldSearchData() {
        long currentTime = System.currentTimeMillis();
        long cutoffTime = currentTime - WINDOW_SIZE;

        long removedCount = redisTemplate.opsForZSet().removeRangeByScore(RANKING_KEY, 0, cutoffTime);

        log.info("스케쥴러 실행 Cleaned up {} old search entries", removedCount);
    }
}