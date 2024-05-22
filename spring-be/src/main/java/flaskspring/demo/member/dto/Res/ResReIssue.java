package flaskspring.demo.member.dto.Res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResReIssue {

    @Schema(description = "JWT 인증 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QWNjb3VudCIsImFjY291bnQiOiJ0ZXN0QWNjb3VudCIsImlhdCI6MTcxMDIyMTI1MCwiZXhwIjoxNzEwODI2MDUwfQ.wpMIUytr8MpqxGpFAJIlF8kG9OSm2KJE7xeUWQHVnAU")
    String token;

    @Schema(description = "리프레시 토큰", example = "eyJzdWIiOiJ0ZXN0QWNjb3VudCIsImFjY291bnQiOiJ0ZXN0QWNjb3VudCIsImlhdCI6MTcxMDIyMTI1MCwiZXhwIjoxNzEwODI2MDUwfQ")
    String refreshToken;

    public ResReIssue(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
