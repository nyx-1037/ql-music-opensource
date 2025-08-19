package com.nyx.musicPlatform.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import java.util.List;
import java.util.ArrayList;
import com.nyx.musicPlatform.config.OssConfig;
import com.nyx.musicPlatform.service.OssFileService;
import com.nyx.musicPlatform.common.exception.BusinessException;
import com.nyx.musicPlatform.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * OSS文件存储服务实现类
 * 
 * @author nyx
 * @since 2025-06
 */
@Slf4j
@Service
public class OssFileServiceImpl implements OssFileService {

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    @Override
    public String uploadMusicFile(MultipartFile file, String fileName) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            log.warn("OSS未启用或客户端未初始化，无法上传音乐文件");
            return null;
        }

        try {
            String objectKey = ossConfig.getMusicObjectKey(fileName);
            return uploadFile(file.getInputStream(), objectKey, file.getContentType());
        } catch (IOException e) {
            log.error("上传音乐文件失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String uploadCoverFile(MultipartFile file) {
        if (!ossConfig.isEnabled()) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "OSS服务未启用");
        }
        
        String objectKey = ossConfig.getCoverObjectKey(file.getOriginalFilename());
        
        try {
            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            // 创建上传请求
            PutObjectRequest putRequest = new PutObjectRequest(
                ossConfig.getBucketName(),
                objectKey,
                file.getInputStream(),
                metadata
            );
            
            // 执行上传
            ossClient.putObject(putRequest);
            
            // 返回文件URL
            return ossConfig.getFileUrl(objectKey);
            
        } catch (Exception e) {
            log.error("上传封面文件失败: {}", e.getMessage(), e);
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED, "上传封面文件失败: " + e.getMessage());
        }
    }
    
    @Override
    public String uploadAvatarFile(MultipartFile file) {
        if (!ossConfig.isEnabled()) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "OSS服务未启用");
        }
        
        String objectKey = ossConfig.getAvatarObjectKey(file.getOriginalFilename());
        
        try {
            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            // 创建上传请求
            PutObjectRequest putRequest = new PutObjectRequest(
                ossConfig.getBucketName(),
                objectKey,
                file.getInputStream(),
                metadata
            );
            
            // 执行上传
            ossClient.putObject(putRequest);
            
            // 返回文件URL
            return ossConfig.getFileUrl(objectKey);
            
        } catch (Exception e) {
            log.error("上传头像文件失败: {}", e.getMessage(), e);
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED, "上传头像文件失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadBannerFile(MultipartFile file) {
        if (!ossConfig.isEnabled()) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "OSS服务未启用");
        }
        
        String objectKey = ossConfig.getBannerObjectKey(file.getOriginalFilename());
        
        try {
            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            // 创建上传请求
            PutObjectRequest putRequest = new PutObjectRequest(
                ossConfig.getBucketName(),
                objectKey,
                file.getInputStream(),
                metadata
            );
            
            // 执行上传
            ossClient.putObject(putRequest);
            
            // 返回文件URL
            return ossConfig.getFileUrl(objectKey);
            
        } catch (Exception e) {
            log.error("上传Banner文件失败: {}", e.getMessage(), e);
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED, "上传Banner文件失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String objectKey, String contentType) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            log.warn("OSS未启用或客户端未初始化，无法上传文件");
            return null;
        }

        try {
            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            if (StringUtils.hasText(contentType)) {
                metadata.setContentType(contentType);
            }
            metadata.setContentLength(inputStream.available());

            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                ossConfig.getBucketName(), objectKey, inputStream, metadata);

            // 上传文件
            ossClient.putObject(putObjectRequest);
            
            log.info("文件上传成功: {}", objectKey);
            return ossConfig.getFileUrl(objectKey);
            
        } catch (OSSException e) {
            log.error("OSS上传文件失败: ErrorCode={}, ErrorMessage={}", e.getErrorCode(), e.getErrorMessage());
            return null;
        } catch (Exception e) {
            log.error("上传文件失败: {}", e.getMessage());
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.warn("关闭输入流失败: {}", e.getMessage());
            }
        }
    }

    @Override
    public boolean deleteFile(String objectKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            log.warn("OSS未启用或客户端未初始化，无法删除文件");
            return false;
        }

        if (!StringUtils.hasText(objectKey)) {
            log.warn("ObjectKey为空，无法删除文件");
            return false;
        }

        try {
            ossClient.deleteObject(ossConfig.getBucketName(), objectKey);
            log.info("文件删除成功: {}", objectKey);
            return true;
        } catch (OSSException e) {
            log.error("OSS删除文件失败: ErrorCode={}, ErrorMessage={}", e.getErrorCode(), e.getErrorMessage());
            return false;
        } catch (Exception e) {
            log.error("删除文件失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getFileUrl(String objectKey) {
        if (!ossConfig.isEnabled()) {
            return null;
        }
        return ossConfig.getFileUrl(objectKey);
    }

    @Override
    public boolean doesFileExist(String objectKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return false;
        }

        if (!StringUtils.hasText(objectKey)) {
            return false;
        }

        try {
            return ossClient.doesObjectExist(ossConfig.getBucketName(), objectKey);
        } catch (Exception e) {
            log.error("检查文件是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public InputStream getFileStream(String objectKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            log.warn("OSS未启用或客户端未初始化，无法获取文件流");
            return null;
        }

        if (!StringUtils.hasText(objectKey)) {
            log.warn("ObjectKey为空，无法获取文件流");
            return null;
        }

        try {
            return ossClient.getObject(ossConfig.getBucketName(), objectKey).getObjectContent();
        } catch (OSSException e) {
            log.error("OSS获取文件流失败: ErrorCode={}, ErrorMessage={}", e.getErrorCode(), e.getErrorMessage());
            return null;
        } catch (Exception e) {
            log.error("获取文件流失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String extractObjectKeyFromUrl(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return null;
        }

        try {
            // 如果是自定义域名
            if (StringUtils.hasText(ossConfig.getCustomDomain()) && fileUrl.startsWith(ossConfig.getCustomDomain())) {
                return fileUrl.substring(ossConfig.getCustomDomain().length() + 1);
            }

            // 如果是OSS默认域名
            String ossHost = ossConfig.getBucketName() + "." + ossConfig.getEndpoint().replace("https://", "").replace("http://", "");
            if (fileUrl.contains(ossHost)) {
                URL url = new URL(fileUrl);
                return url.getPath().substring(1); // 去掉开头的 '/'
            }

            return null;
        } catch (Exception e) {
            log.error("从URL提取ObjectKey失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public long getFileSize(String objectKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return 0L;
        }

        if (!StringUtils.hasText(objectKey)) {
            return 0L;
        }

        try {
            ObjectMetadata metadata = ossClient.getObjectMetadata(ossConfig.getBucketName(), objectKey);
            return metadata.getContentLength();
        } catch (Exception e) {
            log.error("获取文件大小失败: {}", e.getMessage());
            return 0L;
        }
    }

    @Override
    public long getLastModified(String objectKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return 0L;
        }

        if (!StringUtils.hasText(objectKey)) {
            return 0L;
        }

        try {
            ObjectMetadata metadata = ossClient.getObjectMetadata(ossConfig.getBucketName(), objectKey);
            return metadata.getLastModified() != null ? metadata.getLastModified().getTime() : 0L;
        } catch (Exception e) {
            log.error("获取文件最后修改时间失败: {}", e.getMessage());
            return 0L;
        }
    }

    @Override
    public Object getObjectMetadata(String objectKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return null;
        }

        if (!StringUtils.hasText(objectKey)) {
            return null;
        }

        try {
            return ossClient.getObjectMetadata(ossConfig.getBucketName(), objectKey);
        } catch (Exception e) {
            log.error("获取对象元数据失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean copyFile(String sourceKey, String targetKey) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return false;
        }

        if (!StringUtils.hasText(sourceKey) || !StringUtils.hasText(targetKey)) {
            return false;
        }

        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                ossConfig.getBucketName(), sourceKey, ossConfig.getBucketName(), targetKey);
            ossClient.copyObject(copyObjectRequest);
            log.info("文件复制成功: {} -> {}", sourceKey, targetKey);
            return true;
        } catch (Exception e) {
            log.error("文件复制失败: {} -> {}", sourceKey, targetKey, e);
            return false;
        }
    }

    @Override
    public String[] listFiles(String prefix) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return new String[0];
        }

        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfig.getBucketName());
            if (StringUtils.hasText(prefix)) {
                listObjectsRequest.setPrefix(prefix);
            }
            listObjectsRequest.setMaxKeys(1000); // 限制返回数量

            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            List<String> fileList = new ArrayList<>();
            
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                fileList.add(objectSummary.getKey());
            }
            
            return fileList.toArray(new String[0]);
        } catch (Exception e) {
            log.error("列出文件失败: {}", e.getMessage());
            return new String[0];
        }
    }

    @Override
    public boolean deleteDirectory(String prefix) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return false;
        }

        if (!StringUtils.hasText(prefix)) {
            return false;
        }

        try {
            // 确保前缀以 / 结尾
            if (!prefix.endsWith("/")) {
                prefix += "/";
            }

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfig.getBucketName());
            listObjectsRequest.setPrefix(prefix);
            listObjectsRequest.setMaxKeys(1000);

            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            List<String> keysToDelete = new ArrayList<>();
            
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                keysToDelete.add(objectSummary.getKey());
            }

            if (!keysToDelete.isEmpty()) {
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(ossConfig.getBucketName());
                deleteObjectsRequest.setKeys(keysToDelete);
                ossClient.deleteObjects(deleteObjectsRequest);
                log.info("目录删除成功，删除了 {} 个文件", keysToDelete.size());
            }
            
            return true;
        } catch (Exception e) {
            log.error("删除目录失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean directoryExists(String prefix) {
        if (!ossConfig.isEnabled() || ossClient == null) {
            return false;
        }

        if (!StringUtils.hasText(prefix)) {
            return false;
        }

        try {
            // 确保前缀以 / 结尾
            if (!prefix.endsWith("/")) {
                prefix += "/";
            }

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfig.getBucketName());
            listObjectsRequest.setPrefix(prefix);
            listObjectsRequest.setMaxKeys(1); // 只需要检查是否有文件存在

            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            return !objectListing.getObjectSummaries().isEmpty();
        } catch (Exception e) {
            log.error("检查目录是否存在失败: {}", e.getMessage());
            return false;
        }
    }
}