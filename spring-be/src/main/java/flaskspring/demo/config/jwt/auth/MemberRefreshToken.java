package flaskspring.demo.config.jwt.auth;

import flaskspring.demo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberRefreshToken {
    @Id
    private Long memberId;

    private String account;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    private String refreshToken;
    private int reissueCount = 0;

    public MemberRefreshToken(Member member, String refreshToken) {
        this.member = member;
        this.account = member.getAccount();
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.reissueCount = 0;
    }

    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void increaseReissueCount() {
        reissueCount++;
    }
}
