package flaskspring.demo.place.repository;

import flaskspring.demo.place.domain.Place;
import lombok.Data;

@Data
public class PlaceDTO {
    private Place place;
    private String tagIds;
    private String tagNames;
    private boolean isRegistered;

    // Getters and setters
}
