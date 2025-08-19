package com.musicmanager.system.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.UUID;
import java.io.File;
import com.musicmanager.common.utils.DateUtils;
import com.musicmanager.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import com.musicmanager.system.mapper.QlMusicMapper;
import com.musicmanager.system.domain.QlMusic;
import com.musicmanager.system.service.IQlMusicService;
import com.musicmanager.system.service.OssService;
import lombok.extern.slf4j.Slf4j;

/**
 * 音乐Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Slf4j
@Service
public class QlMusicServiceImpl implements IQlMusicService 
{
    @Autowired
    private QlMusicMapper qlMusicMapper;
    
    @Autowired
    private OssService ossService;
    
    @Value("${music.file.allowed-music-types:mp3,wav,flac,m4a}")
    private String allowedMusicTypes;
    
    @Value("${music.file.allowed-cover-types:jpg,jpeg,png,gif,webp}")
    private String allowedCoverTypes;
    
    @Value("${music.file.max-music-size:50MB}")
    private String maxMusicSize;
    
    @Value("${music.file.max-cover-size:2MB}")
    private String maxCoverSize;
    
    @Value("${music.upload.path:/uploads/music/}")
    private String uploadPath;
    
    @Value("${music.cover.path:/uploads/covers/}")
    private String coverPath;

    /**
     * 查询音乐
     * 
     * @param id 音乐主键
     * @return 音乐
     */
    @Override
    public QlMusic selectQlMusicById(Long id)
    {
        return qlMusicMapper.selectQlMusicById(id);
    }

    /**
     * 查询音乐列表
     * 
     * @param qlMusic 音乐
     * @return 音乐
     */
    @Override
    public List<QlMusic> selectQlMusicList(QlMusic qlMusic)
    {
        return qlMusicMapper.selectQlMusicList(qlMusic);
    }

    /**
     * 新增音乐
     * 
     * @param qlMusic 音乐
     * @return 结果
     */
    @Override
    public int insertQlMusic(QlMusic qlMusic)
    {
        qlMusic.setCreateTime(DateUtils.getNowDate());
        return qlMusicMapper.insertQlMusic(qlMusic);
    }

    /**
     * 修改音乐
     * 
     * @param qlMusic 音乐
     * @return 结果
     */
    @Override
    public int updateQlMusic(QlMusic qlMusic)
    {
        qlMusic.setUpdateTime(DateUtils.getNowDate());
        return qlMusicMapper.updateQlMusic(qlMusic);
    }

    /**
     * 批量删除音乐
     * 
     * @param ids 需要删除的音乐主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicByIds(Long[] ids)
    {
        // 批量删除OSS文件
        for (Long id : ids) {
            QlMusic music = selectQlMusicById(id);
            if (music != null) {
                // 删除OSS中的音乐文件
                if (StringUtils.isNotEmpty(music.getFilePath())) {
                    try {
                        ossService.deleteFile(music.getFilePath());
                    } catch (Exception e) {
                        log.warn("删除OSS音乐文件失败: {}", e.getMessage());
                    }
                }
                
                // 删除OSS中的封面文件
                if (StringUtils.isNotEmpty(music.getCoverUrl())) {
                    try {
                        ossService.deleteFile(music.getCoverUrl());
                    } catch (Exception e) {
                        log.warn("删除OSS封面文件失败: {}", e.getMessage());
                    }
                }
            }
        }
        
        return qlMusicMapper.deleteQlMusicByIds(ids);
    }

    /**
     * 删除音乐信息
     * 
     * @param id 音乐主键
     * @return 结果
     */
    @Override
    public int deleteQlMusicById(Long id)
    {
        // 先查询音乐信息，获取文件URL
        QlMusic music = selectQlMusicById(id);
        if (music != null) {
            // 删除OSS中的音乐文件
            if (StringUtils.isNotEmpty(music.getFilePath())) {
                try {
                    ossService.deleteFile(music.getFilePath());
                } catch (Exception e) {
                    log.warn("删除OSS音乐文件失败: {}", e.getMessage());
                }
            }
            
            // 删除OSS中的封面文件
            if (StringUtils.isNotEmpty(music.getCoverUrl())) {
                try {
                    ossService.deleteFile(music.getCoverUrl());
                } catch (Exception e) {
                    log.warn("删除OSS封面文件失败: {}", e.getMessage());
                }
            }
        }
        
        return qlMusicMapper.deleteQlMusicById(id);
    }

    /**
     * 上传音乐文件
     */
    @Override
    public QlMusic uploadMusic(MultipartFile file, String title, String artist, String album,
            MultipartFile cover, String genre, String releaseYear, String description,
            String tags, Integer isPublic, Integer allowDownload, Integer allowComment, Long uploadUserId) throws Exception {
        
        try {
            // 验证音乐文件
            validateMusicFile(file);
            
            // 上传音乐文件到OSS
            String fileUrl = ossService.uploadMusicFile(file);
            if (StringUtils.isEmpty(fileUrl)) {
                throw new Exception("音乐文件上传到OSS失败");
            }
            
            // 从URL中提取文件名
            String fileName = extractFileNameFromUrl(fileUrl);
            
            // 提取音频信息
            AudioInfo audioInfo = extractAudioInfo(file);
            
            // 创建音乐实体
            QlMusic music = new QlMusic();
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setGenre(genre);
            music.setFilePath(fileUrl);
            music.setFileName(fileName);
            music.setOriginalFileName(file.getOriginalFilename());
            music.setFileSize(file.getSize());
            music.setFileType(getFileExtension(file.getOriginalFilename()));
            music.setDuration((long)audioInfo.getDuration());
            music.setUploadUserId(uploadUserId);
            music.setPlayCount(0L);
            music.setStatus(1L); // 正常状态
            music.setCreateTime(DateUtils.getNowDate());
            music.setIsDeleted(0L);
            
            // 处理发布年份
            if (StringUtils.isNotEmpty(releaseYear)) {
                try {
                    music.setReleaseYear((long)Integer.parseInt(releaseYear));
                } catch (NumberFormatException e) {
                    log.warn("发布年份格式错误: {}", releaseYear);
                }
            }
            
            music.setDescription(description);
            music.setTags(tags);
            music.setIsPublic(isPublic != null ? (long)isPublic : 1L);
            music.setAllowDownload(allowDownload != null ? (long)allowDownload : 1L);
            music.setAllowComment(allowComment != null ? (long)allowComment : 1L);
            
            // 处理封面上传
            if (cover != null && !cover.isEmpty()) {
                try {
                    validateCoverFile(cover);
                    String coverUrl = ossService.uploadCoverFile(cover);
                    if (StringUtils.isNotEmpty(coverUrl)) {
                        music.setCoverUrl(coverUrl);
                    }
                } catch (Exception e) {
                    log.warn("封面上传失败，继续上传音乐: {}", e.getMessage());
                }
            }
            
            // 保存到数据库
            insertQlMusic(music);
            
            log.info("音乐上传成功: {}", fileName);
            return music;
            
        } catch (Exception e) {
            log.error("音乐上传失败", e);
            throw new Exception("音乐上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证音乐文件
     */
    private void validateMusicFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("文件不能为空");
        }
        
        // 验证文件格式
        validateFileFormat(file.getOriginalFilename(), allowedMusicTypes);
        
        // 验证文件大小
        validateFileSize(file.getSize(), maxMusicSize);
    }
    
    /**
     * 验证封面文件
     */
    private void validateCoverFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("文件不能为空");
        }
        
        // 验证文件格式
        validateFileFormat(file.getOriginalFilename(), allowedCoverTypes);
        
        // 验证文件大小
        validateFileSize(file.getSize(), maxCoverSize);
    }
    
    /**
     * 验证文件格式
     */
    private void validateFileFormat(String fileName, String allowedTypes) throws Exception {
        if (StringUtils.isEmpty(fileName)) {
            throw new Exception("文件名不能为空");
        }
        
        String extension = getFileExtension(fileName).toLowerCase();
        String[] types = allowedTypes.split(",");
        
        boolean isValid = Arrays.stream(types)
            .map(String::trim)
            .map(String::toLowerCase)
            .anyMatch(type -> type.equals(extension));
        
        if (!isValid) {
            throw new Exception("不支持的文件格式: " + extension + ", 支持的格式: " + allowedTypes);
        }
    }
    
    /**
     * 验证文件大小
     */
    private void validateFileSize(long fileSize, String maxSizeStr) throws Exception {
        long maxSize = parseSize(maxSizeStr);
        if (fileSize > maxSize) {
            throw new Exception("文件大小超过限制: " + (fileSize / 1024 / 1024) + "MB, 最大允许: " + (maxSize / 1024 / 1024) + "MB");
        }
    }
    
    /**
     * 解析文件大小字符串
     */
    private long parseSize(String sizeStr) {
        if (StringUtils.isEmpty(sizeStr)) {
            return 0;
        }
        
        sizeStr = sizeStr.trim().toUpperCase();
        long multiplier = 1;
        
        if (sizeStr.endsWith("KB")) {
            multiplier = 1024;
            sizeStr = sizeStr.substring(0, sizeStr.length() - 2);
        } else if (sizeStr.endsWith("MB")) {
            multiplier = 1024 * 1024;
            sizeStr = sizeStr.substring(0, sizeStr.length() - 2);
        } else if (sizeStr.endsWith("GB")) {
            multiplier = 1024 * 1024 * 1024;
            sizeStr = sizeStr.substring(0, sizeStr.length() - 2);
        }
        
        try {
            return Long.parseLong(sizeStr.trim()) * multiplier;
        } catch (NumberFormatException e) {
            return 50 * 1024 * 1024; // 默认50MB
        }
    }
    
    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }
    
    /**
     * 从OSS URL中提取文件名
     */
    private String extractFileNameFromUrl(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return "";
        }
        try {
            // 从URL中提取最后一个/后面的部分作为文件名
            int lastSlashIndex = fileUrl.lastIndexOf('/');
            if (lastSlashIndex > 0 && lastSlashIndex < fileUrl.length() - 1) {
                return fileUrl.substring(lastSlashIndex + 1);
            }
        } catch (Exception e) {
            log.warn("从URL提取文件名失败: {}", fileUrl);
        }
        return "unknown_file";
    }
    
    /**
     * 提取音频信息
     */
    private AudioInfo extractAudioInfo(MultipartFile file) {
        AudioInfo audioInfo = new AudioInfo();
        
        try {
            // 创建临时文件
            File tempFile = File.createTempFile("temp_audio_", ".tmp");
            file.transferTo(tempFile);
            
            try {
                // 读取音频文件
                AudioFile audioFile = AudioFileIO.read(tempFile);
                
                // 获取音频长度
                audioInfo.setDuration(audioFile.getAudioHeader().getTrackLength());
                
                // 获取标签信息
                Tag tag = audioFile.getTag();
                if (tag != null) {
                    audioInfo.setTitle(tag.getFirst(FieldKey.TITLE));
                    audioInfo.setArtist(tag.getFirst(FieldKey.ARTIST));
                    audioInfo.setAlbum(tag.getFirst(FieldKey.ALBUM));
                }
            } finally {
                // 删除临时文件
                tempFile.delete();
            }
        } catch (Exception e) {
            log.warn("提取音频信息失败: {}", e.getMessage());
            // 设置默认值
            audioInfo.setDuration(0);
        }
        
        return audioInfo;
    }
    
    /**
     * 音频信息内部类
     */
    private static class AudioInfo {
        private int duration; // 时长（秒）
        private String title;
        private String artist;
        private String album;
        
        public int getDuration() {
            return duration;
        }
        
        public void setDuration(int duration) {
            this.duration = duration;
        }
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getArtist() {
            return artist;
        }
        
        public void setArtist(String artist) {
            this.artist = artist;
        }
        
        public String getAlbum() {
            return album;
        }
        
        public void setAlbum(String album) {
            this.album = album;
        }
    }
}
