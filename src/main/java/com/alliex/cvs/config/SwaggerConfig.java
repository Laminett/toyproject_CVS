package com.alliex.cvs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private String version;

    private String title;

    @Bean
    public Docket apiV1() {
        version = "V1";
        title = "CVS API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .apiInfo(apiInfo(title, version));
    }

    private ApiInfo apiInfo(String title, String version) {

        return new ApiInfo(
                title,
                "Swagger generate API",
                version,
                "cvs.alli-ex.com",
                new Contact("Contact us", "cvs.alli-ex.com", "mobilediv@alli-ex.com"),
                "Licenses",
                "cvs.alli-ex.com",
                new ArrayList<>());
    }

}
