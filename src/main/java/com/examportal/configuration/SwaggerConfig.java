package com.examportal.configuration;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${swagger_enabled}")
    private boolean isSwaggerEnabled;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.examportal.controller"))
                .paths(PathSelectors.any()).build().apiInfo(apiInfo())
                .enable(isSwaggerEnabled);
    }

    public ApiInfo apiInfo() {
        return new ApiInfo("User Service API specification", "User Service API specification", "v1.1.0", "https://www.ex"
                new Contact("", "", ""), "License of API", "https://examportal/en/legal", Collections.emptyList());
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        if (isSwaggerEnabled) {
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
