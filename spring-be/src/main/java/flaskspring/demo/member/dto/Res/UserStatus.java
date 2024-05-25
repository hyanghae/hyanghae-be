package flaskspring.demo.member.dto.Res;

public enum UserStatus {
    ONBOARDING, // 온보딩 안 한 경우
    LOGINED, // 온보딩 하여 로그인 한 경우
    DEACTIVATED // 탈퇴
}