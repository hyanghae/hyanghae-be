package flaskspring.demo.config.hello.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResHello {

    @Schema(description = "업데이트 버전", example = "1.1.4")
    private String updateVersion;

    @Schema(description = "강업 버전", example = "1.1.4")
    private String forceUpdateVersion;

    @Schema(description = "온보딩 필요 여부", example = "false")
    private boolean needToOnboarding;

    @Schema(description = "약관 동의 필요 여부", example = "false")
    private boolean needToAgreement;

}
