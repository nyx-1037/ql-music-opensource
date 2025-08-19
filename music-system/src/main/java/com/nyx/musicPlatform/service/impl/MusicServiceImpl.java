package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.common.exception.BusinessException;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.mapper.MusicMapper;
import com.nyx.musicPlatform.service.FileStorageService;
import com.nyx.musicPlatform.service.MusicService;
import com.nyx.musicPlatform.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.UUID;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 音乐服务实现类
 * 
 * @author nyx
 * @since 2025-06
 */
@Slf4j
@Service
@Transactional
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${music.file.allowed-music-types}")
    private String allowedMusicTypes;

    @Value("${music.file.allowed-cover-types}")
    private String allowedCoverTypes;

    @Value("${music.file.max-music-size}")
    private String maxMusicSize;

    @Value("${music.file.max-cover-size}")
    private String maxCoverSize;

    private static final String CACHE_KEY_PREFIX = "music:";
    private static final String HOT_MUSIC_KEY = "music:hot";
    private static final String LATEST_MUSIC_KEY = "music:latest";
    private static final int CACHE_EXPIRE_HOURS = 24;

    @Override
    public Music uploadMusic(MultipartFile file, String title, String artist, String album, 
                           MultipartFile cover, String genre, String releaseYear, String description, 
                           String tags, Boolean isPublic, Boolean allowDownload, Boolean allowComment, Long userId) {
        try {
            // 验证文件
            validateMusicFile(file);

            // 生成唯一文件名
            String fileName = fileStorageService.generateUniqueFileName(file.getOriginalFilename());

            // 上传文件
            String fileUrl = fileStorageService.uploadMusicFile(file, fileName);

            // 提取音频信息
            AudioInfo audioInfo = extractAudioInfo(file);

            // 创建音乐实体
            Music music = new Music();
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setGenre(genre);
            music.setFilePath(fileUrl);
            music.setFileName(fileName);
            music.setOriginalName(file.getOriginalFilename());
            music.setFileType("mp3");
            music.setFileSize(file.getSize());
            music.setDuration(audioInfo.getDuration());
            music.setCreateTime(LocalDateTime.now());
            music.setPlayCount(0);
            music.setUserId(userId);
            // 处理发布年份转换
            if (StringUtils.hasText(releaseYear)) {
                try {
                    music.setReleaseYear(Integer.parseInt(releaseYear));
                } catch (NumberFormatException e) {
                    log.warn("发布年份格式错误: {}", releaseYear);
                }
            }
            music.setDescription(description);
            music.setTags(tags);
            music.setIsPublic(isPublic != null && isPublic ? 1 : 0);
            music.setAllowDownload(allowDownload != null && allowDownload ? 1 : 0);
            music.setAllowComment(allowComment != null && allowComment ? 1 : 0);

            // 处理封面上传
            if (cover != null && !cover.isEmpty()) {
                try {
                    validateCoverFile(cover);
                    String coverUrl = fileStorageService.uploadCoverFile(cover);
                    music.setCoverUrl(coverUrl);
                } catch (Exception e) {
                    log.warn("封面上传失败，继续上传音乐: {}", e.getMessage());
                }
            }

            // 保存到数据库
            save(music);

            // 清除相关缓存
            clearMusicCache();

            log.info("音乐上传成功: {} - 存储类型: {}", fileName, fileStorageService.getStorageType());
            return music;
        } catch (Exception e) {
            log.error("音乐上传失败", e);
            throw new BusinessException("音乐上传失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<Music> getMusicByUserId(Page<Music> page, Long userId) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> getMusicList(Page<Music> page) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> searchByTitle(Page<Music> page, String title) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("play_count");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> searchByArtist(Page<Music> page, String artist) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("artist", artist);
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("play_count");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> searchByAlbum(Page<Music> page, String album) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("album", album);
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("play_count");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> searchMusic(Page<Music> page, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return new Page<>();
        }

        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
            .like("title", keyword)
            .or().like("artist", keyword)
            .or().like("album", keyword)
        );
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("play_count");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> getHotMusic(Page<Music> page) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("play_count");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> getLatestMusic(Page<Music> page) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_public", true);
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    public IPage<Music> getMusicListWithFilter(Page<Music> page, String sort, String genre, String duration, String search) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_public", true);
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_deleted", 0);
        
        // 搜索关键词筛选
        if (StringUtils.hasText(search)) {
            queryWrapper.and(wrapper -> wrapper
                .like("title", search)
                .or().like("artist", search)
                .or().like("album", search)
            );
        }
        
        // 流派筛选
        if (StringUtils.hasText(genre)) {
            queryWrapper.eq("genre", genre);
        }
        
        // 时长筛选
        if (StringUtils.hasText(duration)) {
            switch (duration) {
                case "short":
                    queryWrapper.lt("duration", 180); // 小于3分钟
                    break;
                case "medium":
                    queryWrapper.between("duration", 180, 360); // 3-6分钟
                    break;
                case "long":
                    queryWrapper.gt("duration", 360); // 大于6分钟
                    break;
            }
        }
        
        // 排序
        if (StringUtils.hasText(sort)) {
            switch (sort) {
                case "latest":
                    queryWrapper.orderByDesc("create_time");
                    break;
                case "popular":
                case "playCount":
                    queryWrapper.orderByDesc("play_count");
                    break;
                case "title":
                    queryWrapper.orderByAsc("title");
                    break;
                case "artist":
                    queryWrapper.orderByAsc("artist");
                    break;
                default:
                    queryWrapper.orderByDesc("create_time");
                    break;
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        
        return page(page, queryWrapper);
    }

    @Override
    public List<String> getAllGenres() {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT genre");
        queryWrapper.isNotNull("genre");
        queryWrapper.ne("genre", "");
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByAsc("genre");
        
        List<Music> musicList = list(queryWrapper);
        List<String> genres = new ArrayList<>();
        for (Music music : musicList) {
            if (StringUtils.hasText(music.getGenre())) {
                genres.add(music.getGenre());
            }
        }
        return genres;
    }

    @Override
    public boolean playMusic(Long musicId) {
        Music music = getById(musicId);
        if (music != null) {
            music.setPlayCount(music.getPlayCount() + 1);
            updateById(music);
            clearMusicCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMusic(Long musicId, Long userId) {
        try {
            Music music = getById(musicId);
            if (music == null || !music.getUserId().equals(userId)) {
                return false;
            }

            // 删除文件
            if (StringUtils.hasText(music.getFilePath())) {
                boolean deleted = fileStorageService.deleteFile(music.getFilePath());
                if (!deleted) {
                    log.warn("删除音乐文件失败: {}", music.getFilePath());
                }
            }

            // 删除封面
            if (StringUtils.hasText(music.getCoverUrl())) {
                boolean deleted = fileStorageService.deleteFile(music.getCoverUrl());
                if (!deleted) {
                    log.warn("删除封面文件失败: {}", music.getCoverUrl());
                }
            }

            // 从数据库删除
            boolean result = removeById(musicId);
            
            if (result) {
                // 清除相关缓存
                clearMusicCache();
                log.info("音乐删除成功: {} - 存储类型: {}", music.getFileName(), fileStorageService.getStorageType());
            }
            
            return result;
        } catch (Exception e) {
            log.error("删除音乐失败", e);
            return false;
        }
    }

    @Override
    public String uploadMusicCover(Long musicId, MultipartFile file, Long userId) {
        try {
            Music music = getById(musicId);
            if (music == null || !music.getUserId().equals(userId)) {
                throw new BusinessException("音乐不存在或无权限");
            }

            // 验证封面文件
            validateCoverFile(file);

            // 删除旧封面
            if (StringUtils.hasText(music.getCoverUrl())) {
                fileStorageService.deleteFile(music.getCoverUrl());
            }

            // 生成唯一文件名
            String fileName = fileStorageService.generateUniqueFileName(file.getOriginalFilename());

            // 上传封面
            String coverUrl = fileStorageService.uploadCoverFile(file);

            // 更新数据库
            music.setCoverUrl(coverUrl);
            updateById(music);

            // 清除相关缓存
            clearMusicCache();

            log.info("封面上传成功: {} - 存储类型: {}", fileName, fileStorageService.getStorageType());
            return coverUrl;
        } catch (Exception e) {
            log.error("封面上传失败", e);
            throw new BusinessException("封面上传失败: " + e.getMessage());
        }
    }

    @Override
    public String getMusicCoverPath(Long musicId) {
        Music music = getById(musicId);
        return music != null ? music.getCoverUrl() : null;
    }

    @Override
    public Object getMusicStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMusic", count());
        stats.put("totalPlayCount", musicMapper.getTotalPlayCount());
        stats.put("totalLikeCount", musicMapper.getTotalLikeCount());
        stats.put("genreStats", musicMapper.getGenreStats());
        return stats;
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
    public boolean validateFileSize(MultipartFile file) {
        try {
            validateFileSize(file.getSize(), maxMusicSize);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    @Override
    public boolean validateFileFormat(MultipartFile file) {
        try {
            validateFileFormat(file.getOriginalFilename(), allowedMusicTypes);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    @Override
    public String getMusicFilePath(Long musicId) {
        Music music = getById(musicId);
        if (music == null) {
            throw new BusinessException("音乐不存在");
        }
        return music.getFilePath();
    }

    @Override
    public byte[] getMusicFile(Long musicId) {
        Music music = getById(musicId);
        if (music == null) {
            throw new BusinessException("音乐不存在");
        }
        
        String filePath = music.getFilePath();
        try (InputStream inputStream = fileStorageService.getFileStream(filePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("读取音乐文件失败: {}", e.getMessage(), e);
            throw new BusinessException("音乐文件读取失败");
        }
    }

    @Override
    public List<Music> getRecommendMusic(int limit) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("play_count");
        queryWrapper.last("LIMIT " + limit);
        return list(queryWrapper);
    }

    @Override
    public IPage<Music> getRandomRecommendMusic(Page<Music> page) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.eq("is_public", 1);
        queryWrapper.last("ORDER BY RAND()");
        return page(page, queryWrapper);
    }

    @Override
    public long getTotalMusicCount() {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_deleted", 0);
        return count(queryWrapper);
    }

    @Override
    public long getUserMusicCount(Long userId) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_deleted", 0);
        return count(queryWrapper);
    }

    @Override
    public boolean existsByTitleAndArtist(String title, String artist, Long userId) {
        return musicMapper.existsByTitleAndArtistAndUserId(title, artist, userId);
    }

    @Override
    public Music getMusicByFileName(String fileName) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileName);
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_deleted", 0);
        return getOne(queryWrapper);
    }

    @Override
    public boolean deleteMusicBatch(List<Long> musicIds, Long userId) {
        if (musicIds == null || musicIds.isEmpty()) {
            return false;
        }
        
        try {
            for (Long musicId : musicIds) {
                deleteMusic(musicId, userId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量删除音乐失败", e);
            return false;
        }
    }

    @Override
    public boolean updateMusicInfo(Long musicId, String title, String artist, String album, Long userId) {
        try {
            Music music = getById(musicId);
            if (music == null || !music.getUserId().equals(userId)) {
                return false;
            }
            
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setUpdateTime(LocalDateTime.now());
            
            boolean result = updateById(music);
            if (result) {
                clearMusicCache();
            }
            return result;
        } catch (Exception e) {
            log.error("更新音乐信息失败", e);
            return false;
        }
    }

    @Override
    public boolean updateMusicStatus(Long musicId, Integer status) {
        try {
            Music music = getById(musicId);
            if (music == null) {
                return false;
            }
            
            music.setStatus(status);
            music.setUpdateTime(LocalDateTime.now());
            
            boolean result = updateById(music);
            if (result) {
                clearMusicCache();
            }
            return result;
        } catch (Exception e) {
            log.error("更新音乐状态失败", e);
            return false;
        }
    }

    /**
     * 验证音乐文件
     */
    private void validateMusicFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 验证文件格式
        validateFileFormat(file.getOriginalFilename(), allowedMusicTypes);

        // 验证文件大小
        validateFileSize(file.getSize(), maxMusicSize);
    }

    /**
     * 验证封面文件
     */
    private void validateCoverFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 验证文件格式
        validateFileFormat(file.getOriginalFilename(), allowedCoverTypes);

        // 验证文件大小
        validateFileSize(file.getSize(), maxCoverSize);
    }

    /**
     * 验证文件格式
     */
    private void validateFileFormat(String fileName, String allowedTypes) {
        if (!StringUtils.hasText(fileName)) {
            throw new BusinessException("文件名不能为空");
        }

        String extension = getFileExtension(fileName).toLowerCase();
        String[] types = allowedTypes.split(",");
        
        boolean isValid = Arrays.stream(types)
            .map(String::trim)
            .map(String::toLowerCase)
            .anyMatch(type -> type.equals(extension));

        if (!isValid) {
            throw new BusinessException("不支持的文件格式: " + extension + ", 支持的格式: " + allowedTypes);
        }
    }

    /**
     * 验证文件大小
     */
    private void validateFileSize(long fileSize, String maxSizeStr) {
        long maxSize = parseSize(maxSizeStr);
        if (fileSize > maxSize) {
            throw new BusinessException("文件大小超过限制: " + (fileSize / 1024 / 1024) + "MB, 最大允许: " + (maxSize / 1024 / 1024) + "MB");
        }
    }

    /**
     * 解析文件大小字符串
     */
    private long parseSize(String sizeStr) {
        if (!StringUtils.hasText(sizeStr)) {
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
            throw new BusinessException("无效的文件大小格式: " + sizeStr);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    /**
     * 清除音乐相关缓存
     */
    private void clearMusicCache() {
        Set<String> keys = redisTemplate.keys(CACHE_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        
        // 清除热门和最新音乐缓存
        Set<String> hotKeys = redisTemplate.keys(HOT_MUSIC_KEY + "*");
        if (hotKeys != null && !hotKeys.isEmpty()) {
            redisTemplate.delete(hotKeys);
        }
        
        Set<String> latestKeys = redisTemplate.keys(LATEST_MUSIC_KEY + "*");
        if (latestKeys != null && !latestKeys.isEmpty()) {
            redisTemplate.delete(latestKeys);
        }
    }

    /**
     * 音频信息内部类
     */
    private static class AudioInfo {
        private int duration; // 时长（秒）
        private String title;
        private String artist;
        private String album;
        private byte[] albumArt;

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

        public byte[] getAlbumArt() {
            return albumArt;
        }

        public void setAlbumArt(byte[] albumArt) {
            this.albumArt = albumArt;
        }
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
                    
                    // 获取专辑封面
                    if (tag.getFirstArtwork() != null) {
                        audioInfo.setAlbumArt(tag.getFirstArtwork().getBinaryData());
                    }
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
     * 获取默认封面
     */
    private byte[] getDefaultCover() {
        try {
            // 创建一个简单的默认封面图片
            BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
            
            // 填充背景色
            for (int x = 0; x < 300; x++) {
                for (int y = 0; y < 300; y++) {
                    // 渐变效果
                    int rgb = (x + y) % 255;
                    int color = (rgb << 16) | (rgb << 8) | rgb;
                    image.setRGB(x, y, color);
                }
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成默认封面失败", e);
            return new byte[0];
        }
    }

    /**
     * 根据文件路径获取图片内容类型
     */
    private String getImageContentType(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return "image/png";
        }
        
        String extension = getFileExtension(filePath).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "image/png";
        }
    }
}