package flaskspring.demo.departure.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeparturePoint is a Querydsl query type for DeparturePoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeparturePoint extends EntityPathBase<DeparturePoint> {

    private static final long serialVersionUID = -849151121L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeparturePoint departurePoint = new QDeparturePoint("departurePoint");

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final flaskspring.demo.place.domain.QLocation location;

    public final flaskspring.demo.member.domain.QMember member;

    public final StringPath roadAddress = createString("roadAddress");

    public QDeparturePoint(String variable) {
        this(DeparturePoint.class, forVariable(variable), INITS);
    }

    public QDeparturePoint(Path<? extends DeparturePoint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeparturePoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeparturePoint(PathMetadata metadata, PathInits inits) {
        this(DeparturePoint.class, metadata, inits);
    }

    public QDeparturePoint(Class<? extends DeparturePoint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new flaskspring.demo.place.domain.QLocation(forProperty("location")) : null;
        this.member = inits.isInitialized("member") ? new flaskspring.demo.member.domain.QMember(forProperty("member")) : null;
    }

}

