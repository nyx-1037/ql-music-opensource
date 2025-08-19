package com.musicmanager.framework.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat配置
 * 解决FileCountLimitExceededException问题
 * 
 * @author musicmanager
 */
@Configuration
public class TomcatConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        
        tomcat.addConnectorCustomizers((Connector connector) -> {
            // 设置最大参数数量，解决FileCountLimitExceededException
            connector.setProperty("maxParameterCount", "50000");
            
            // 设置最大HTTP表单POST大小
            connector.setProperty("maxHttpFormPostSize", "62914560"); // 60MB
            
            // 设置最大POST大小（用于文件上传）
            connector.setProperty("maxPostSize", "62914560"); // 60MB
            
            // 设置最大连接数
            connector.setProperty("maxConnections", "8192");
            
            // 设置连接超时时间
            connector.setProperty("connectionTimeout", "20000");
        });
        
        return tomcat;
    }
}