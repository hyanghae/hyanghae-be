package flaskspring.demo.config.auth;

public class AuthConstant {

    // 인증이 필요하지 않은 경로
    public static final String[] AUTH_WHITELIST = {
            "/api/**", "/graphiql", "/graphql",
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };

    // 인증이 필요한 경로
    public static final String[] AUTH_BLACKLIST = {

    };

    // GET 메서드에 대한 인증이 필요한 경로
    public static final String[] GET_AUTH_BLACKLIST
            = {"/api/tag"};

    // POST 메서드에 대한 인증이 필요한 경로
    public static final String[] POST_AUTH_BLACKLIST
            = {"/api/tag"};

    // PUT 메서드에 대한 인증이 필요한 경로
    public static final String[] PUT_AUTH_BLACKLIST
            = {"/api/tag"};

    // DELETE 메서드에 대한 인증이 필요한 경로
    public static final String[] DELETE_AUTH_BLACKLIST
            = {};

    // ADMIN 역할에 대한 인증이 필요한 경로
    public static final String[] ADMIN_POST_AUTH_BLACKLIST = {};

    public static final String[] ADMIN_PUT_AUTH_BLACKLIST = {};

    public static final String[] ADMIN_DELETE_AUTH_BLACKLIST = {};
}
