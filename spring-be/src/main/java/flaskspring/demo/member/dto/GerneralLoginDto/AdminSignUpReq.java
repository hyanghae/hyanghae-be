package flaskspring.demo.member.dto.GerneralLoginDto;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Schema(description = "어드민 회원가입 요청")
public class AdminSignUpReq {

    @NotBlank
    private String account;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    public Member toEntity() {

        return Member.builder()
                .account(this.account)
                .nickname("관리자")
                .password(this.password)
                .name(this.name)
                .role(Role.ADMIN)
                .build();
    }
}