package flaskspring.demo.tag.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFamousPlaceTagLog is a Querydsl query type for FamousPlaceTagLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFamousPlaceTagLog extends EntityPathBase<FamousPlaceTagLog> {

    private static final long serialVersionUID = 1589308861L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFamousPlaceTagLog famousPlaceTagLog = new QFamousPlaceTagLog("famousPlaceTagLog");

    public final flaskspring.demo.place.domain.QFamousPlace famousPlace;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTag tag;

    public final NumberPath<Integer> tagScore = createNumber("tagScore", Integer.class);

    public QFamousPlaceTagLog(String variable) {
        this(FamousPlaceTagLog.class, forVariable(variable), INITS);
    }

    public QFamousPlaceTagLog(Path<? extends FamousPlaceTagLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFamousPlaceTagLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFamousPlaceTagLog(PathMetadata metadata, PathInits inits) {
        this(FamousPlaceTagLog.class, metadata, inits);
    }

    public QFamousPlaceTagLog(Class<? extends FamousPlaceTagLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.famousPlace = inits.isInitialized("famousPlace") ? new flaskspring.demo.place.domain.QFamousPlace(forProperty("famousPlace")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag"), inits.get("tag")) : null;
    }

}

