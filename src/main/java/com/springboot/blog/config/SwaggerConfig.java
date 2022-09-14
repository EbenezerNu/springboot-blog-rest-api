package com.springboot.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo(){
        return new ApiInfo("Spring Boot Blog REST API",
                "Spring Boot Blog REST API Documentation",
                "1",
                "Terms of Service",
                new Contact("Ebenezer Nuamah", "www.github.com/Ebenezernu", "ebenebe1997@gmail.com"),
                "License",
                "License URL",
                Collections.emptyList()
                );
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
