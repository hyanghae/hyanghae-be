package flaskspring.demo.member.domain;

import flaskspring.demo.config.hello.dto.req.ReqAgreement;
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

    private boolean isOnboarded; //온보딩 여부 (앱 처음 시작시 무조건 true)
    private boolean recommendPossible; //설정 정보 있는지 여부 -> 추천 가능 여부


    @Column(columnDefinition = "INT DEFAULT 0")
    private int registrationCount;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean refreshNeeded;

    private boolean serviceTerms;
    private boolean privacyTerms;
    private boolean locationTerms;
    private boolean ageTerms;
    private boolean marketingTerms;

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
    public boolean isRequiredTermsAgreed(){
        return this.serviceTerms;
    }

    public void onBoard() {
        this.isOnboarded = true;
    }
    public void canRecommend() {
        this.recommendPossible = true;
    }
    public void canNotRecommend() {
        this.recommendPossible = false;
    }

    public void initStatus() {
        this.isOnboarded = false;
    }

    public void setRefreshNeeded() {
        this.refreshNeeded = true;
    }
    public void setRefreshNotNeeded() {
        this.refreshNeeded = false;
    }

    public void updateTermsAgreement(ReqAgreement reqAgreement){
        this.serviceTerms = reqAgreement.isServiceTerms();
        this.privacyTerms = reqAgreement.isPrivacyTerms();
        this.locationTerms = reqAgreement.isLocationTerms();
        this.ageTerms = reqAgreement.isAgeTerms();
        this.marketingTerms = reqAgreement.isMarketingTerms();
    }


}