package com.nyx.musicPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nyx.musicPlatform.entity.Banner;

import java.util.List;

/**
 * Banner管理服务接口
 *
 * @author nyx
 * @since 2025-06-31
 */
public interface BannerService extends IService<Banner> {

    /**
     * 获取启用状态的Banner列表
     *
     * @return Banner列表
     */
    List<Banner> getActiveBanners();

    /**
     * 获取所有Banner列表
     *
     * @return Banner列表
     */
    List<Banner> getAllBanners();

    /**
     * 创建Banner
     *
     * @param banner Banner信息
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean createBanner(Banner banner, Long userId);

    /**
     * 更新Banner
     *
     * @param banner Banner信息
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateBanner(Banner banner, Long userId);

    /**
     * 删除Banner
     *
     * @param id Banner ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteBanner(Long id, Long userId);

    /**
     * 启用/禁用Banner
     *
     * @param id Banner ID
     * @param status 状态
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateBannerStatus(Long id, Integer status, Long userId);
}