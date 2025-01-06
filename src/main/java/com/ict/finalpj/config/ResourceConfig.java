package com.ict.finalpj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ResourceConfig implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/upload/**")       // URL 경로
            .addResourceLocations("file:D:/upload/")       // 실제 경로
            .setCachePeriod(3600);                          // 캐시 시간 (초)
    }
    
}