package flaskspring.demo.place.register.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceRegister is a Querydsl query type for PlaceRegister
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceRegister extends EntityPathBase<PlaceRegister> {

    private static final long serialVersionUID = 1375773863L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceRegister placeRegister = new QPlaceRegister("placeRegister");

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final flaskspring.demo.member.domain.QMember member;

    public final flaskspring.demo.place.domain.QPlace place;

    public QPlaceRegister(String variable) {
        this(PlaceRegister.class, forVariable(variable), INITS);
    }

    public QPlaceRegister(Path<? extends PlaceRegister> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceRegister(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceRegister(PathMetadata metadata, PathInits inits) {
        this(PlaceRegister.class, metadata, inits);
    }

    public QPlaceRegister(Class<? extends PlaceRegister> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new flaskspring.demo.member.domain.QMember(forProperty("member")) : null;
        this.place = inits.isInitialized("place") ? new flaskspring.demo.place.domain.QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

