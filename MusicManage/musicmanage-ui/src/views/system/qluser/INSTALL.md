# QlUser 模块安装配置指南

## 1. 数据库配置

确保数据库中已创建 `ql_user` 表，表结构如下：

```sql
CREATE TABLE `ql_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（加密）',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `status` bigint(1) DEFAULT '1' COMMENT '账号状态（0-禁用，1-启用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bigint(1) DEFAULT '0' COMMENT '逻辑删除标志（0-未删除，1-已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户端用户表';
```

## 2. 后端配置

### 2.1 确认后端文件存在

确保以下后端文件已正确配置：

- `QlUserController.java` - 控制器
- `IQlUserService.java` - 服务接口
- `QlUserServiceImpl.java` - 服务实现
- `QlUser.java` - 实体类
- `QlUserMapper.java` - 数据访问接口
- `QlUserMapper.xml` - MyBatis映射文件

### 2.2 权限配置

在若依系统的菜单管理中添加以下权限：

```
system:qluser:list    - 查询客户端用户列表
system:qluser:query   - 查询客户端用户详情
system:qluser:add     - 新增客户端用户
system:qluser:edit    - 修改客户端用户
system:qluser:remove  - 删除客户端用户
system:qluser:export  - 导出客户端用户
```

## 3. 前端配置

### 3.1 文件结构

确保以下前端文件已正确放置：

```
src/
├── api/system/qluser.js              # API接口定义
└── views/system/qluser/
    ├── index.vue                      # 主页面组件
    ├── README.md                      # 功能说明
    └── INSTALL.md                     # 安装指南
```

### 3.2 菜单配置

在若依系统后台的菜单管理中添加新菜单：

**菜单信息：**
- 菜单名称：`客户端用户管理`
- 父菜单：`系统管理`
- 显示顺序：`5`
- 路由地址：`qluser`
- 组件路径：`system/qluser/index`
- 菜单类型：`菜单`
- 是否外链：`否`
- 是否缓存：`是`
- 是否显示：`是`
- 权限标识：`system:qluser:list`
- 菜单图标：`user`

**按钮权限配置：**

为上述菜单添加以下按钮权限：

1. **新增按钮**
   - 按钮名称：`新增`
   - 权限标识：`system:qluser:add`

2. **修改按钮**
   - 按钮名称：`修改`
   - 权限标识：`system:qluser:edit`

3. **删除按钮**
   - 按钮名称：`删除`
   - 权限标识：`system:qluser:remove`

4. **导出按钮**
   - 按钮名称：`导出`
   - 权限标识：`system:qluser:export`

5. **查询按钮**
   - 按钮名称：`查询`
   - 权限标识：`system:qluser:query`

## 4. 角色权限分配

### 4.1 为管理员角色分配权限

1. 进入 `系统管理` -> `角色管理`
2. 选择需要分配权限的角色（如：管理员）
3. 点击 `分配权限`
4. 在权限树中勾选 `客户端用户管理` 及其所有子权限
5. 保存配置

### 4.2 权限验证

分配权限后，对应角色的用户应该能够：
- 在左侧菜单中看到 `客户端用户管理` 菜单项
- 访问用户管理页面
- 根据分配的按钮权限执行相应操作

## 5. 测试验证

### 5.1 功能测试

1. **菜单访问测试**
   - 登录系统
   - 点击 `系统管理` -> `客户端用户管理`
   - 验证页面是否正常加载

2. **CRUD操作测试**
   - 新增用户：填写用户信息并保存
   - 查询用户：使用搜索条件筛选用户
   - 修改用户：编辑用户信息并保存
   - 删除用户：删除测试用户
   - 导出用户：导出用户列表到Excel

3. **权限测试**
   - 使用不同权限的用户登录
   - 验证按钮显示/隐藏是否正确
   - 验证操作权限是否生效

### 5.2 常见问题排查

**问题1：菜单不显示**
- 检查菜单配置是否正确
- 检查用户角色是否分配了相应权限
- 检查权限标识是否匹配

**问题2：页面访问404**
- 检查组件路径是否正确
- 检查前端文件是否存在
- 检查路由配置是否正确

**问题3：API调用失败**
- 检查后端控制器是否正常启动
- 检查数据库连接是否正常
- 检查权限注解是否正确

**问题4：按钮权限不生效**
- 检查按钮权限配置是否正确
- 检查前端权限指令是否正确
- 检查用户是否拥有相应权限

## 6. 维护说明

### 6.1 数据备份

定期备份 `ql_user` 表数据：

```sql
-- 备份用户数据
CREATE TABLE ql_user_backup AS SELECT * FROM ql_user;

-- 或导出到文件
mysqldump -u username -p database_name ql_user > ql_user_backup.sql
```

### 6.2 性能优化

1. **数据库索引优化**
   - 为常用查询字段添加索引
   - 定期分析查询性能

2. **分页查询优化**
   - 合理设置分页大小
   - 避免深度分页

3. **缓存策略**
   - 考虑为用户信息添加缓存
   - 设置合理的缓存过期时间

### 6.3 安全建议

1. **密码安全**
   - 强制使用强密码策略
   - 定期提醒用户修改密码

2. **数据安全**
   - 敏感数据加密存储
   - 定期安全审计

3. **访问控制**
   - 严格控制管理权限
   - 记录操作日志

## 7. 联系支持

如果在安装或使用过程中遇到问题，请：

1. 查看本文档的常见问题排查部分
2. 检查系统日志获取详细错误信息
3. 联系技术支持团队

---

**注意：** 本模块与若依系统原有的用户管理模块完全独立，不会影响系统原有功能。