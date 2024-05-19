package flaskspring.demo.member.dto.GerneralLoginDto;

import flaskspring.demo.member.dto.Res.UserStauts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Schema(description = "일반 테스트 로그인 응답")
public class GeneralLoginRes {

    @Schema(description = "JWT 인증 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QWNjb3VudCIsImFjY291bnQiOiJ0ZXN0QWNjb3VudCIsImlhdCI6MTcxMDIyMTI1MCwiZXhwIjoxNzEwODI2MDUwfQ.wpMIUytr8MpqxGpFAJIlF8kG9OSm2KJE7xeUWQHVnAU")
    String token;

    @Schema(description = "온보딩 여부", example = "NOT_ONBOARDED")
    UserStauts userStauts;


    public GeneralLoginRes(String token,  UserStauts userStauts) {
        this.token = token;
        this.userStauts = userStauts;
    }
}
