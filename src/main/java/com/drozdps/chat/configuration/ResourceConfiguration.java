package com.drozdps.chat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class ResourceConfiguration extends WebMvcConfigurerAdapter {

	/*
	 *  Standard resource mapper
	 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webjars/**",
                "/js/**",
                "/img/**",
                "/css/**")
                .addResourceLocations(
                		"classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/static/js/");
    }

}