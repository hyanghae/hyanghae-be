package flaskspring.demo.search.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

@Data
@NoArgsConstructor
public class ResSearchRankDto {
    @Schema(description = "검색어 순위", example = "1")
    int rank;

    @Schema(description = "검색 키워드", example = "해운대")
    String keyWord;

    @Schema(description = "검색 횟수", example = "20")
    double searchCount;


    public ResSearchRankDto(int rank, String keyWord, double score) {
        this.rank = rank;
        this.keyWord = keyWord;
        this.searchCount = score;
    }
    public static ResSearchRankDto convertToResponseRankingDto(ZSetOperations.TypedTuple<Object> typedTuple, int rank) {
        String keyWord = (String) typedTuple.getValue();
        double score = typedTuple.getScore() != null ? typedTuple.getScore() : 0;
        return new ResSearchRankDto(rank, keyWord, score);
    }
}
