package com.nyx.musicPlatform.config;

import com.nyx.musicPlatform.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 
 * @author nyx
 * @since 2025-06
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 添加拦截器
     * 
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        // 静态资源
                        "/static/**",
                        "/public/**",
                        "/assets/**",
                        "/favicon.ico",
                        
                        // SpringDoc OpenAPI相关
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/doc.html",
                        
                        // 健康检查
                        "/actuator/**",
                        "/health",
                        "/info",
                        
                        // 错误页面
                        "/error"
                );
    }

    // CORS配置已移至SecurityConfig中，避免重复配置

    /**
     * 配置静态资源处理
     * 
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(false);
        
        // 配置音乐文件访问路径
        registry.addResourceHandler("/music/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/music_data/")
                .resourceChain(false);
    }
}