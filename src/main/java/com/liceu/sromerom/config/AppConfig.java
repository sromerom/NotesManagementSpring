package com.liceu.sromerom.config;

import com.liceu.sromerom.interceptors.CheckCsrfTokenFilter;
import com.liceu.sromerom.interceptors.GenerateCsrfTokenInterceptor;
import com.liceu.sromerom.interceptors.LoginInterceptor;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.io.IOException;
import java.util.Properties;


@Configuration
@EnableWebMvc
@ComponentScan("com.liceu.sromerom")
public class AppConfig implements WebMvcConfigurer {

    final LoginInterceptor loginInterceptor;
    final GenerateCsrfTokenInterceptor generateCsrfTokenInterceptor;
    final CheckCsrfTokenFilter checkCsrfTokenFilter;

    public AppConfig(LoginInterceptor loginInterceptor, GenerateCsrfTokenInterceptor generateCsrfTokenInterceptor, CheckCsrfTokenFilter checkCsrfTokenFilter) {
        this.loginInterceptor = loginInterceptor;
        this.generateCsrfTokenInterceptor = generateCsrfTokenInterceptor;
        this.checkCsrfTokenFilter = checkCsrfTokenFilter;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //Interceptor Auth/login
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");

        //Interceptor CSRF
        registry.addInterceptor(generateCsrfTokenInterceptor).addPathPatterns("/**");
        registry.addInterceptor(checkCsrfTokenFilter).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public UrlBasedViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Bean(name = "application.properties")
    public Properties getProperties() throws IOException {
        PropertiesFactoryBean factory = new PropertiesFactoryBean();
        factory.setLocation(new ClassPathResource("application.properties"));
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}
