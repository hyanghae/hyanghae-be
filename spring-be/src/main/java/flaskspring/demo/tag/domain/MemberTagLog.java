package flaskspring.demo.tag.domain;

import flaskspring.demo.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MemberTagLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_tag_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static MemberTagLog createMemberTagLog(Member member, Tag tag) {
        MemberTagLog memberTagLog = new MemberTagLog();
        memberTagLog.member = member;
        memberTagLog.tag = tag;
        return memberTagLog;
    }
}
