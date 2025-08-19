package com.nyx.musicPlatform.controller;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.service.FileStorageService;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传控制器
 * 
 * @author nyx
 * @since 2025-06
 */
@Tag(name = "文件上传", description = "文件上传相关接口")
@RestController
@RequestMapping("/api/upload")
@Slf4j
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private JwtUtils jwtUtils;

    // 允许的图片格式
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    // 最大文件大小 5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 上传封面图片
     */
    @Operation(summary = "上传封面图片", description = "上传封面图片接口")
    @PostMapping("/cover")
    public Result uploadCover(
            @Parameter(description = "封面图片文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        try {
            // 验证用户身份
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 验证文件
            if (file == null || file.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "请选择要上传的图片文件");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                return Result.error(ResultCode.PARAM_ERROR, "不支持的图片格式，仅支持 JPG、PNG、GIF、WEBP 格式");
            }

            // 验证文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                return Result.error(ResultCode.PARAM_ERROR, "图片文件大小不能超过 5MB");
            }

            // 上传文件
            String fileUrl = fileStorageService.uploadCoverFile(file);
            if (!StringUtils.hasText(fileUrl)) {
                return Result.error(ResultCode.SYSTEM_ERROR, "文件上传失败");
            }

            log.info("封面图片上传成功: {}", fileUrl);
            return Result.success(fileUrl, "上传成功");
            
        } catch (Exception e) {
            log.error("上传封面图片失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传Banner图片
     */
    @Operation(summary = "上传Banner图片", description = "上传Banner图片接口")
    @PostMapping("/banner")
    public Result uploadBanner(
            @Parameter(description = "Banner图片文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        try {
            // 验证用户身份
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            // 验证文件
            if (file == null || file.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "请选择要上传的Banner图片文件");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                return Result.error(ResultCode.PARAM_ERROR, "不支持的图片格式，仅支持 JPG、PNG、GIF、WEBP 格式");
            }

            // 验证文件大小 - Banner图片可以稍大一些，允许10MB
            long maxBannerSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxBannerSize) {
                return Result.error(ResultCode.PARAM_ERROR, "Banner图片文件大小不能超过 10MB");
            }

            // 上传文件到Banner目录
            String fileUrl = fileStorageService.uploadBannerFile(file);
            if (!StringUtils.hasText(fileUrl)) {
                return Result.error(ResultCode.SYSTEM_ERROR, "文件上传失败");
            }

            log.info("Banner图片上传成功: {}", fileUrl);
            return Result.success(fileUrl, "上传成功");
            
        } catch (Exception e) {
            log.error("上传Banner图片失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "上传失败: " + e.getMessage());
        }
    }
}