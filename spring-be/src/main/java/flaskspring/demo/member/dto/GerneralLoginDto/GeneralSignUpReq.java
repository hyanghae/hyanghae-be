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
@Schema(description = "일반 회원가입 요청")
public class GeneralSignUpReq {

    @NotBlank
    @Schema(description = "계정", example = "test1@gmail.com")
    private String account;

    @NotBlank
    @Schema(description = "비밀번호", example = "testPassword1")
    private String password;

    @NotBlank
    @Schema(description = "이름", example = "김김김")
    private String name;

    public Member toEntity() {
        return Member.builder()
                .account(this.account)
                .password(this.password)
                .name(this.name)
                .role(Role.USER)
                .build();
    }

}
