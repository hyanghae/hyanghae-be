package flaskspring.demo.tag.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberTagLog is a Querydsl query type for MemberTagLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberTagLog extends EntityPathBase<MemberTagLog> {

    private static final long serialVersionUID = -986657379L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberTagLog memberTagLog = new QMemberTagLog("memberTagLog");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final flaskspring.demo.member.domain.QMember member;

    public final QTag tag;

    public QMemberTagLog(String variable) {
        this(MemberTagLog.class, forVariable(variable), INITS);
    }

    public QMemberTagLog(Path<? extends MemberTagLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberTagLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberTagLog(PathMetadata metadata, PathInits inits) {
        this(MemberTagLog.class, metadata, inits);
    }

    public QMemberTagLog(Class<? extends MemberTagLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new flaskspring.demo.member.domain.QMember(forProperty("member")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag"), inits.get("tag")) : null;
    }

}

