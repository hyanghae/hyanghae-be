package flaskspring.demo.register.domain;

import flaskspring.demo.like.domain.PlaceLike;
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
public class PlaceRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_register_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    public static PlaceRegister createRegister(Member member, Place place) {
        return PlaceRegister.builder()
                .member(member)
                .place(place)
                .build();
    }
}
