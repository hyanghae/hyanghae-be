package flaskspring.demo.like.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceLike is a Querydsl query type for PlaceLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceLike extends EntityPathBase<PlaceLike> {

    private static final long serialVersionUID = -504258802L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceLike placeLike = new QPlaceLike("placeLike");

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final flaskspring.demo.member.domain.QMember member;

    public final flaskspring.demo.place.domain.QPlace place;

    public QPlaceLike(String variable) {
        this(PlaceLike.class, forVariable(variable), INITS);
    }

    public QPlaceLike(Path<? extends PlaceLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceLike(PathMetadata metadata, PathInits inits) {
        this(PlaceLike.class, metadata, inits);
    }

    public QPlaceLike(Class<? extends PlaceLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new flaskspring.demo.member.domain.QMember(forProperty("member")) : null;
        this.place = inits.isInitialized("place") ? new flaskspring.demo.place.domain.QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

