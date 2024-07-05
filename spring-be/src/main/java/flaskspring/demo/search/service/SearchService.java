package flaskspring.demo.search.service;

import flaskspring.demo.search.dto.res.ResSearchRankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private static final String RANKING_KEY = "ranking";
    private static final long WINDOW_SIZE = 24 * 60 * 60 * 1000; // 24시간을 밀리초로

    private final RedisTemplate<String, String> redisTemplate;


    public void saveSearchQuery(String searchQuery) {
        long currentTime = System.currentTimeMillis();
        String member = searchQuery + ":" + currentTime;
        redisTemplate.opsForZSet().add(RANKING_KEY, member, currentTime);
    }

    public List<ResSearchRankDto> getPopularSearches() {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - WINDOW_SIZE;

        Set<ZSetOperations.TypedTuple<String>> rankings = redisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(RANKING_KEY, windowStart, currentTime);

        Map<String, Long> searchFrequencyMap = new HashMap<>();

        if (rankings != null) {
            for (ZSetOperations.TypedTuple<String> tuple : rankings) {
                String[] parts = tuple.getValue().split(":");
                String searchQuery = parts[0];
                searchFrequencyMap.put(searchQuery, searchFrequencyMap.getOrDefault(searchQuery, 0L) + 1);
            }
        }

        AtomicInteger rank = new AtomicInteger(0);
        return searchFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10) // 상위 10개만
                .map(entry -> new ResSearchRankDto(
                        rank.incrementAndGet(),
                        entry.getKey(),
                        entry.getValue().intValue()
                ))
                .collect(Collectors.toList());
    }
}