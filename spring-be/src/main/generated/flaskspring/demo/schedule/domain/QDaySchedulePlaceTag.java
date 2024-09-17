package flaskspring.demo.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDaySchedulePlaceTag is a Querydsl query type for DaySchedulePlaceTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDaySchedulePlaceTag extends EntityPathBase<DaySchedulePlaceTag> {

    private static final long serialVersionUID = -2001810986L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDaySchedulePlaceTag daySchedulePlaceTag = new QDaySchedulePlaceTag("daySchedulePlaceTag");

    public final QDaySchedule daySchedule;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final flaskspring.demo.place.domain.QPlace place;

    public QDaySchedulePlaceTag(String variable) {
        this(DaySchedulePlaceTag.class, forVariable(variable), INITS);
    }

    public QDaySchedulePlaceTag(Path<? extends DaySchedulePlaceTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDaySchedulePlaceTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDaySchedulePlaceTag(PathMetadata metadata, PathInits inits) {
        this(DaySchedulePlaceTag.class, metadata, inits);
    }

    public QDaySchedulePlaceTag(Class<? extends DaySchedulePlaceTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.daySchedule = inits.isInitialized("daySchedule") ? new QDaySchedule(forProperty("daySchedule"), inits.get("daySchedule")) : null;
        this.place = inits.isInitialized("place") ? new flaskspring.demo.place.domain.QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

