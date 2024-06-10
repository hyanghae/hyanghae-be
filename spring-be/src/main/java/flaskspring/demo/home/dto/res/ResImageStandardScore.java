package flaskspring.demo.home.dto.res;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResImageStandardScore {
    private String name;
    private double score;

}