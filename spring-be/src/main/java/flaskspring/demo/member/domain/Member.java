package flaskspring.demo.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    private String account;
    private String password;
    private String email;

    private Long kakaoId;
    private String name;
    private String nickname;
    private String profileImage;

    private int registrationCount;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void increaseRegistrationCount() {
        this.registrationCount++;
    }

    public void decreaseRegistrationCount() {
        if (this.registrationCount > 0) {
            this.registrationCount--;
        }
    }
}