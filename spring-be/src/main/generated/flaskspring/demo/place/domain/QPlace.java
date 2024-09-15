package flaskspring.demo.place.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlace is a Querydsl query type for Place
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlace extends EntityPathBase<Place> {

    private static final long serialVersionUID = 843276929L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlace place = new QPlace("place");

    public final StringPath city = createString("city");

    public final StringPath engCityName = createString("engCityName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final QLocation location;

    public final StringPath majorCategory = createString("majorCategory");

    public final StringPath region = createString("region");

    public final NumberPath<Long> registerCount = createNumber("registerCount", Long.class);

    public final StringPath roadAddress = createString("roadAddress");

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    public final StringPath subCategory = createString("subCategory");

    public final StringPath touristSpotName = createString("touristSpotName");

    public QPlace(String variable) {
        this(Place.class, forVariable(variable), INITS);
    }

    public QPlace(Path<? extends Place> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlace(PathMetadata metadata, PathInits inits) {
        this(Place.class, metadata, inits);
    }

    public QPlace(Class<? extends Place> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location")) : null;
    }

}

