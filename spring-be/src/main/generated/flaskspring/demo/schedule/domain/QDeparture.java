package flaskspring.demo.schedule.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeparture is a Querydsl query type for Departure
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDeparture extends BeanPath<Departure> {

    private static final long serialVersionUID = 840088132L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeparture departure = new QDeparture("departure");

    public final flaskspring.demo.place.domain.QLocation location;

    public final StringPath placeName = createString("placeName");

    public final StringPath roadAddress = createString("roadAddress");

    public QDeparture(String variable) {
        this(Departure.class, forVariable(variable), INITS);
    }

    public QDeparture(Path<? extends Departure> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeparture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeparture(PathMetadata metadata, PathInits inits) {
        this(Departure.class, metadata, inits);
    }

    public QDeparture(Class<? extends Departure> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new flaskspring.demo.place.domain.QLocation(forProperty("location")) : null;
    }

}

