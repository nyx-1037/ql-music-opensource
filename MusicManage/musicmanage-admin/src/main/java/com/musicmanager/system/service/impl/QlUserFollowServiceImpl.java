package com.musicmanager.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlUserFollowMapper;
import com.musicmanager.system.domain.QlUserFollow;
import com.musicmanager.system.service.IQlUserFollowService;

/**
 * 用户关注关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlUserFollowServiceImpl implements IQlUserFollowService 
{
    @Autowired
    private QlUserFollowMapper qlUserFollowMapper;

    /**
     * 查询用户关注关系
     * 
     * @param id 用户关注关系主键
     * @return 用户关注关系
     */
    @Override
    public QlUserFollow selectQlUserFollowById(Long id)
    {
        return qlUserFollowMapper.selectQlUserFollowById(id);
    }

    /**
     * 查询用户关注关系列表
     * 
     * @param qlUserFollow 用户关注关系
     * @return 用户关注关系
     */
    @Override
    public List<QlUserFollow> selectQlUserFollowList(QlUserFollow qlUserFollow)
    {
        return qlUserFollowMapper.selectQlUserFollowList(qlUserFollow);
    }

    /**
     * 新增用户关注关系
     * 
     * @param qlUserFollow 用户关注关系
     * @return 结果
     */
    @Override
    public int insertQlUserFollow(QlUserFollow qlUserFollow)
    {
        return qlUserFollowMapper.insertQlUserFollow(qlUserFollow);
    }

    /**
     * 修改用户关注关系
     * 
     * @param qlUserFollow 用户关注关系
     * @return 结果
     */
    @Override
    public int updateQlUserFollow(QlUserFollow qlUserFollow)
    {
        return qlUserFollowMapper.updateQlUserFollow(qlUserFollow);
    }

    /**
     * 批量删除用户关注关系
     * 
     * @param ids 需要删除的用户关注关系主键
     * @return 结果
     */
    @Override
    public int deleteQlUserFollowByIds(Long[] ids)
    {
        return qlUserFollowMapper.deleteQlUserFollowByIds(ids);
    }

    /**
     * 删除用户关注关系信息
     * 
     * @param id 用户关注关系主键
     * @return 结果
     */
    @Override
    public int deleteQlUserFollowById(Long id)
    {
        return qlUserFollowMapper.deleteQlUserFollowById(id);
    }
}
