package com.moongcheap_backend.common.config;

import com.moongcheap_backend.common.security.SessionPrincipal;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    private static final String SESSION_SCHEME = "sessionCookie";

    @PostConstruct
    public void hideSessionPrincipal() {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(SessionPrincipal.class);
    }

    @Bean
    public OpenAPI moongcheapOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MoongCheap API")
                        .version("v1")
                        .description("MoongCheap 백엔드 API — 세션 기반 인증 (쿠키명 SID)"))
                .components(new Components()
                        .addSecuritySchemes(SESSION_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("SID")
                                .description("로그인 성공 시 발급되는 세션 쿠키")))
                .addTagsItem(new Tag().name("Social Login").description("소셜 로그인 (브라우저 리다이렉트)"))
                .paths(socialLoginPaths());
    }

    @Bean
    public OperationCustomizer sessionSecurityCustomizer() {
        return (operation, handlerMethod) -> {
            boolean requiresSession = Arrays.stream(handlerMethod.getMethodParameters())
                    .anyMatch(p -> p.getParameterType().equals(SessionPrincipal.class));
            if (requiresSession) {
                operation.addSecurityItem(new SecurityRequirement().addList(SESSION_SCHEME));
            }
            return operation;
        };
    }

    private Paths socialLoginPaths() {
        return new Paths()
                .addPathItem("/oauth2/authorization/kakao", oauthPathItem("카카오", "kakao"))
                .addPathItem("/oauth2/authorization/google", oauthPathItem("구글", "google"));
    }

    private PathItem oauthPathItem(String provider, String registrationId) {
        return new PathItem().get(new Operation()
                .addTagsItem("Social Login")
                .summary(provider + " 소셜 로그인")
                .description("FN-B01-01. "+provider + " OAuth2 로그인 페이지로 리다이렉트합니다. Swagger UI에서 직접 실행 불가 — 브라우저에서 URL을 직접 열어야 합니다.")
                .operationId("oauth2Login_" + registrationId)
                .responses(new ApiResponses()
                        .addApiResponse("302", new ApiResponse().description(provider + " 로그인 페이지로 리다이렉트"))));
    }
}
