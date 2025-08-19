package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Banner管理 Mapper 接口
 *
 * @author nyx
 * @since 2025-06-31
 */
@Mapper
public interface BannerMapper extends BaseMapper<Banner> {

    /**
     * 获取启用状态的Banner列表，按排序顺序排列
     *
     * @return Banner列表
     */
    @Select("SELECT * FROM banner_management WHERE status = 0 ORDER BY sort_order ASC, create_time DESC LIMIT 10")
    List<Banner> getActiveBanners();

    /**
     * 获取所有Banner列表，按排序顺序排列
     *
     * @return Banner列表
     */
    @Select("SELECT * FROM banner_management ORDER BY sort_order ASC, create_time DESC")
    List<Banner> getAllBanners();
}