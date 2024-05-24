package com.vicheak.mbankingapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {

    @Value("${resource.server-path}")
    private String resourceServerPath;

    @Value("${resource.client-path}")
    private String resourceClientPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceClientPath)
                .addResourceLocations("classpath:" + resourceServerPath);
    }

}
