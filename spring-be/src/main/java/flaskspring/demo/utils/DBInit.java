package flaskspring.demo.utils;

import flaskspring.demo.tag.repository.PlaceTagLogRepository;
import flaskspring.demo.tag.repository.TagRepository;
import flaskspring.demo.travel.repository.PlaceRepository;
import flaskspring.demo.utils.sql.SQLFileExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBInit {

    private final TagRepository tagRepository;
    private final PlaceRepository PlaceRepository;
    private final PlaceTagLogRepository PlaceTagLogRepository;
    private final SQLFileExecutor sqlFileExecutor;

    String[] sqlFiles = {"sql/init_place.sql", "sql/init_tag.sql"};


}

