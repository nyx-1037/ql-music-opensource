package com.nyx.musicPlatform.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 阿里云OSS配置类
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    /**
     * 是否启用OSS
     */
    private boolean enabled = false;

    /**
     * OSS访问域名
     */
    private String endpoint;

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 存储空间名称
     */
    private String bucketName;

    /**
     * 自定义域名（可选）
     */
    private String customDomain;

    /**
     * 音乐文件存储前缀
     */
    private String musicPrefix = "music/";

    /**
     * 封面文件存储前缀
     */
    private String coverPrefix = "cover/";

    /**
     * 头像文件存储前缀
     */
    private String avatarPrefix = "avatars/";

    /**
     * Banner文件存储前缀
     */
    private String bannerPrefix = "banners/";

    /**
     * 创建OSS客户端
     */
    @Bean
    public OSS ossClient() {
        if (!enabled || !StringUtils.hasText(endpoint) || 
            !StringUtils.hasText(accessKeyId) || !StringUtils.hasText(accessKeySecret)) {
            return null;
        }
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 获取文件访问URL
     */
    public String getFileUrl(String objectKey) {
        if (!enabled || !StringUtils.hasText(objectKey)) {
            return null;
        }
        
        // 如果配置了自定义域名，使用自定义域名
        if (StringUtils.hasText(customDomain)) {
            return customDomain + "/" + objectKey;
        }
        
        // 否则使用OSS默认域名
        return "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectKey;
    }

    /**
     * 获取音乐文件的ObjectKey
     */
    public String getMusicObjectKey(String fileName) {
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return musicPrefix + dateDir + "/" + fileName;
    }

    /**
     * 获取封面文件的ObjectKey
     */
    public String getCoverObjectKey(String originalFilename) {
        String extension = com.nyx.musicPlatform.common.utils.FileUtils.getFileExtension(originalFilename);
        String filename = com.nyx.musicPlatform.common.utils.FileUtils.generateUniqueFilename(extension);
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return coverPrefix + dateDir + "/" + filename;
    }
    
    /**
     * 获取头像文件的ObjectKey
     */
    public String getAvatarObjectKey(String originalFilename) {
        String extension = com.nyx.musicPlatform.common.utils.FileUtils.getFileExtension(originalFilename);
        String filename = com.nyx.musicPlatform.common.utils.FileUtils.generateUniqueFilename(extension);
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return avatarPrefix + dateDir + "/" + filename;
    }
    
    /**
     * 获取Banner文件的ObjectKey
     */
    public String getBannerObjectKey(String originalFilename) {
        String extension = com.nyx.musicPlatform.common.utils.FileUtils.getFileExtension(originalFilename);
        String filename = com.nyx.musicPlatform.common.utils.FileUtils.generateUniqueFilename(extension);
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return bannerPrefix + dateDir + "/" + filename;
    }
}