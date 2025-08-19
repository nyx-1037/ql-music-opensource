package com.nyx.musicPlatform.controller;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import com.nyx.musicPlatform.common.utils.JwtUtils;
import com.nyx.musicPlatform.entity.Banner;
import com.nyx.musicPlatform.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Banner管理控制器
 *
 * @author nyx
 * @since 2025-06-31
 */
@Slf4j
@RestController
@RequestMapping("/api/banner")
@Tag(name = "Banner管理", description = "Banner管理相关接口")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取启用状态的Banner列表
     */
    @Operation(summary = "获取启用Banner列表", description = "获取启用状态的Banner列表接口")
    @GetMapping("/active")
    public Result getActiveBanners() {
        try {
            List<Banner> banners = bannerService.getActiveBanners();
            return Result.success(banners, "获取成功");
        } catch (Exception e) {
            log.error("获取启用Banner列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "获取Banner列表失败");
        }
    }

    /**
     * 获取所有Banner列表（管理员接口）
     */
    @Operation(summary = "获取所有Banner列表", description = "获取所有Banner列表接口（管理员）")
    @GetMapping("/all")
    public Result getAllBanners(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            List<Banner> banners = bannerService.getAllBanners();
            return Result.success(banners, "获取成功");
        } catch (Exception e) {
            log.error("获取所有Banner列表失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "获取Banner列表失败");
        }
    }

    /**
     * 创建Banner
     */
    @Operation(summary = "创建Banner", description = "创建Banner接口")
    @PostMapping
    public Result createBanner(
            @RequestBody Banner banner,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            boolean result = bannerService.createBanner(banner, userId);
            
            if (result) {
                return Result.success("创建成功");
            } else {
                return Result.error(ResultCode.SYSTEM_ERROR, "创建失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.PARAM_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("创建Banner失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "创建失败");
        }
    }

    /**
     * 更新Banner
     */
    @Operation(summary = "更新Banner", description = "更新Banner接口")
    @PutMapping("/{id}")
    public Result updateBanner(
            @Parameter(description = "Banner ID") @PathVariable Long id,
            @RequestBody Banner banner,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            banner.setId(id);
            boolean result = bannerService.updateBanner(banner, userId);
            
            if (result) {
                return Result.success("更新成功");
            } else {
                return Result.error(ResultCode.SYSTEM_ERROR, "更新失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.PARAM_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("更新Banner失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "更新失败");
        }
    }

    /**
     * 删除Banner
     */
    @Operation(summary = "删除Banner", description = "删除Banner接口")
    @DeleteMapping("/{id}")
    public Result deleteBanner(
            @Parameter(description = "Banner ID") @PathVariable Long id,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            boolean result = bannerService.deleteBanner(id, userId);
            
            if (result) {
                return Result.success("删除成功");
            } else {
                return Result.error(ResultCode.SYSTEM_ERROR, "删除失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.PARAM_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("删除Banner失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "删除失败");
        }
    }

    /**
     * 更新Banner状态
     */
    @Operation(summary = "更新Banner状态", description = "启用/禁用Banner接口")
    @PutMapping("/{id}/status")
    public Result updateBannerStatus(
            @Parameter(description = "Banner ID") @PathVariable Long id,
            @Parameter(description = "状态(0:启用 1:禁用)") @RequestParam Integer status,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (!StringUtils.hasText(token) || !jwtUtils.validateToken(token)) {
                return Result.error(ResultCode.TOKEN_INVALID, "Token无效");
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            boolean result = bannerService.updateBannerStatus(id, status, userId);
            
            if (result) {
                return Result.success("状态更新成功");
            } else {
                return Result.error(ResultCode.SYSTEM_ERROR, "状态更新失败");
            }
        } catch (RuntimeException e) {
            return Result.error(ResultCode.PARAM_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("更新Banner状态失败", e);
            return Result.error(ResultCode.SYSTEM_ERROR, "状态更新失败");
        }
    }
}