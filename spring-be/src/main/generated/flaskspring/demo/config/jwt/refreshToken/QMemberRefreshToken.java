package flaskspring.demo.config.jwt.refreshToken;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberRefreshToken is a Querydsl query type for MemberRefreshToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberRefreshToken extends EntityPathBase<MemberRefreshToken> {

    private static final long serialVersionUID = -1221836622L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberRefreshToken memberRefreshToken = new QMemberRefreshToken("memberRefreshToken");

    public final StringPath account = createString("account");

    public final flaskspring.demo.member.domain.QMember member;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final NumberPath<Integer> reissueCount = createNumber("reissueCount", Integer.class);

    public QMemberRefreshToken(String variable) {
        this(MemberRefreshToken.class, forVariable(variable), INITS);
    }

    public QMemberRefreshToken(Path<? extends MemberRefreshToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberRefreshToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberRefreshToken(PathMetadata metadata, PathInits inits) {
        this(MemberRefreshToken.class, metadata, inits);
    }

    public QMemberRefreshToken(Class<? extends MemberRefreshToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new flaskspring.demo.member.domain.QMember(forProperty("member")) : null;
    }

}

