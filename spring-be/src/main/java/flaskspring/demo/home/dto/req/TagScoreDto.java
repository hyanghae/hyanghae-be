package flaskspring.demo.home.dto.req;

import flaskspring.demo.tag.domain.BaseTagLog;
import flaskspring.demo.tag.domain.PlaceTagLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagScoreDto {

    @Builder.Default
    int tag1 = 0;
    @Builder.Default
    int tag2 = 0;
    @Builder.Default
    int tag3 = 0;
    @Builder.Default
    int tag4 = 0;
    @Builder.Default
    int tag5 = 0;
    @Builder.Default
    int tag6 = 0;
    @Builder.Default
    int tag7 = 0;
    @Builder.Default
    int tag8 = 0;
    @Builder.Default
    int tag9 = 0;
    @Builder.Default
    int tag10 = 0;
    @Builder.Default
    int tag11 = 0;
    @Builder.Default
    int tag12 = 0;
    @Builder.Default
    int tag13 = 0;
    @Builder.Default
    int tag14 = 0;
    @Builder.Default
    int tag15 = 0;
    @Builder.Default
    int tag16 = 0;
    @Builder.Default
    int tag17 = 0;
    @Builder.Default
    int tag18 = 0;
    @Builder.Default
    int tag19 = 0;
    @Builder.Default
    int tag20 = 0;
    @Builder.Default
    int tag21 = 0;
    @Builder.Default
    int tag22 = 0;
    @Builder.Default
    int tag23 = 0;
    @Builder.Default
    int tag24 = 0;


    // 공통 로직을 처리하는 private 메서드
    private void processTagLogs(List<? extends BaseTagLog> tagLogs) {
        for (BaseTagLog tagLog : tagLogs) {
            switch (tagLog.getTag().getTagName()) {
                case 액티비티:
                    this.tag1 = tagLog.getTagScore();
                    break;
                case 체험시설:
                    this.tag2 = tagLog.getTagScore();
                    break;
                case 산책:
                    this.tag3 = tagLog.getTagScore();
                    break;
                case 등산:
                    this.tag4 = tagLog.getTagScore();
                    break;
                case 숙박시설:
                    this.tag5 = tagLog.getTagScore();
                    break;
                case 해수욕장:
                    this.tag6 = tagLog.getTagScore();
                    break;
                case 계곡:
                    this.tag7 = tagLog.getTagScore();
                    break;
                case 공원:
                    this.tag8 = tagLog.getTagScore();
                    break;
                case 공연:
                    this.tag9 = tagLog.getTagScore();
                    break;
                case 거리:
                    this.tag10 = tagLog.getTagScore();
                    break;
                case 마을:
                    this.tag11 = tagLog.getTagScore();
                    break;
                case 예술:
                    this.tag12 = tagLog.getTagScore();
                    break;
                case 문화유적:
                    this.tag13 = tagLog.getTagScore();
                    break;
                case 박물관:
                    this.tag14 = tagLog.getTagScore();
                    break;
                case 자연유산:
                    this.tag15 = tagLog.getTagScore();
                    break;
                case 종교유적:
                    this.tag16 = tagLog.getTagScore();
                    break;
                case 맛집:
                    this.tag17 = tagLog.getTagScore();
                    break;
                case 카페:
                    this.tag18 = tagLog.getTagScore();
                    break;
                case 특산물:
                    this.tag19 = tagLog.getTagScore();
                    break;
                case 야시장:
                    this.tag20 = tagLog.getTagScore();
                    break;
                case 오션뷰:
                    this.tag21 = tagLog.getTagScore();
                    break;
                case 도시뷰:
                    this.tag22 = tagLog.getTagScore();
                    break;
                case 숲뷰:
                    this.tag23 = tagLog.getTagScore();
                    break;
                case 전망대:
                    this.tag24 = tagLog.getTagScore();
                    break;
                default:
                    // 다른 태그에 대한 처리 추가 가능
                    break;
            }
        }
    }


    // PlaceTagLog를 처리하는 생성자
    public TagScoreDto(List<? extends BaseTagLog> baseTagLogs) {
        processTagLogs(baseTagLogs);
    }

    public int getTagScore(int index) {
        switch (index) {
            case 1: return tag1;
            case 2: return tag2;
            case 3: return tag3;
            case 4: return tag4;
            case 5: return tag5;
            case 6: return tag6;
            case 7: return tag7;
            case 8: return tag8;
            case 9: return tag9;
            case 10: return tag10;
            case 11: return tag11;
            case 12: return tag12;
            case 13: return tag13;
            case 14: return tag14;
            case 15: return tag15;
            case 16: return tag16;
            case 17: return tag17;
            case 18: return tag18;
            case 19: return tag19;
            case 20: return tag20;
            case 21: return tag21;
            case 22: return tag22;
            case 23: return tag23;
            case 24: return tag24;
            default:
                throw new IllegalArgumentException("Invalid tag index: " + index);
        }
    }
}


