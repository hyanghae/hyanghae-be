package flaskspring.demo.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDaySchedule is a Querydsl query type for DaySchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDaySchedule extends EntityPathBase<DaySchedule> {

    private static final long serialVersionUID = -2141209693L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDaySchedule daySchedule = new QDaySchedule("daySchedule");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSchedule schedule;

    public QDaySchedule(String variable) {
        this(DaySchedule.class, forVariable(variable), INITS);
    }

    public QDaySchedule(Path<? extends DaySchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDaySchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDaySchedule(PathMetadata metadata, PathInits inits) {
        this(DaySchedule.class, metadata, inits);
    }

    public QDaySchedule(Class<? extends DaySchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

