package com.skafld.sample.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI(final Info apiInfo) {
        return new OpenAPI()
            .info(apiInfo);
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder().group("sample")
            .packagesToScan("com.skafld.sample.api")
            .build();
    }

    @Bean
    public Info apiInfo(
        @Value("${module.version}") final String version,
        @Value("${module.terms-of-service}") final String termOfService,
        final Contact contact) {
        return new Info()
            .title("sample API")
            .version(version)
            .contact(contact)
            .termsOfService(termOfService);
    }

    @Bean
    public Contact contact(
        @Value("${module.name}") final String name,
        @Value("${module.url}") final String url,
        @Value("${module.email}") final String email) {
        return new Contact()
            .name(name)
            .url(url)
            .email(email);
    }
}
