package com.nyx.musicPlatform.controller;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.entity.MusicCategory;
import com.nyx.musicPlatform.service.MusicCategoryService;
import com.nyx.musicPlatform.vo.MusicVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 音乐分类控制器
 *
 * @author nyx
 * @since 2025-06-01
 */
@RestController
@RequestMapping("/api/category")
@Tag(name = "音乐分类管理", description = "音乐分类管理相关接口")
public class MusicCategoryController {

    @Autowired
    private MusicCategoryService musicCategoryService;

    @GetMapping("/list")
    @Operation(summary = "获取所有分类", description = "获取所有分类接口")
    public Result<List<MusicCategory>> getAllCategories() {
        return musicCategoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}/music")
    @Operation(summary = "根据分类获取音乐列表", description = "根据分类获取音乐列表接口")
    public Result<List<MusicVO>> getMusicByCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        return musicCategoryService.getMusicByCategory(categoryId);
    }

    @PostMapping("/music/{musicId}/categories")
    @Operation(summary = "为音乐设置分类", description = "为音乐设置分类接口")
    public Result<Void> setMusicCategories(
            @Parameter(description = "音乐ID") @PathVariable Long musicId,
            @Parameter(description = "分类ID列表") @RequestBody List<Long> categoryIds) {
        return musicCategoryService.setMusicCategories(musicId, categoryIds);
    }

    @GetMapping("/music/{musicId}/categories")
    @Operation(summary = "获取音乐的分类", description = "获取音乐的分类接口")
    public Result<List<MusicCategory>> getMusicCategories(
            @Parameter(description = "音乐ID") @PathVariable Long musicId) {
        return musicCategoryService.getMusicCategories(musicId);
    }

}