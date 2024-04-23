package flaskspring.demo.like.domain;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.travel.domain.Place;
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
public class PlaceLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTime;

    public static PlaceLike createLike(Member member, Place place) {
        return PlaceLike.builder()
                .member(member)
                .place(place)
                .build();
    }
}
