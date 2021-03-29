package com.vlad.code.sample.config;

import com.vlad.code.sample.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiDetails() {
        return new ApiInfoBuilder()
                .title("Code Sample Application API")
                .description("Documentation of the API for the Code Sample application")
                .version("1.0")
                .license("Free to use")
                .contact(new Contact("Vlad", "http://com.vlad.code.sample", "vlad@email.com"))
                .build();
    }

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/users/*"))
                .apis(RequestHandlerSelectors.basePackage(Application.class.getPackage().getName()))
                .build()
                .apiInfo(apiDetails());
    }
}
