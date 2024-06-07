package flaskspring.demo.image.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUploadImage is a Querydsl query type for UploadImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUploadImage extends EntityPathBase<UploadImage> {

    private static final long serialVersionUID = 1772081408L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUploadImage uploadImage = new QUploadImage("uploadImage");

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    public final StringPath extension = createString("extension");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isSetting = createBoolean("isSetting");

    public final flaskspring.demo.member.domain.QMember member;

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath savedImageUrl = createString("savedImageUrl");

    public final StringPath saveFileName = createString("saveFileName");

    public QUploadImage(String variable) {
        this(UploadImage.class, forVariable(variable), INITS);
    }

    public QUploadImage(Path<? extends UploadImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUploadImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUploadImage(PathMetadata metadata, PathInits inits) {
        this(UploadImage.class, metadata, inits);
    }

    public QUploadImage(Class<? extends UploadImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new flaskspring.demo.member.domain.QMember(forProperty("member")) : null;
    }

}

