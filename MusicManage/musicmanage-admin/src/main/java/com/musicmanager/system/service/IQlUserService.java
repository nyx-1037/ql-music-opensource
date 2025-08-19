package com.musicmanager.system.service;

import java.util.List;
import com.musicmanager.system.domain.QlUser;

/**
 * 用户Service接口
 * 
 * @author ruoyi
 * @date 2025-06-26
 */
public interface IQlUserService 
{
    /**
     * 查询用户
     * 
     * @param id 用户主键
     * @return 用户
     */
    public QlUser selectQlUserById(Long id);

    /**
     * 查询用户列表
     * 
     * @param qlUser 用户
     * @return 用户集合
     */
    public List<QlUser> selectQlUserList(QlUser qlUser);

    /**
     * 新增用户
     * 
     * @param qlUser 用户
     * @return 结果
     */
    public int insertQlUser(QlUser qlUser);

    /**
     * 修改用户
     * 
     * @param qlUser 用户
     * @return 结果
     */
    public int updateQlUser(QlUser qlUser);

    /**
     * 批量删除用户
     * 
     * @param ids 需要删除的用户主键集合
     * @return 结果
     */
    public int deleteQlUserByIds(Long[] ids);

    /**
     * 删除用户信息
     * 
     * @param id 用户主键
     * @return 结果
     */
    public int deleteQlUserById(Long id);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户
     */
    public QlUser selectQlUserByUsername(String username);
}
