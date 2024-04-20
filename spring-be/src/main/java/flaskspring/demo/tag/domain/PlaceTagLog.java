package flaskspring.demo.tag.domain;


import flaskspring.demo.travel.domain.Place;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class PlaceTagLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_tag_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PlaceTagLog createPlaceTagLog(Place Place, Tag tag) {
        return PlaceTagLog.builder()
                .place(Place)
                .tag(tag)
                .build();
    }
}
