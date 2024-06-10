package flaskspring.demo.member.domain;

import flaskspring.demo.home.dto.res.PlaceScore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SortedPlaceName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String placeName;

    private Double tagScore;
    private Double imageScore;
    private Double sum;

    public static SortedPlaceName create(Member member, PlaceScore placeScore) {
        return SortedPlaceName.builder()
                .member(member)
                .placeName(placeScore.getName())
                .tagScore(placeScore.getTagScore())
                .imageScore(placeScore.getImageScore())
                .sum(placeScore.getSum())
                .build();
    }
}