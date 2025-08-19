package com.musicmanager.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置类
 * 
 * @author musicmanager
 * @date 2024-12-01
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssConfig {
    
    /**
     * 是否启用OSS
     */
    private boolean enabled;
    
    /**
     * OSS访问端点
     */
    private String endpoint;
    
    /**
     * 访问密钥ID
     */
    private String accessKeyId;
    
    /**
     * 访问密钥Secret
     */
    private String accessKeySecret;
    
    /**
     * 存储桶名称
     */
    private String bucketName;
    
    /**
     * 音乐文件存储前缀
     */
    private String musicPrefix;
    
    /**
     * 封面图片存储前缀
     */
    private String coverPrefix;
    
    /**
     * Banner图片存储前缀
     */
    private String bannerPrefix;
    
    /**
     * 获取Banner图片存储前缀
     * @return Banner图片存储前缀
     */
    public String getBannerPrefix() {
        return bannerPrefix;
    }
    
    /**
     * 获取音乐文件存储前缀
     * @return 音乐文件存储前缀
     */
    public String getMusicPrefix() {
        return musicPrefix;
    }
    
    /**
     * 获取封面图片存储前缀
     * @return 封面图片存储前缀
     */
    public String getCoverPrefix() {
        return coverPrefix;
    }
}