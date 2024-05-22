package flaskspring.demo.config.jwt.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
    Optional<MemberRefreshToken> findByAccountAndReissueCountLessThan(String account, long count);
}