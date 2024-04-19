package flaskspring.demo.like.domain;

import flaskspring.demo.member.domain.Member;
import flaskspring.demo.travel.domain.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;


    public static PlaceLike createLike(Member member, Place place) {
        return PlaceLike.builder()
                .member(member)
                .place(place)
                .build();
    }
}
