package com.fibo.rule.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private SessionInterceptor sessionInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //  添加拦截器
        registry.addInterceptor(sessionInterceptor)
                .excludePathPatterns("")  //  排除拦截器要拦截的路径
                .excludePathPatterns("/operations")
                .excludePathPatterns("/swagger")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/configuration/ui")
                .excludePathPatterns("/swagger-resources")
                .excludePathPatterns("/configuration/security")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/error")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/configuration/ui")
                .excludePathPatterns("/swagger-resources/configuration/security");
//                .addPathPatterns("/**");    //  添加拦截器需要要拦截的路径
    }

}
