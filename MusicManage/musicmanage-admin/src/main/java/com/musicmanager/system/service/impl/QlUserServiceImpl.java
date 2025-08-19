package com.musicmanager.system.service.impl;

import java.util.List;
import com.musicmanager.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmanager.system.mapper.QlUserMapper;
import com.musicmanager.system.domain.QlUser;
import com.musicmanager.system.service.IQlUserService;

/**
 * 用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
@Service
public class QlUserServiceImpl implements IQlUserService 
{
    @Autowired
    private QlUserMapper qlUserMapper;

    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    @Override
    public QlUser selectQlUserById(Long id)
    {
        return qlUserMapper.selectQlUserById(id);
    }

    /**
     * 查询用户列表
     * 
     * @param qlUser 用户
     * @return 用户
     */
    @Override
    public List<QlUser> selectQlUserList(QlUser qlUser)
    {
        return qlUserMapper.selectQlUserList(qlUser);
    }

    /**
     * 新增用户
     * 
     * @param qlUser 用户
     * @return 结果
     */
    @Override
    public int insertQlUser(QlUser qlUser)
    {
        qlUser.setCreateTime(DateUtils.getNowDate());
        return qlUserMapper.insertQlUser(qlUser);
    }

    /**
     * 修改用户
     * 
     * @param qlUser 用户
     * @return 结果
     */
    @Override
    public int updateQlUser(QlUser qlUser)
    {
        qlUser.setUpdateTime(DateUtils.getNowDate());
        return qlUserMapper.updateQlUser(qlUser);
    }

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteQlUserByIds(Long[] ids)
    {
        return qlUserMapper.deleteQlUserByIds(ids);
    }

    /**
     * 删除用户信息
     * 
     * @param id 用户主键
     * @return 结果
     */
    @Override
    public int deleteQlUserById(Long id)
    {
        return qlUserMapper.deleteQlUserById(id);
    }

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户
     */
    @Override
    public QlUser selectQlUserByUsername(String username)
    {
        return qlUserMapper.selectQlUserByUsername(username);
    }
}
