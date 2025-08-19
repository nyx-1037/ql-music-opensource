package com.musicmanager.system.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.musicmanager.system.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 阿里云OSS服务类
 * 
 * @author musicmanager
 * @date 2024-12-01
 */
@Slf4j
@Service
public class OssService {
    
    @Autowired
    private OssConfig ossConfig;
    
    /**
     * 上传音乐文件到OSS
     * 
     * @param file 音乐文件
     * @return OSS文件URL
     */
    public String uploadMusicFile(MultipartFile file) {
        if (!ossConfig.isEnabled()) {
            log.warn("OSS未启用，无法上传文件");
            return null;
        }
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 生成带日期分类的文件路径
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String fileName = timestamp + "_" + randomSuffix + fileExtension;
        
        String objectKey = ossConfig.getMusicPrefix() + datePath + "/" + fileName;
        
        return uploadFile(file, objectKey);
    }
    
    /**
     * 上传封面文件到OSS
     * 
     * @param file 封面文件
     * @return OSS文件URL
     */
    public String uploadCoverFile(MultipartFile file) {
        if (!ossConfig.isEnabled()) {
            log.warn("OSS未启用，无法上传文件");
            return null;
        }
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 生成带日期分类的文件路径
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String fileName = timestamp + "_" + randomSuffix + fileExtension;
        
        String objectKey = ossConfig.getCoverPrefix() + datePath + "/" + fileName;
        
        return uploadFile(file, objectKey);
    }
    
    /**
     * 上传Banner图片到OSS
     * 
     * @param file Banner图片文件
     * @return OSS文件URL
     */
    public String uploadBannerFile(MultipartFile file) {
        if (!ossConfig.isEnabled()) {
            log.warn("OSS未启用，无法上传文件");
            return null;
        }
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 生成带日期分类的文件路径
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String fileName = timestamp + "_" + randomSuffix + fileExtension;
        
        String objectKey = ossConfig.getBannerPrefix() + datePath + "/" + fileName;
        
        return uploadFile(file, objectKey);
    }
    
    /**
     * 上传文件到OSS
     * 
     * @param file 文件
     * @param objectKey OSS对象键
     * @return OSS文件URL
     */
    private String uploadFile(MultipartFile file, String objectKey) {
        OSS ossClient = null;
        try {
            // 创建OSS客户端
            ossClient = new OSSClientBuilder().build(
                "https://" + ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
            );
            
            // 上传文件
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                ossConfig.getBucketName(),
                objectKey,
                inputStream
            );
            
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            
            // 构建文件访问URL
            String fileUrl = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + objectKey;
            
            log.info("文件上传成功，OSS URL: {}", fileUrl);
            return fileUrl;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * 删除OSS文件
     * 
     * @param fileUrl 文件URL
     */
    public void deleteFile(String fileUrl) {
        if (!ossConfig.isEnabled() || fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        
        OSS ossClient = null;
        try {
            // 从URL中提取对象键
            String objectKey = extractObjectKeyFromUrl(fileUrl);
            if (objectKey == null) {
                log.warn("无法从URL中提取对象键: {}", fileUrl);
                return;
            }
            
            // 创建OSS客户端
            ossClient = new OSSClientBuilder().build(
                "https://" + ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
            );
            
            // 删除文件
            ossClient.deleteObject(ossConfig.getBucketName(), objectKey);
            log.info("文件删除成功，对象键: {}", objectKey);
            
        } catch (Exception e) {
            log.error("文件删除失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * 从URL中提取对象键
     * 
     * @param fileUrl 文件URL
     * @return 对象键
     */
    private String extractObjectKeyFromUrl(String fileUrl) {
        try {
            String bucketDomain = ossConfig.getBucketName() + "." + ossConfig.getEndpoint();
            if (fileUrl.contains(bucketDomain)) {
                int index = fileUrl.indexOf(bucketDomain) + bucketDomain.length() + 1;
                return fileUrl.substring(index);
            }
        } catch (Exception e) {
            log.error("提取对象键失败", e);
        }
        return null;
    }
}