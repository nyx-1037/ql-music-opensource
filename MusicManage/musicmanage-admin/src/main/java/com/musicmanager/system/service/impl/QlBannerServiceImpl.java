package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlBannerMapper;
import com.musicmanager.system.domain.QlBanner;
import com.musicmanager.system.service.IQlBannerService;

/**
 * BannerService业务层处理
 * 
 * @author musicmanager
 * @date 2024-01-01
 */
@Service
public class QlBannerServiceImpl implements IQlBannerService 
{
    @Autowired
    private QlBannerMapper qlBannerMapper;

    /**
     * 查询Banner
     * 
     * @param id Banner主键
     * @return Banner
     */
    @Override
    public QlBanner selectQlBannerById(Long id)
    {
        return qlBannerMapper.selectQlBannerById(id);
    }

    /**
     * 查询Banner列表
     * 
     * @param qlBanner Banner
     * @return Banner
     */
    @Override
    public List<QlBanner> selectQlBannerList(QlBanner qlBanner)
    {
        return qlBannerMapper.selectQlBannerList(qlBanner);
    }

    /**
     * 查询激活状态的Banner列表
     * 
     * @return Banner集合
     */
    @Override
    public List<QlBanner> selectActiveBannerList()
    {
        return qlBannerMapper.selectActiveBannerList();
    }

    /**
     * 新增Banner
     * 
     * @param qlBanner Banner
     * @return 结果
     */
    @Override
    public int insertQlBanner(QlBanner qlBanner)
    {
        qlBanner.setCreateTime(DateUtils.getNowDate());
        return qlBannerMapper.insertQlBanner(qlBanner);
    }

    /**
     * 修改Banner
     * 
     * @param qlBanner Banner
     * @return 结果
     */
    @Override
    public int updateQlBanner(QlBanner qlBanner)
    {
        qlBanner.setUpdateTime(DateUtils.getNowDate());
        return qlBannerMapper.updateQlBanner(qlBanner);
    }

    /**
     * 批量删除Banner
     * 
     * @param ids 需要删除的Banner主键
     * @return 结果
     */
    @Override
    public int deleteQlBannerByIds(Long[] ids)
    {
        return qlBannerMapper.deleteQlBannerByIds(ids);
    }

    /**
     * 删除Banner信息
     * 
     * @param id Banner主键
     * @return 结果
     */
    @Override
    public int deleteQlBannerById(Long id)
    {
        return qlBannerMapper.deleteQlBannerById(id);
    }
}