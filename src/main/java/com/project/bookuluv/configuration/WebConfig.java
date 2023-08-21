package com.project.bookuluv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // FILE I/O
    @Value("${custom.resourcePath}")
    private String resourcePath;
    @Value("${custom.requestPath}")
    private String requestPath;

    // WebMvcConfigurer를 이용해서 이미지파일 업로드시 실시간 반영
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
        resourceHandlerRegistry.addResourceHandler(requestPath).addResourceLocations(resourcePath);
    }
}
