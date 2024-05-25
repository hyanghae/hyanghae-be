package flaskspring.demo.member.dto.GerneralLoginDto;

import flaskspring.demo.member.dto.Res.HyanghaeToken;
import flaskspring.demo.member.dto.Res.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Schema(description = "일반 테스트 로그인 응답")
public class GeneralLoginRes {

    @Schema(description = "토큰 정보")
    HyanghaeToken hyanghaeToken;

    @Schema(description = "온보딩 여부", example = "ONBOARDING")
    UserStatus userStatus;


    public GeneralLoginRes(String accessToken, String refreshToken, UserStatus userStatus) {
        hyanghaeToken = new HyanghaeToken(accessToken, refreshToken);
        this.userStatus = userStatus;
    }
}
