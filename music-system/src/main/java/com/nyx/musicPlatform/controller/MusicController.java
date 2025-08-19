package com.nyx.musicPlatform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.entity.Music;
import com.nyx.musicPlatform.service.MusicService;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音乐控制器
 * 
 * @author nyx
 * @since 2025-06
 */
@Tag(name = "音乐管理", description = "音乐管理相关接口")
@RestController
@RequestMapping("/api/music")
@Slf4j
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 上传音乐
     */
    @Operation(summary = "上传音乐", description = "上传音乐文件接口")
    @PostMapping("/upload")
    public Result uploadMusic(
            @Parameter(description = "音乐文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "音乐标题") @RequestParam String title,
            @Parameter(description = "艺术家") @RequestParam String artist,
            @Parameter(description = "专辑") @RequestParam(required = false) String album,
            @Parameter(description = "封面图片") @RequestParam(value = "cover", required = false) MultipartFile cover,
            @Parameter(description = "音乐流派") @RequestParam(required = false) String genre,
            @Parameter(description = "发布年份") @RequestParam(required = false) String releaseYear,
            @Parameter(description = "音乐描述") @RequestParam(required = false) String description,
            @Parameter(description = "音乐标签") @RequestParam(required = false) String tags,
            @Parameter(description = "是否公开") @RequestParam(defaultValue = "true") Boolean isPublic,
            @Parameter(description = "允许下载") @RequestParam(defaultValue = "true") Boolean allowDownload,
            @Parameter(description = "允许评论") @RequestParam(defaultValue = "true") Boolean allowComment,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 参数验证
            if (file == null || file.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "请选择要上传的音乐文件");
            }

            if (!StringUtils.hasText(title) || !StringUtils.hasText(artist)) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐标题和艺术家不能为空");
            }

            // 标题和艺术家长度验证
            if (title.length() > 100) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐标题长度不能超过100个字符");
            }
            if (artist.length() > 100) {
                return Result.error(ResultCode.PARAM_ERROR, "艺术家名称长度不能超过100个字符");
            }
            if (StringUtils.hasText(album) && album.length() > 100) {
                return Result.error(ResultCode.PARAM_ERROR, "专辑名称长度不能超过100个字符");
            }

            Music music = musicService.uploadMusic(file, title, artist, album, cover, 
                    genre, releaseYear, description, tags, isPublic, allowDownload, allowComment, userId);
            if (music != null) {
                return Result.success(music, "音乐上传成功");
            } else {
                return Result.error(ResultCode.MUSIC_UPLOAD_FAILED, "音乐上传失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.MUSIC_UPLOAD_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐列表
     */
    @Operation(summary = "获取音乐列表", description = "分页获取音乐列表接口")
    @GetMapping("/list")
    public Result getMusicList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "排序方式") @RequestParam(required = false) String sort,
            @Parameter(description = "音乐流派") @RequestParam(required = false) String genre,
            @Parameter(description = "时长筛选") @RequestParam(required = false) String duration,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String search) {
        
        try {
            Page<Music> pageObj = new Page<>(page, limit);
            IPage<Music> musicPage = musicService.getMusicListWithFilter(pageObj, sort, genre, duration, search);
            
            return Result.success(musicPage, "查询成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取热门音乐列表
     */
    @Operation(summary = "获取热门音乐列表", description = "获取热门音乐列表接口")
    @GetMapping("/hot")
    public Result getHotMusic(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int limit) {
        
        try {
            Page<Music> pageObj = new Page<>(page, limit);
            IPage<Music> musicPage = musicService.getHotMusic(pageObj);
            
            return Result.success(musicPage, "查询成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取最新音乐列表
     */
    @Operation(summary = "获取最新音乐列表", description = "获取最新音乐列表接口")
    @GetMapping("/latest")
    public Result getLatestMusic(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int limit) {
        
        try {
            Page<Music> pageObj = new Page<>(page, limit);
            IPage<Music> musicPage = musicService.getLatestMusic(pageObj);
            
            return Result.success(musicPage, "查询成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐流派列表
     */
    @Operation(summary = "获取音乐流派列表", description = "获取音乐流派列表接口")
    @GetMapping("/genres")
    public Result getMusicGenres() {
        try {
            List<String> genres = musicService.getAllGenres();
            return Result.success(genres, "查询成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取随机推荐音乐列表
     */
    @Operation(summary = "获取随机推荐音乐列表", description = "获取随机推荐音乐列表接口")
    @GetMapping("/recommend")
    public Result getRandomRecommendMusic(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int limit) {
        
        try {
            Page<Music> pageObj = new Page<>(page, limit);
            IPage<Music> musicPage = musicService.getRandomRecommendMusic(pageObj);
            
            return Result.success(musicPage, "查询成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }







    /**
     * 搜索音乐
     */
    @Operation(summary = "搜索音乐", description = "搜索音乐接口")
    @GetMapping("/search")
    public Result searchMusic(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        try {
            if (!StringUtils.hasText(keyword)) {
                return Result.error(ResultCode.PARAM_ERROR, "搜索关键词不能为空");
            }

            Page<Music> page = new Page<>(current, size);
            IPage<Music> musicPage = musicService.searchMusic(page, keyword.trim());
            
            return Result.success(musicPage, "搜索成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }







    /**
     * 播放音乐（增加播放次数）
     */
    @Operation(summary = "播放音乐", description = "播放音乐接口")
    @PostMapping("/play/{musicId}")
    public Result playMusic(@Parameter(description = "音乐ID") @PathVariable Long musicId) {
        try {
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            boolean result = musicService.playMusic(musicId);
            if (result) {
                return Result.success("播放记录成功");
            } else {
                return Result.error(ResultCode.MUSIC_NOT_FOUND, "音乐不存在");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐播放URL
     */
    @Operation(summary = "获取音乐播放URL", description = "获取音乐播放URL接口")
    @GetMapping("/url/{musicId}")
    public Result getMusicUrl(@Parameter(description = "音乐ID") @PathVariable Long musicId) {
        try {
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            Music music = musicService.getById(musicId);
            if (music == null || music.getStatus() != 1) {
                return Result.error(ResultCode.MUSIC_NOT_FOUND, "音乐不存在或已禁用");
            }

            // 直接返回阿里云OSS的音乐文件URL
            String musicUrl = music.getFilePath();
            Map<String, String> result = new HashMap<>();
            result.put("url", musicUrl);
            return Result.success(result, "获取成功");
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐文件流
     */
    @Operation(summary = "获取音乐文件流", description = "获取音乐文件流接口")
    @GetMapping("/stream/{musicId}")
    public ResponseEntity<InputStreamResource> streamMusic(@Parameter(description = "音乐ID") @PathVariable Long musicId) {
        try {
            if (musicId == null) {
                return ResponseEntity.badRequest().build();
            }

            String filePath = musicService.getMusicFilePath(musicId);
            if (!StringUtils.hasText(filePath)) {
                return ResponseEntity.notFound().build();
            }

            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 获取音乐信息
            Music music = musicService.getById(musicId);
            if (music == null || music.getStatus() != 1) {
                return ResponseEntity.notFound().build();
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + music.getOriginalName() + "\"");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    /**
     * 获取音乐详情
     */
    @Operation(summary = "获取音乐详情", description = "获取音乐详情接口")
    @GetMapping("/{musicId}")
    public Result getMusicDetail(@Parameter(description = "音乐ID") @PathVariable Long musicId) {
        try {
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            Music music = musicService.getById(musicId);
            if (music != null && music.getStatus() == 1) {
                return Result.success(music, "查询成功");
            } else {
                return Result.error(ResultCode.MUSIC_NOT_FOUND, "音乐不存在");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 更新音乐信息
     */
    @Operation(summary = "更新音乐信息", description = "更新音乐信息接口")
    @PutMapping("/{musicId}")
    public Result updateMusic(
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            @Parameter(description = "音乐标题") @RequestParam(required = false) String title,
            @Parameter(description = "艺术家") @RequestParam(required = false) String artist,
            @Parameter(description = "专辑") @RequestParam(required = false) String album,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            // 长度验证
            if (StringUtils.hasText(title) && title.length() > 100) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐标题长度不能超过100个字符");
            }
            if (StringUtils.hasText(artist) && artist.length() > 100) {
                return Result.error(ResultCode.PARAM_ERROR, "艺术家名称长度不能超过100个字符");
            }
            if (StringUtils.hasText(album) && album.length() > 100) {
                return Result.error(ResultCode.PARAM_ERROR, "专辑名称长度不能超过100个字符");
            }

            boolean result = musicService.updateMusicInfo(musicId, title, artist, album, userId);
            if (result) {
                return Result.success("更新成功");
            } else {
                return Result.error(ResultCode.MUSIC_UPDATE_FAILED, "更新失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.MUSIC_UPDATE_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 删除音乐
     */
    @Operation(summary = "删除音乐", description = "删除音乐接口")
    @DeleteMapping("/{musicId}")
    public Result deleteMusic(
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }

            boolean result = musicService.deleteMusic(musicId, userId);
            if (result) {
                return Result.success("删除成功");
            } else {
                return Result.error(ResultCode.MUSIC_DELETE_FAILED, "删除失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.MUSIC_DELETE_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }



    /**
     * 上传音乐封面图片
     */
    @Operation(summary = "上传音乐封面图片", description = "上传音乐封面图片接口")
    @PostMapping("/cover/upload")
    public Result uploadMusicCover(
            @Parameter(description = "音乐ID") @RequestParam Long musicId,
            @Parameter(description = "封面图片文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 参数验证
            if (musicId == null) {
                return Result.error(ResultCode.PARAM_ERROR, "音乐ID不能为空");
            }
            
            if (file == null || file.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "请选择要上传的封面图片文件");
            }

            String coverPath = musicService.uploadMusicCover(musicId, file, userId);
            if (StringUtils.hasText(coverPath)) {
                return Result.success(coverPath, "封面图片上传成功");
            } else {
                return Result.error(ResultCode.MUSIC_UPLOAD_FAILED, "封面图片上传失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.MUSIC_UPLOAD_FAILED, e.getMessage());
        } catch (Exception e) {
            return Result.error(ResultCode.SYSTEM_ERROR, "系统错误");
        }
    }

    /**
     * 获取音乐封面图片
     */
    @Operation(summary = "获取音乐封面图片", description = "获取音乐封面图片接口")
    @GetMapping("/cover/{id}")
    public ResponseEntity<byte[]> getMusicCover(@Parameter(description = "音乐ID") @PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().build();
            }

            // 获取音乐信息
            Music music = musicService.getById(id);
            if (music == null || music.getStatus() != 1) {
                return ResponseEntity.notFound().build();
            }

            // 直接重定向到OSS URL
            String coverPath = music.getCoverUrl();
            if (StringUtils.hasText(coverPath)) {
                // 重定向到OSS文件URL
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(coverPath));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            } else {
                // 返回默认封面
                return createDefaultCoverResponse();
            }
        } catch (Exception e) {
             log.error("获取音乐封面失败: {}", e.getMessage());
             return createDefaultCoverResponse();
         }
     }

    /**
     * 创建默认封面响应（1x1像素透明PNG）
     */
    private ResponseEntity<byte[]> createDefaultCoverResponse() {
        try {
            // 创建1x1像素透明PNG的字节数组
            byte[] transparentPng = {
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, // PNG signature
                0x00, 0x00, 0x00, 0x0D, // IHDR chunk size
                0x49, 0x48, 0x44, 0x52, // IHDR
                0x00, 0x00, 0x00, 0x01, // width = 1
                0x00, 0x00, 0x00, 0x01, // height = 1
                0x08, 0x06, 0x00, 0x00, 0x00, // bit depth = 8, color type = 6 (RGBA)
                0x1F, 0x15, (byte) 0xC4, (byte) 0x89, // CRC
                0x00, 0x00, 0x00, 0x0B, // IDAT chunk size
                0x49, 0x44, 0x41, 0x54, // IDAT
                0x78, (byte) 0x9C, 0x62, 0x00, 0x02, 0x00, 0x00, 0x05, 0x00, 0x01, (byte) 0x0D, // compressed data
                0x0A, 0x2D, (byte) 0xB4, // CRC
                0x00, 0x00, 0x00, 0x00, // IEND chunk size
                0x49, 0x45, 0x4E, 0x44, // IEND
                (byte) 0xAE, 0x42, 0x60, (byte) 0x82 // CRC
            };

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"default-cover.png\"");
            headers.add(HttpHeaders.CACHE_CONTROL, "max-age=3600");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(transparentPng.length)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(transparentPng);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}