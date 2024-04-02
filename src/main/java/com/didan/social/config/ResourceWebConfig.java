package com.didan.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceWebConfig implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/uploads/","classpath:/templates/"}; // Khai báo đường dẫn tĩnh của các file resource
    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry){ // Hàm này sẽ cấu hình resource cho Spring Boot
        registry.addResourceHandler("/**") // đường dẫn tới các file resource từ client
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS) // Đường dẫn tĩnh của các file resource
                .setCachePeriod(3600); // Thời gian cache resource là 3600s
    }
}
