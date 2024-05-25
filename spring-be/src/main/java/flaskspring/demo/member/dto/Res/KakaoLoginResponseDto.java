package flaskspring.demo.member.dto.Res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KakaoLoginResponseDto {

    @Schema(description = "토큰 정보")
    HyanghaeToken hyanghaeToken;

    @Schema(description = "온보딩 여부", example = "ONBOARDING")
    UserStatus userStatus;


    public KakaoLoginResponseDto(String accessToken, String refreshToken, UserStatus userStatus) {
        hyanghaeToken = new HyanghaeToken(accessToken, refreshToken);
        this.userStatus = userStatus;
    }
}
