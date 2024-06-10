package flaskspring.demo.home.dto.res;

import lombok.Data;

@Data
public class PlaceScore {
    private String name;
    private Double tagScore;
    private Double imageScore;
    private Double sum;

    public PlaceScore(String name, Double tagScore, Double imageScore) {
        this.name = name;
        this.tagScore = tagScore;
        this.imageScore = imageScore;
        this.sum = (tagScore != null ? tagScore : 0.0) + (imageScore != null ? imageScore : 0.0);
    }


    public void addImageScore(double imageScore) {
        this.imageScore = imageScore;
        sum += imageScore;
    }
}