package com.musicmanager.system.service;

import java.util.List;
import com.musicmanager.system.domain.QlBanner;

/**
 * BannerService接口
 * 
 * @author musicmanager
 * @date 2024-01-01
 */
public interface IQlBannerService 
{
    /**
     * 查询Banner
     * 
     * @param id Banner主键
     * @return Banner
     */
    public QlBanner selectQlBannerById(Long id);

    /**
     * 查询Banner列表
     * 
     * @param qlBanner Banner
     * @return Banner集合
     */
    public List<QlBanner> selectQlBannerList(QlBanner qlBanner);

    /**
     * 查询激活状态的Banner列表
     * 
     * @return Banner集合
     */
    public List<QlBanner> selectActiveBannerList();

    /**
     * 新增Banner
     * 
     * @param qlBanner Banner
     * @return 结果
     */
    public int insertQlBanner(QlBanner qlBanner);

    /**
     * 修改Banner
     * 
     * @param qlBanner Banner
     * @return 结果
     */
    public int updateQlBanner(QlBanner qlBanner);

    /**
     * 批量删除Banner
     * 
     * @param ids 需要删除的Banner主键集合
     * @return 结果
     */
    public int deleteQlBannerByIds(Long[] ids);

    /**
     * 删除Banner信息
     * 
     * @param id Banner主键
     * @return 结果
     */
    public int deleteQlBannerById(Long id);
}