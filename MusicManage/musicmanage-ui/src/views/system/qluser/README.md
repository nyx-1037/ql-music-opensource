# QlUser 客户端用户管理模块

## 概述

本模块是为了解决若依系统中客户端用户表与后台系统用户表冲突而创建的独立用户管理模块。

## 文件结构

```
qluser/
├── index.vue          # 用户管理主页面
└── README.md          # 说明文档
```

## 功能特性

- ✅ 用户列表查询（支持用户名、邮箱、昵称、账号状态筛选）
- ✅ 用户信息新增
- ✅ 用户信息修改
- ✅ 用户删除（支持批量删除）
- ✅ 用户数据导出
- ✅ 头像预览功能
- ✅ 账号状态管理（正常/停用）
- ✅ 表单验证（用户名、密码长度限制，邮箱格式验证等）

## 字段说明

| 字段名 | 类型 | 说明 | 验证规则 |
|--------|------|------|----------|
| id | Long | 用户ID | 自动生成 |
| username | String | 用户名 | 必填，2-20字符 |
| password | String | 密码 | 必填，6-20字符 |
| email | String | 邮箱 | 必填，邮箱格式 |
| nickname | String | 昵称 | 必填，最大50字符 |
| avatarUrl | String | 头像URL | 可选 |
| status | Long | 账号状态 | 0-停用，1-正常 |
| isDeleted | Long | 逻辑删除标志 | 0-未删除，1-已删除 |
| createTime | Date | 创建时间 | 自动生成 |
| updateTime | Date | 更新时间 | 自动更新 |

## 权限配置

需要在系统菜单中配置以下权限：

- `system:qluser:list` - 查询用户列表
- `system:qluser:query` - 查询用户详情
- `system:qluser:add` - 新增用户
- `system:qluser:edit` - 修改用户
- `system:qluser:remove` - 删除用户
- `system:qluser:export` - 导出用户

## 菜单配置示例

在若依系统菜单管理中添加以下菜单项：

```
菜单名称：客户端用户管理
父菜单：系统管理
显示顺序：5
路由地址：qluser
组件路径：system/qluser/index
菜单类型：菜单
是否外链：否
是否缓存：是
是否显示：是
权限标识：system:qluser:list
菜单图标：user
```

## API 接口

所有API接口都在 `@/api/system/qluser.js` 中定义：

- `listQlUser(query)` - 获取用户列表
- `getQlUser(id)` - 获取用户详情
- `addQlUser(data)` - 新增用户
- `updateQlUser(data)` - 更新用户
- `delQlUser(id)` - 删除用户
- `exportQlUser(query)` - 导出用户

## 后端支持

后端相关文件：
- Controller: `QlUserController.java`
- Service: `IQlUserService.java` / `QlUserServiceImpl.java`
- Entity: `QlUser.java`
- Mapper: `QlUserMapper.java` / `QlUserMapper.xml`

## 使用说明

1. 确保后端已正确配置相关权限
2. 在系统菜单管理中添加对应菜单项
3. 为相关角色分配权限
4. 访问菜单即可使用用户管理功能

## 注意事项

- 本模块与若依系统自带的用户管理模块完全独立
- 密码字段在新增时为必填，修改时可选
- 账号状态：1表示正常，0表示停用
- 支持头像URL预览，建议使用有效的图片链接
- 删除操作为逻辑删除，不会物理删除数据