package com.musicmanager.system.mapper;

import java.util.List;
import com.musicmanager.system.domain.QlUserFollow;

/**
 * 用户关注关系Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface QlUserFollowMapper 
{
    /**
     * 查询用户关注关系
     * 
     * @param id 用户关注关系主键
     * @return 用户关注关系
     */
    public QlUserFollow selectQlUserFollowById(Long id);

    /**
     * 查询用户关注关系列表
     * 
     * @param qlUserFollow 用户关注关系
     * @return 用户关注关系集合
     */
    public List<QlUserFollow> selectQlUserFollowList(QlUserFollow qlUserFollow);

    /**
     * 新增用户关注关系
     * 
     * @param qlUserFollow 用户关注关系
     * @return 结果
     */
    public int insertQlUserFollow(QlUserFollow qlUserFollow);

    /**
     * 修改用户关注关系
     * 
     * @param qlUserFollow 用户关注关系
     * @return 结果
     */
    public int updateQlUserFollow(QlUserFollow qlUserFollow);

    /**
     * 删除用户关注关系
     * 
     * @param id 用户关注关系主键
     * @return 结果
     */
    public int deleteQlUserFollowById(Long id);

    /**
     * 批量删除用户关注关系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQlUserFollowByIds(Long[] ids);
}
