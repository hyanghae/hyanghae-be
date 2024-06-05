package flaskspring.demo.home.dto.req;

import flaskspring.demo.tag.domain.PlaceTagLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagScoreDto {

    Integer tag1 = 0;
    Integer tag2 = 0;
    Integer tag3 = 0;
    Integer tag4 = 0;
    Integer tag5 = 0;
    Integer tag6 = 0;
    Integer tag7 = 0;
    Integer tag8 = 0;
    Integer tag9 = 0;
    Integer tag10 = 0;
    Integer tag11 = 0;
    Integer tag12 = 0;
    Integer tag13 = 0;
    Integer tag14 = 0;
    Integer tag15 = 0;
    Integer tag16 = 0;
    Integer tag17 = 0;
    Integer tag18 = 0;
    Integer tag19 = 0;
    Integer tag20 = 0;
    Integer tag21 = 0;
    Integer tag22 = 0;
    Integer tag23 = 0;
    Integer tag24 = 0;

    public TagScoreDto(List<PlaceTagLog> tagLogs) {
        for (PlaceTagLog tagLog : tagLogs) {
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
}

