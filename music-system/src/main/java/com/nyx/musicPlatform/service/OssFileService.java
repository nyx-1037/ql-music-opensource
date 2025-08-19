package com.nyx.musicPlatform.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * OSS文件存储服务接口
 * 
 * @author nyx
 * @since 2025-06
 */
public interface OssFileService {

    /**
     * 上传音乐文件
     * 
     * @param file 文件
     * @param fileName 文件名
     * @return 文件访问URL
     */
    String uploadMusicFile(MultipartFile file, String fileName);

    /**
     * 上传封面文件
     * 
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadCoverFile(MultipartFile file);

    /**
     * 上传Banner文件
     * 
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadBannerFile(MultipartFile file);
    
    /**
     * 上传用户头像
     * 
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadAvatarFile(MultipartFile file);

    /**
     * 上传文件流
     * 
     * @param inputStream 文件流
     * @param objectKey 对象键
     * @param contentType 内容类型
     * @return 文件访问URL
     */
    String uploadFile(InputStream inputStream, String objectKey, String contentType);

    /**
     * 删除文件
     * 
     * @param objectKey 对象键
     * @return 是否删除成功
     */
    boolean deleteFile(String objectKey);

    /**
     * 获取文件访问URL
     * 
     * @param objectKey 对象键
     * @return 文件访问URL
     */
    String getFileUrl(String objectKey);

    /**
     * 检查文件是否存在
     * 
     * @param objectKey 对象键
     * @return 是否存在
     */
    boolean doesFileExist(String objectKey);

    /**
     * 获取文件下载流
     * 
     * @param objectKey 对象键
     * @return 文件输入流
     */
    InputStream getFileStream(String objectKey);

    /**
     * 从URL中提取ObjectKey
     * 
     * @param fileUrl 文件URL
     * @return ObjectKey
     */
    String extractObjectKeyFromUrl(String fileUrl);

    /**
     * 获取文件大小
     * 
     * @param objectKey 对象键
     * @return 文件大小（字节）
     */
    long getFileSize(String objectKey);

    /**
     * 获取文件最后修改时间
     * 
     * @param objectKey 对象键
     * @return 最后修改时间戳
     */
    long getLastModified(String objectKey);

    /**
     * 获取对象元数据
     * 
     * @param objectKey 对象键
     * @return 对象元数据
     */
    Object getObjectMetadata(String objectKey);

    /**
     * 复制文件
     * 
     * @param sourceKey 源文件键
     * @param targetKey 目标文件键
     * @return 是否复制成功
     */
    boolean copyFile(String sourceKey, String targetKey);

    /**
     * 列出指定前缀的文件
     * 
     * @param prefix 前缀
     * @return 文件列表
     */
    String[] listFiles(String prefix);

    /**
     * 删除指定前缀的所有文件（目录删除）
     * 
     * @param prefix 前缀
     * @return 是否删除成功
     */
    boolean deleteDirectory(String prefix);

    /**
     * 检查指定前缀是否存在文件（目录存在检查）
     * 
     * @param prefix 前缀
     * @return 是否存在
     */
    boolean directoryExists(String prefix);
}