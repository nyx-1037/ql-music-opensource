package com.nyx.musicPlatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nyx.musicPlatform.entity.Banner;
import com.nyx.musicPlatform.mapper.BannerMapper;
import com.nyx.musicPlatform.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Banner管理服务实现类
 *
 * @author nyx
 * @since 2025-06-31
 */
@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> getActiveBanners() {
        try {
            return baseMapper.getActiveBanners();
        } catch (Exception e) {
            log.error("获取启用Banner列表失败", e);
            throw new RuntimeException("获取Banner列表失败");
        }
    }

    @Override
    public List<Banner> getAllBanners() {
        try {
            return baseMapper.getAllBanners();
        } catch (Exception e) {
            log.error("获取所有Banner列表失败", e);
            throw new RuntimeException("获取Banner列表失败");
        }
    }

    @Override
    public boolean createBanner(Banner banner, Long userId) {
        try {
            // 参数验证
            if (!StringUtils.hasText(banner.getTitle())) {
                throw new RuntimeException("Banner标题不能为空");
            }
            if (!StringUtils.hasText(banner.getImageUrl())) {
                throw new RuntimeException("Banner图片地址不能为空");
            }

            // 设置默认值
            if (banner.getStatus() == null) {
                banner.setStatus(1);
            }
            if (banner.getSortOrder() == null) {
                banner.setSortOrder(0);
            }
            if (!StringUtils.hasText(banner.getActionType())) {
                banner.setActionType("explore");
            }

            banner.setCreatorId(userId);
            banner.setCreateTime(LocalDateTime.now());
            banner.setUpdateTime(LocalDateTime.now());

            return save(banner);
        } catch (Exception e) {
            log.error("创建Banner失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updateBanner(Banner banner, Long userId) {
        try {
            if (banner.getId() == null) {
                throw new RuntimeException("Banner ID不能为空");
            }

            Banner existingBanner = getById(banner.getId());
            if (existingBanner == null) {
                throw new RuntimeException("Banner不存在");
            }

            // 参数验证
            if (StringUtils.hasText(banner.getTitle()) && banner.getTitle().length() > 100) {
                throw new RuntimeException("Banner标题长度不能超过100个字符");
            }
            if (StringUtils.hasText(banner.getDescription()) && banner.getDescription().length() > 255) {
                throw new RuntimeException("Banner描述长度不能超过255个字符");
            }

            banner.setUpdateTime(LocalDateTime.now());
            return updateById(banner);
        } catch (Exception e) {
            log.error("更新Banner失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean deleteBanner(Long id, Long userId) {
        try {
            if (id == null) {
                throw new RuntimeException("Banner ID不能为空");
            }

            Banner banner = getById(id);
            if (banner == null) {
                throw new RuntimeException("Banner不存在");
            }

            return removeById(id);
        } catch (Exception e) {
            log.error("删除Banner失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updateBannerStatus(Long id, Integer status, Long userId) {
        try {
            if (id == null) {
                throw new RuntimeException("Banner ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                throw new RuntimeException("状态值无效");
            }

            Banner banner = getById(id);
            if (banner == null) {
                throw new RuntimeException("Banner不存在");
            }

            banner.setStatus(status);
            banner.setUpdateTime(LocalDateTime.now());
            return updateById(banner);
        } catch (Exception e) {
            log.error("更新Banner状态失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}