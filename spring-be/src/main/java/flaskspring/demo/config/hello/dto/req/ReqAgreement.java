package flaskspring.demo.config.hello.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "약관 동의 여부 요청")
@Data
public class ReqAgreement {

    private boolean serviceTerms;
    private boolean privacyTerms;
    private boolean locationTerms;
    private boolean ageTerms;
    private boolean marketingTerms;
}
