package flaskspring.demo.place.dto.res;

import flaskspring.demo.departure.dto.res.ResSchedulePlace;
import flaskspring.demo.place.domain.FamousPlace;
import flaskspring.demo.place.domain.Place;
import lombok.Data;

import java.util.List;

@Data
public class ResPlaceDetail {
    private Long placeId;
    private String placeName;
    private String placeImgUrl;
    private ResFamousPlace simFamousPlace;
    private int totalSimScore;
    private List<ResTagSim> tagList;
    private double mapX;
    private double mapY;
    private String placeRoadAddress;
    private List<ResSchedulePlace> nearByPlaces;

    public ResPlaceDetail(Place place, FamousPlace famousPlace, int totalSimScore, List<ResTagSim> tagSims) {
        this.placeId = place.getId();
        this.placeName = place.getTouristSpotName();
        this.placeImgUrl = place.getImagePath();
        this.simFamousPlace = new ResFamousPlace(famousPlace);
        this.totalSimScore = totalSimScore;
        this.tagList = tagSims;
        this.mapX = place.getLocation().getMapX();
        this.mapY = place.getLocation().getMapY();;
        this.placeRoadAddress = place.getRoadAddress();
        this.nearByPlaces = null;
    }
}