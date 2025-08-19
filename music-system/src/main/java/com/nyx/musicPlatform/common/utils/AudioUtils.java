package com.nyx.musicPlatform.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 音频工具类
 *
 * @author nyx
 * @since 2025-06
 */
@Component
public class AudioUtils {

    private static final Logger log = LoggerFactory.getLogger(AudioUtils.class);

    /**
     * 从音频文件中提取元数据
     */
    public Map<String, Object> extractAudioMetadata(MultipartFile file) {
        Map<String, Object> metadata = new HashMap<>();
        
        // 创建临时文件
        File tempFile = null;
        try {
            tempFile = createTempFile(file);
            AudioFile audioFile = AudioFileIO.read(tempFile);
            
            // 获取音频头信息
            AudioHeader audioHeader = audioFile.getAudioHeader();
            if (audioHeader != null) {
                metadata.put("duration", audioHeader.getTrackLength()); // 时长（秒）
                metadata.put("bitRate", audioHeader.getBitRate()); // 比特率
                metadata.put("sampleRate", audioHeader.getSampleRate()); // 采样率
                metadata.put("channels", audioHeader.getChannels()); // 声道数
                metadata.put("encoding", audioHeader.getEncodingType()); // 编码类型
                metadata.put("format", audioHeader.getFormat()); // 格式
            }
            
            // 获取标签信息
            Tag tag = audioFile.getTag();
            if (tag != null) {
                metadata.put("title", getTagValue(tag, FieldKey.TITLE));
                metadata.put("artist", getTagValue(tag, FieldKey.ARTIST));
                metadata.put("album", getTagValue(tag, FieldKey.ALBUM));
                metadata.put("year", getTagValue(tag, FieldKey.YEAR));
                metadata.put("genre", getTagValue(tag, FieldKey.GENRE));
                metadata.put("track", getTagValue(tag, FieldKey.TRACK));
                metadata.put("albumArtist", getTagValue(tag, FieldKey.ALBUM_ARTIST));
                metadata.put("composer", getTagValue(tag, FieldKey.COMPOSER));
                metadata.put("comment", getTagValue(tag, FieldKey.COMMENT));
            }
            
            log.info("音频元数据提取成功，文件：{}", file.getOriginalFilename());
            
        } catch (Exception e) {
            log.error("提取音频元数据失败，文件：" + file.getOriginalFilename() + ", 错误：" + e.getMessage());
            // 返回基本信息
            metadata.put("title", getFileNameWithoutExtension(file.getOriginalFilename()));
            metadata.put("artist", "未知艺术家");
            metadata.put("album", "未知专辑");
            metadata.put("duration", 0);
        } finally {
            // 清理临时文件
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    log.warn("删除临时文件失败：{}", e.getMessage());
                }
            }
        }
        
        return metadata;
    }

    /**
     * 从文件路径提取音频元数据
     */
    public Map<String, Object> extractAudioMetadata(String filePath) {
        Map<String, Object> metadata = new HashMap<>();
        
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.warn("音频文件不存在：{}", filePath);
                return metadata;
            }
            
            AudioFile audioFile = AudioFileIO.read(file);
            
            // 获取音频头信息
            AudioHeader audioHeader = audioFile.getAudioHeader();
            if (audioHeader != null) {
                metadata.put("duration", audioHeader.getTrackLength());
                metadata.put("bitRate", audioHeader.getBitRate());
                metadata.put("sampleRate", audioHeader.getSampleRate());
                metadata.put("channels", audioHeader.getChannels());
                metadata.put("encoding", audioHeader.getEncodingType());
                metadata.put("format", audioHeader.getFormat());
            }
            
            // 获取标签信息
            Tag tag = audioFile.getTag();
            if (tag != null) {
                metadata.put("title", getTagValue(tag, FieldKey.TITLE));
                metadata.put("artist", getTagValue(tag, FieldKey.ARTIST));
                metadata.put("album", getTagValue(tag, FieldKey.ALBUM));
                metadata.put("year", getTagValue(tag, FieldKey.YEAR));
                metadata.put("genre", getTagValue(tag, FieldKey.GENRE));
                metadata.put("track", getTagValue(tag, FieldKey.TRACK));
                metadata.put("albumArtist", getTagValue(tag, FieldKey.ALBUM_ARTIST));
                metadata.put("composer", getTagValue(tag, FieldKey.COMPOSER));
                metadata.put("comment", getTagValue(tag, FieldKey.COMMENT));
            }
            
            log.info("音频元数据提取成功，文件：{}", filePath);
            
        } catch (Exception e) {
            log.error("提取音频元数据失败，文件：" + filePath + ", 错误：" + e.getMessage());
        }
        
        return metadata;
    }

    /**
     * 格式化音频时长
     */
    public String formatDuration(int seconds) {
        if (seconds <= 0) {
            return "00:00";
        }
        
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }

    /**
     * 解析时长字符串为秒数
     */
    public int parseDuration(String duration) {
        if (duration == null || duration.trim().isEmpty()) {
            return 0;
        }
        
        try {
            String[] parts = duration.split(":");
            int seconds = 0;
            
            if (parts.length == 2) {
                // MM:SS 格式
                seconds = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            } else if (parts.length == 3) {
                // HH:MM:SS 格式
                seconds = Integer.parseInt(parts[0]) * 3600 + 
                         Integer.parseInt(parts[1]) * 60 + 
                         Integer.parseInt(parts[2]);
            } else {
                // 直接是秒数
                seconds = Integer.parseInt(duration);
            }
            
            return Math.max(seconds, 0);
        } catch (NumberFormatException e) {
            log.warn("解析时长失败：{}", duration);
            return 0;
        }
    }

    /**
     * 验证音频文件格式
     */
    public boolean isValidAudioFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return false;
        }
        
        String extension = getFileExtension(filename).toLowerCase();
        return isValidAudioExtension(extension);
    }

    /**
     * 验证音频文件扩展名
     */
    public boolean isValidAudioExtension(String extension) {
        String[] validExtensions = {"mp3", "wav", "flac", "aac", "ogg", "m4a", "wma", "ape"};
        for (String validExt : validExtensions) {
            if (validExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取音频文件的比特率描述
     */
    public String getBitRateDescription(String bitRate) {
        if (bitRate == null || bitRate.trim().isEmpty()) {
            return "未知";
        }
        
        try {
            int rate = Integer.parseInt(bitRate);
            if (rate >= 320) {
                return "高品质";
            } else if (rate >= 192) {
                return "标准品质";
            } else if (rate >= 128) {
                return "普通品质";
            } else {
                return "低品质";
            }
        } catch (NumberFormatException e) {
            return "未知";
        }
    }

    /**
     * 创建临时文件
     */
    private File createTempFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        
        Path tempFile = Files.createTempFile("audio_", "." + extension);
        
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile.toFile())) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        
        return tempFile.toFile();
    }

    /**
     * 获取标签值
     */
    private String getTagValue(Tag tag, FieldKey fieldKey) {
        try {
            String value = tag.getFirst(fieldKey);
            return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 获取不带扩展名的文件名
     */
    private String getFileNameWithoutExtension(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "未知";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
    }

    /**
     * 获取音频格式描述
     */
    public String getFormatDescription(String format) {
        if (format == null) {
            return "未知格式";
        }
        
        Map<String, String> formatDescriptions = new HashMap<>();
        formatDescriptions.put("MP3", "MP3 (MPEG Audio Layer 3)");
        formatDescriptions.put("FLAC", "FLAC (Free Lossless Audio Codec)");
        formatDescriptions.put("WAV", "WAV (Waveform Audio File Format)");
        formatDescriptions.put("AAC", "AAC (Advanced Audio Coding)");
        formatDescriptions.put("OGG", "OGG Vorbis");
        formatDescriptions.put("M4A", "M4A (MPEG-4 Audio)");
        formatDescriptions.put("WMA", "WMA (Windows Media Audio)");
        formatDescriptions.put("APE", "APE (Monkey's Audio)");
        
        return formatDescriptions.getOrDefault(format.toUpperCase(), format);
    }
}