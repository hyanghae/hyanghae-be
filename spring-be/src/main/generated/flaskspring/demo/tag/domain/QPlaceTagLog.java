package flaskspring.demo.tag.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceTagLog is a Querydsl query type for PlaceTagLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceTagLog extends EntityPathBase<PlaceTagLog> {

    private static final long serialVersionUID = 1516897528L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceTagLog placeTagLog = new QPlaceTagLog("placeTagLog");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final flaskspring.demo.place.domain.QPlace place;

    public final QTag tag;

    public final NumberPath<Integer> tagScore = createNumber("tagScore", Integer.class);

    public QPlaceTagLog(String variable) {
        this(PlaceTagLog.class, forVariable(variable), INITS);
    }

    public QPlaceTagLog(Path<? extends PlaceTagLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceTagLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceTagLog(PathMetadata metadata, PathInits inits) {
        this(PlaceTagLog.class, metadata, inits);
    }

    public QPlaceTagLog(Class<? extends PlaceTagLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new flaskspring.demo.place.domain.QPlace(forProperty("place"), inits.get("place")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag"), inits.get("tag")) : null;
    }

}

