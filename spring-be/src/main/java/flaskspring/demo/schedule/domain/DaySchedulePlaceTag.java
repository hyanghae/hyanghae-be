package flaskspring.demo.schedule.domain;

import flaskspring.demo.place.domain.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedulePlaceTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_place_tag_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_schedule_id")
    private DaySchedule daySchedule;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;


    public static DaySchedulePlaceTag create(DaySchedule daySchedule, Place place){
        DaySchedulePlaceTag daySchedulePlaceTag = DaySchedulePlaceTag.builder()
                .daySchedule(daySchedule)
                .place(place)
                .build();

        return daySchedulePlaceTag;
    }

}
