package org.MY_APP.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private TermsInterceptor termsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(
                        "/upload",
                        "/quiz-selection",
                        "/quiz1",
                        "/quiz2",
                        "/history",
                        "/sentiment"
                );

        registry.addInterceptor(termsInterceptor)
                .addPathPatterns("/**");
    }

    //  ΕΔΩ είναι το καινούργιο κομμάτι για τα αρχεία
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:src/main/resources/static/uploads/"
                );
    }
}