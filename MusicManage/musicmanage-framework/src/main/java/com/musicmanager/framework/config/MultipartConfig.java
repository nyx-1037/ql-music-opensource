package com.musicmanager.framework.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传配置
 * 解决FileCountLimitExceededException问题
 * 
 * @author musicmanager
 */
@Configuration
public class MultipartConfig {

    // 移除静态代码块中的系统属性设置，避免影响若依原有的文件上传功能
    // 文件数量限制通过TomcatConfig中的maxParameterCount来处理

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 设置文件上传的最大大小
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        
        // 设置总上传数据的最大大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(60));
        
        // 设置内存临界值
        factory.setFileSizeThreshold(DataSize.ofKilobytes(0));
        
        // 设置临时文件存储位置
        factory.setLocation(System.getProperty("java.io.tmpdir"));
        
        return factory.createMultipartConfig();
    }
}