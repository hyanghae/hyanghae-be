package flaskspring.demo.schedule.domain;

import flaskspring.demo.departure.dto.res.ResDeparture;
import flaskspring.demo.place.domain.Location;
import flaskspring.demo.schedule.dto.req.ReqDeparture;
import flaskspring.demo.schedule.dto.res.ResDepartureDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Departure {

    private String placeName;

    private String roadAddress;

    private Location location;

    public static Departure create(ReqDeparture reqDeparture){

        Departure departure = new Departure();
        departure.placeName = reqDeparture.getPlaceName();
        departure.roadAddress = reqDeparture.getRoadAddress();
        departure.location = new Location(reqDeparture.getMapX(), reqDeparture.getMapY());
        return departure;
    }

    public ResDepartureDto toDto(){
        return ResDepartureDto.builder()
                .placeName(placeName)
                .roadAddress(roadAddress)
                .mapX(location.getMapX())
                .mapY(location.getMapY())
                .build();
    }
}
