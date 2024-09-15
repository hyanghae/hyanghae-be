package flaskspring.demo.place.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFamousPlace is a Querydsl query type for FamousPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFamousPlace extends EntityPathBase<FamousPlace> {

    private static final long serialVersionUID = -1751310138L;

    public static final QFamousPlace famousPlace = new QFamousPlace("famousPlace");

    public final StringPath city = createString("city");

    public final StringPath enCityName = createString("enCityName");

    public final NumberPath<Integer> hashTagCount = createNumber("hashTagCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath majorCategory = createString("majorCategory");

    public final StringPath region = createString("region");

    public final StringPath roadAddress = createString("roadAddress");

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    public final StringPath subCategory = createString("subCategory");

    public final StringPath touristSpotName = createString("touristSpotName");

    public QFamousPlace(String variable) {
        super(FamousPlace.class, forVariable(variable));
    }

    public QFamousPlace(Path<? extends FamousPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFamousPlace(PathMetadata metadata) {
        super(FamousPlace.class, metadata);
    }

}

