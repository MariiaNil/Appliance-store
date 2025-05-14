package com.epam.rd.autocode.assessment.appliances.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // URL /images/categories/** будем искать и в classpath:/static/images/categories/,
        // и в локальной папке uploads/images/categories/
        registry.addResourceHandler("/images/categories/**")
                .addResourceLocations(
                        "classpath:/static/images/categories/",
                        "file:./uploads/images/categories/"
                );

        registry.addResourceHandler("/images/appliances/**")
                .addResourceLocations(
                        "classpath:/static/images/appliances/",
                        "file:./uploads/images/appliances/"
                );
    }
       /* // 1) чтобы статика из src/main/resources/static работала как прежде:
        registry
                .addResourceHandler("/css/**", "/js/**", "/images/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/", "classpath:/static/images/");

        // 2) а все запросы /images/categories/** дёргают из папки uploads:
        registry
                .addResourceHandler("/images/categories/**")
                .addResourceLocations("file:uploads/images/categories/");
    }*/
}
