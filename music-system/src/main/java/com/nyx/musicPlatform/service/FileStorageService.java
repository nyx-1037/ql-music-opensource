package com.nyx.musicPlatform.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件存储服务接口
 * 
 * @author nyx
 * @since 2025-06
 */
public interface FileStorageService {

    /**
     * 获取存储类型
     */
    String getStorageType();

    /**
     * 生成唯一文件名
     */
    String generateUniqueFileName(String originalFilename);

    /**
     * 上传音乐文件
     */
    String uploadMusicFile(MultipartFile file, String fileName);

    /**
     * 上传封面文件
     */
    String uploadCoverFile(MultipartFile file);

    /**
     * 上传Banner文件
     */
    String uploadBannerFile(MultipartFile file);

    /**
     * 删除文件
     */
    boolean deleteFile(String filePath);

    /**
     * 获取文件流
     */
    InputStream getFileStream(String filePath);

    /**
     * 检查文件是否存在
     */
    boolean fileExists(String filePath);

    /**
     * 获取文件URL
     */
    String getFileUrl(String filePath);

    /**
     * 获取文件大小
     */
    long getFileSize(String filePath);

    /**
     * 获取文件最后修改时间
     */
    long getLastModified(String filePath);

    /**
     * 获取文件元数据
     */
    Object getObjectMetadata(String filePath);

    /**
     * 复制文件
     */
    boolean copyFile(String sourcePath, String targetPath);

    /**
     * 移动文件
     */
    boolean moveFile(String sourcePath, String targetPath);

    /**
     * 列出目录下的文件
     */
    String[] listFiles(String directoryPath);

    /**
     * 创建目录
     */
    boolean createDirectory(String directoryPath);

    /**
     * 删除目录
     */
    boolean deleteDirectory(String directoryPath);

    /**
     * 检查目录是否存在
     */
    boolean directoryExists(String directoryPath);
}