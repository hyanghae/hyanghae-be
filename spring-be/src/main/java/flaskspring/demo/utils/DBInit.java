package flaskspring.demo.utils;

import flaskspring.demo.travel.repository.PlaceTagLogRepository;
import flaskspring.demo.travel.repository.TagRepository;
import flaskspring.demo.travel.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBInit {

    private final TagRepository tagRepository;
    private final PlaceRepository PlaceRepository;
    private final PlaceTagLogRepository PlaceTagLogRepository;


    String[] sqlFiles = {"sql/init_place.sql", "sql/init_category_tag.sql"};


}

