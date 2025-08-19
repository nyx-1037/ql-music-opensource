package com.nyx.musicPlatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nyx.musicPlatform.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 * 
 * @author nyx
 * @since 2025-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM ql_user WHERE username = #{username} AND is_deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM ql_user WHERE email = #{email} AND is_deleted = 0")
    User findByEmail(@Param("email") String email);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    @Select("SELECT COUNT(*) > 0 FROM ql_user WHERE username = #{username} AND is_deleted = 0")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 存在返回true，不存在返回false
     */
    @Select("SELECT COUNT(*) > 0 FROM ql_user WHERE email = #{email} AND is_deleted = 0")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 根据用户ID查询用户状态
     * @param userId 用户ID
     * @return 用户状态
     */
    @Select("SELECT status FROM `ql_user` WHERE id = #{userId} AND is_deleted = 0")
    Integer getUserStatus(@Param("userId") Long userId);
}