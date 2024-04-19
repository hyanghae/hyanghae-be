package flaskspring.demo.member.dto.Req;


import flaskspring.demo.member.domain.Member;
import flaskspring.demo.member.domain.Role;
import flaskspring.demo.member.dto.kakaoLoginDto.KakaoProfile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //컨트롤러에서 사용 안 됌.
public class KakaoLoginRequestDto {
    private String account;

    private String nickname;

    private String password;

    private String name;

    private String profileImageFileName;

    private Long kakaoIdentifier;

    private String email;

    public KakaoLoginRequestDto(KakaoProfile profile, String uniqueNickname) {
        this.account = profile.getProperties().getNickname() + profile.getId(); //닉네임과 카카오 식별자를 가지고 임의로 account를 만듦.
        this.nickname = uniqueNickname;
        this.password = "password";
        this.name = profile.getProperties().getNickname();
        this.profileImageFileName = profile.getProperties().getProfile_image();
        this.kakaoIdentifier = profile.getId();
        this.email = profile.getKakao_account().getEmail();
    }

    public Member toEntity() {
        return Member.builder()
                .account(this.account)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .name(this.name)
                .kakaoId(this.kakaoIdentifier)
                .role(Role.USER)
                .build();
    }


}
