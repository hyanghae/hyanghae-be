package flaskspring.demo.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSortedPlaceName is a Querydsl query type for SortedPlaceName
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSortedPlaceName extends EntityPathBase<SortedPlaceName> {

    private static final long serialVersionUID = -908401502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSortedPlaceName sortedPlaceName = new QSortedPlaceName("sortedPlaceName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> imageScore = createNumber("imageScore", Double.class);

    public final QMember member;

    public final StringPath placeName = createString("placeName");

    public final NumberPath<Double> sum = createNumber("sum", Double.class);

    public final NumberPath<Double> tagScore = createNumber("tagScore", Double.class);

    public QSortedPlaceName(String variable) {
        this(SortedPlaceName.class, forVariable(variable), INITS);
    }

    public QSortedPlaceName(Path<? extends SortedPlaceName> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSortedPlaceName(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSortedPlaceName(PathMetadata metadata, PathInits inits) {
        this(SortedPlaceName.class, metadata, inits);
    }

    public QSortedPlaceName(Class<? extends SortedPlaceName> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

