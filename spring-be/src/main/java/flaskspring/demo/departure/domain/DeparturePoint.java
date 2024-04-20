package flaskspring.demo.departure.domain;

import flaskspring.demo.departure.dto.req.ReqDeparture;
import flaskspring.demo.member.domain.Member;
import flaskspring.demo.travel.domain.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeparturePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roadAddress;

    @Embedded
    private Location location;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdTime;

    public static DeparturePoint create(ReqDeparture req, Member member) {
        return DeparturePoint.builder()
                .roadAddress(req.getRoadAddress())
                .location(new Location(req.getMapX(), req.getMapY()))
                .member(member)
                .build();
    }
}
