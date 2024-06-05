package flaskspring.demo.home.dto.res;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SimFamousPlaceDto {
    private String firstPlace;
    private String secondPlace;
    private String thirdPlace;

    private Long getFamousPlaceId(String place) {
        // "place"를 제거하고 남은 숫자만 추출하는 정규 표현식
        String regex = "place(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(place);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1)); // 첫 번째 캡처 그룹에 있는 숫자를 Long으로 변환하여 반환
        } else {
            throw new IllegalArgumentException("Invalid place format: " + place);
        }
    }

    public Long getFirstPlaceId() {
        return getFamousPlaceId(firstPlace);
    }

    public Long getSecondPlaceId() {
        return getFamousPlaceId(secondPlace);
    }

    public Long getThirdPlaceId() {
        return getFamousPlaceId(thirdPlace);
    }
}