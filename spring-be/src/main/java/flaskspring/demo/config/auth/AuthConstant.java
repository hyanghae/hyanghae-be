package flaskspring.demo.config.auth;

import org.springframework.beans.factory.annotation.Value;

public class AuthConstant {

    public static final String TOKEN_EXPIRED_MESSAGE = "토큰이 만료되었습니다.";
    public static final String TOKEN_NOT_VALID_MESSAGE = "유효하지 않은 토큰입니다.";

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
            = {"/api/tag", "/api/feed/tag", "/api/departure", "/api/schedule/path", "/api/tag/all",
            "/api/mypage/main", "/api/place/detail/{placeId}", "/api/recommend/famous", "/api/recommend/rising", "/api/explore",
            "/api/recommend/{famousPlaceId}/similar", "/api/recommend/explore", "/api/place/detail/{placeId}",
            "/api/place/detail/{placeId}/similar", "/api/place/detail/{placeId}/similar/{famousPlaceId}",
            "/api/recommend/setting", "/api/search", "/search/city", "/api/search", "/api/search/rank", "/api/search/city",
            "/api/member/upload-image", "/api/member/saved"};

    // POST 메서드에 대한 인증이 필요한 경로
    public static final String[] POST_AUTH_BLACKLIST
            = {"/api/tag", "/api/like/{placeId}", "/api/departure", "/api/image", "/api/feed/image",
            "/api/on-boarding", "/api/recommend/setting", "/api/place/save"};

    // PUT 메서드에 대한 인증이 필요한 경로
    public static final String[] PUT_AUTH_BLACKLIST
            = {"/api/tag", "/api/auth/logout"};
    public static final String[] PATCH_AUTH_BLACKLIST
            = {"/api/auth/reissue",};

    // DELETE 메서드에 대한 인증이 필요한 경로
    public static final String[] DELETE_AUTH_BLACKLIST
            = {};

    // ADMIN 역할에 대한 인증이 필요한 경로
    public static final String[] ADMIN_POST_AUTH_BLACKLIST = {};

    public static final String[] ADMIN_PUT_AUTH_BLACKLIST = {};

    public static final String[] ADMIN_DELETE_AUTH_BLACKLIST = {};
}
