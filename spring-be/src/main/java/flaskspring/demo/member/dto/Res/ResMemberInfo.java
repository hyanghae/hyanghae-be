package flaskspring.demo.member.dto.Res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResMemberInfo {
    @Schema(description = "멤버 ID", example = "1")
    private Long memberId;

    @Schema(description = "계정", example = "testAccount")
    private String account;

    @Schema(description = "이메일", example = "treesheep@naver.com")
    private String email;

    @Schema(description = "카카오 ID", example = "")
    private Long kakaoId;

    @Schema(description = "이름", example = "이원준")
    private String name;



}
