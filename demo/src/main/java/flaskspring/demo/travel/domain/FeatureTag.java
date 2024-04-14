package flaskspring.demo.travel.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class FeatureTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FeatureName name;

    private int score;
}