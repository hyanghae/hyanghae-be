package flaskspring.demo.member.dto.Res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResReIssue {

    @Schema(description = "토큰 정보")
    HyanghaeToken hyanghaeToken;

    public ResReIssue(String accessToken, String refreshToken) {
        hyanghaeToken = new HyanghaeToken(accessToken, refreshToken);
    }
}
