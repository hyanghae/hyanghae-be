package flaskspring.demo.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {@Server(url = "/", description = "Default Server URL")},
        info = @Info(
                title = "향해 API 명세서",
                description = "향해 API 명세서입니다",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    // 운영 환경에는 Swagger를 비활성화하기 위해 추가했습니다.
    public OpenAPI api() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }

   /* @Bean
    public OperationCustomizer globalHeader() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema().name("Refresh-Token"))
                    .name("Refresh-Token"));
            return operation;
        };
    }*/

}
