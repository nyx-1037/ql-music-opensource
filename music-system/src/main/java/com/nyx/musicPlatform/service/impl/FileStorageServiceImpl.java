package com.nyx.musicPlatform.service.impl;

import com.nyx.musicPlatform.config.OssConfig;
import com.nyx.musicPlatform.service.FileStorageService;
import com.nyx.musicPlatform.service.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * 文件存储服务实现类
 * 专门使用阿里云OSS存储，不再支持本地存储
 * 
 * @author nyx
 * @since 2025-06
 */
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private OssFileService ossFileService;

    @Override
    public String uploadMusicFile(MultipartFile file, String fileName) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        return ossFileService.uploadMusicFile(file, fileName);
    }

    @Override
    public String uploadCoverFile(MultipartFile file) {
        log.info("上传封面文件: {}", file.getOriginalFilename());
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        return ossFileService.uploadCoverFile(file);
    }

    @Override
    public String uploadBannerFile(MultipartFile file) {
        log.info("上传Banner文件: {}", file.getOriginalFilename());
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        return ossFileService.uploadBannerFile(file);
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // 如果是OSS URL，提取ObjectKey
        String objectKey = ossFileService.extractObjectKeyFromUrl(filePath);
        if (objectKey != null) {
            return ossFileService.deleteFile(objectKey);
        }
        return false;
    }

    @Override
    public String getFileUrl(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // 如果已经是完整URL，直接返回
        if (filePath.startsWith("http")) {
            return filePath;
        }
        // 否则作为ObjectKey处理
        return ossFileService.getFileUrl(filePath);
    }

    @Override
    public boolean fileExists(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        String objectKey = ossFileService.extractObjectKeyFromUrl(filePath);
        if (objectKey != null) {
            return ossFileService.doesFileExist(objectKey);
        }
        return false;
    }

    @Override
    public InputStream getFileStream(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        String objectKey = ossFileService.extractObjectKeyFromUrl(filePath);
        if (objectKey != null) {
            return ossFileService.getFileStream(objectKey);
        }
        return null;
    }

    @Override
    public String getStorageType() {
        return "OSS";
    }

    @Override
    public String generateUniqueFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
    }

    @Override
    public long getFileSize(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS文件大小获取通过元数据
        String objectKey = ossFileService.extractObjectKeyFromUrl(filePath);
        if (objectKey != null) {
            return ossFileService.getFileSize(objectKey);
        }
        return 0L;
    }

    @Override
    public long getLastModified(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS文件最后修改时间通过元数据获取
        String objectKey = ossFileService.extractObjectKeyFromUrl(filePath);
        if (objectKey != null) {
            return ossFileService.getLastModified(objectKey);
        }
        return 0L;
    }

    @Override
    public Object getObjectMetadata(String filePath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // 返回OSS对象元数据
        String objectKey = ossFileService.extractObjectKeyFromUrl(filePath);
        if (objectKey != null) {
            return ossFileService.getObjectMetadata(objectKey);
        }
        return null;
    }

    @Override
    public boolean copyFile(String sourcePath, String targetPath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS文件复制
        String sourceKey = ossFileService.extractObjectKeyFromUrl(sourcePath);
        String targetKey = ossFileService.extractObjectKeyFromUrl(targetPath);
        if (sourceKey != null && targetKey != null) {
            return ossFileService.copyFile(sourceKey, targetKey);
        }
        return false;
    }

    @Override
    public boolean moveFile(String sourcePath, String targetPath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS文件移动（复制后删除源文件）
        String sourceKey = ossFileService.extractObjectKeyFromUrl(sourcePath);
        String targetKey = ossFileService.extractObjectKeyFromUrl(targetPath);
        if (sourceKey != null && targetKey != null) {
            if (ossFileService.copyFile(sourceKey, targetKey)) {
                return ossFileService.deleteFile(sourceKey);
            }
        }
        return false;
    }

    @Override
    public String[] listFiles(String directoryPath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS目录文件列表
        return ossFileService.listFiles(directoryPath);
    }

    @Override
    public boolean createDirectory(String directoryPath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS不需要显式创建目录，上传文件时自动创建
        return true;
    }

    @Override
    public boolean deleteDirectory(String directoryPath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS目录删除（删除指定前缀的所有文件）
        return ossFileService.deleteDirectory(directoryPath);
    }

    @Override
    public boolean directoryExists(String directoryPath) {
        if (!ossConfig.isEnabled()) {
            throw new RuntimeException("OSS存储未启用，系统仅支持OSS存储模式");
        }
        // OSS目录存在检查（检查是否有该前缀的文件）
        return ossFileService.directoryExists(directoryPath);
    }


}