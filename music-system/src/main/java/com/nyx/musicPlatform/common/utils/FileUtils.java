package com.nyx.musicPlatform.common.utils;

import com.nyx.musicPlatform.common.exception.BusinessException;
import com.nyx.musicPlatform.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 文件工具类
 * 
 * @deprecated 项目已完全切换到阿里云OSS存储，此类不再使用
 * @author nyx
 * @since 2025-06
 */
@Deprecated
@Component
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    @Value("${file.upload.path:/uploads}")
    private String uploadPath;

    @Value("${file.upload.max-size:52428800}")
    private long maxFileSize;

    @Value("${file.music.allowed-types:mp3,wav,flac,aac,ogg,m4a}")
    private String allowedMusicTypes;

    @Value("${file.image.allowed-types:jpg,jpeg,png,gif,bmp,webp}")
    private String allowedImageTypes;

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final Pattern INVALID_FILENAME_PATTERN = Pattern.compile("[\\/:*?\"<>|]");
    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB"};

    /**
     * 初始化上传目录
     */
    public void initUploadDirectory() {
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("创建上传目录：{}", uploadDir.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("创建上传目录失败：" + e.getMessage());
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "初始化上传目录失败");
        }
    }

    /**
     * 保存文件
     */
    public String saveFile(MultipartFile file, String subDirectory) {
        validateFile(file);
        
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String filename = generateUniqueFilename(extension);
        
        // 创建日期目录
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String relativePath = subDirectory + "/" + dateDir + "/" + filename;
        Path filePath = Paths.get(uploadPath, relativePath);
        
        try {
            // 创建目录
            Files.createDirectories(filePath.getParent());
            
            // 保存文件
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            log.info("文件保存成功：{}", filePath.toAbsolutePath());
            return relativePath;
        } catch (IOException e) {
            log.error("文件保存失败：" + e.getMessage());
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED, "文件保存失败");
        }
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String relativePath) {
        if (!StringUtils.hasText(relativePath)) {
            return false;
        }
        
        try {
            Path filePath = Paths.get(uploadPath, relativePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("文件删除成功：{}", filePath.toAbsolutePath());
                return true;
            } else {
                log.warn("要删除的文件不存在：{}", filePath.toAbsolutePath());
                return false;
            }
        } catch (IOException e) {
            log.error("文件删除失败：" + relativePath + ", 错误：" + e.getMessage());
            return false;
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String relativePath) {
        if (!StringUtils.hasText(relativePath)) {
            return false;
        }
        
        Path filePath = Paths.get(uploadPath, relativePath);
        return Files.exists(filePath);
    }

    /**
     * 获取文件大小
     */
    public long getFileSize(String relativePath) {
        if (!StringUtils.hasText(relativePath)) {
            return 0;
        }
        
        try {
            Path filePath = Paths.get(uploadPath, relativePath);
            if (Files.exists(filePath)) {
                return Files.size(filePath);
            }
        } catch (IOException e) {
            log.error("获取文件大小失败：" + relativePath + ", 错误：" + e.getMessage());
        }
        return 0;
    }

    /**
     * 获取文件输入流
     */
    public InputStream getFileInputStream(String relativePath) throws IOException {
        if (!StringUtils.hasText(relativePath)) {
            throw new FileNotFoundException("文件路径为空");
        }
        
        Path filePath = Paths.get(uploadPath, relativePath);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("文件不存在：" + relativePath);
        }
        
        return Files.newInputStream(filePath);
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "文件不能为空");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ResultCode.FILE_SIZE_EXCEEDED, 
                    "文件大小超出限制，最大允许：" + formatFileSize(maxFileSize));
        }
        
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new BusinessException(ResultCode.FILE_NAME_INVALID, "文件名无效");
        }
        
        if (INVALID_FILENAME_PATTERN.matcher(originalFilename).find()) {
            throw new BusinessException(ResultCode.FILE_NAME_INVALID, "文件名包含非法字符");
        }
    }

    /**
     * 验证音乐文件格式
     */
    public boolean isValidMusicFile(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        Set<String> allowedTypes = new HashSet<>(Arrays.asList(allowedMusicTypes.toLowerCase().split(",")));
        return allowedTypes.contains(extension);
    }

    /**
     * 验证图片文件格式
     */
    public boolean isValidImageFile(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        Set<String> allowedTypes = new HashSet<>(Arrays.asList(allowedImageTypes.toLowerCase().split(",")));
        return allowedTypes.contains(extension);
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 生成唯一文件名
     */
    public static String generateUniqueFilename(String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return timestamp + "_" + random + (StringUtils.hasText(extension) ? "." + extension : "");
    }

    /**
     * 计算文件MD5
     */
    public String calculateFileMD5(MultipartFile file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream inputStream = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("计算文件MD5失败：" + e.getMessage());
            return null;
        }
    }

    /**
     * 格式化文件大小
     */
    public String formatFileSize(long size) {
        if (size <= 0) {
            return "0 B";
        }
        
        int unitIndex = (int) (Math.log(size) / Math.log(1024));
        unitIndex = Math.min(unitIndex, UNITS.length - 1);
        
        double value = size / Math.pow(1024, unitIndex);
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value) + " " + UNITS[unitIndex];
    }

    /**
     * 获取MIME类型
     */
    public String getMimeType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        
        Map<String, String> mimeTypes = new HashMap<>();
        // 音频类型
        mimeTypes.put("mp3", "audio/mpeg");
        mimeTypes.put("wav", "audio/wav");
        mimeTypes.put("flac", "audio/flac");
        mimeTypes.put("aac", "audio/aac");
        mimeTypes.put("ogg", "audio/ogg");
        mimeTypes.put("m4a", "audio/mp4");
        
        // 图片类型
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("bmp", "image/bmp");
        mimeTypes.put("webp", "image/webp");
        
        return mimeTypes.getOrDefault(extension, "application/octet-stream");
    }

    /**
     * 清理空目录
     */
    public void cleanEmptyDirectories(String relativePath) {
        try {
            Path dirPath = Paths.get(uploadPath, relativePath);
            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                    if (!stream.iterator().hasNext()) {
                        Files.delete(dirPath);
                        log.info("删除空目录：{}", dirPath.toAbsolutePath());
                        
                        // 递归清理父目录
                        Path parent = dirPath.getParent();
                        if (parent != null && !parent.equals(Paths.get(uploadPath))) {
                            cleanEmptyDirectories(Paths.get(uploadPath).relativize(parent).toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.warn("清理空目录失败：{}, 错误：{}", relativePath, e.getMessage());
        }
    }

    /**
     * 获取上传路径
     */
    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * 获取最大文件大小
     */
    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * 获取允许的音乐文件类型
     */
    public String getAllowedMusicTypes() {
        return allowedMusicTypes;
    }

    /**
     * 获取允许的图片文件类型
     */
    public String getAllowedImageTypes() {
        return allowedImageTypes;
    }
}