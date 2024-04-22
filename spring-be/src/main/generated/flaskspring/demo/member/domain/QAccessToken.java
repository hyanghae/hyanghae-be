package flaskspring.demo.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccessToken is a Querydsl query type for AccessToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccessToken extends EntityPathBase<AccessToken> {

    private static final long serialVersionUID = 398809122L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccessToken accessToken = new QAccessToken("accessToken");

    public final BooleanPath blacklisted = createBoolean("blacklisted");

    public final DateTimePath<java.util.Date> expirationDate = createDateTime("expirationDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath token = createString("token");

    public QAccessToken(String variable) {
        this(AccessToken.class, forVariable(variable), INITS);
    }

    public QAccessToken(Path<? extends AccessToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccessToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccessToken(PathMetadata metadata, PathInits inits) {
        this(AccessToken.class, metadata, inits);
    }

    public QAccessToken(Class<? extends AccessToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

