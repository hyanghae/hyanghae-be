package flaskspring.demo.schedule.dto.res;

import flaskspring.demo.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResSchedule {
    @Schema(description = "스케쥴 ID", example = "1")
    private Long id;

    @Schema(description = "스케줄 명", example = "강릉 여행 스케줄")
    private String title;

    @Schema(description = "스케줄 이미지", example = "www.example.com")
    private String scheduleImgUrl;

    @Schema(description = "시작 일자", example = "2024-09-16")
    private LocalDate startDate;

    @Schema(description = "종료 일자", example = "2024-09-18")
    private LocalDate endDate;

    @Schema(description = "여행일 수", example = "3")
    private int dayCount;


}
