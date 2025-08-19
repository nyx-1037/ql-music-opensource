# 阿里云OSS配置说明

## 概述

后台管理系统已完成阿里云OSS集成，音乐文件和封面图片将直接上传到阿里云OSS存储，确保与客户端存储方式保持一致。

## 配置步骤

### 1. 阿里云OSS准备

1. 登录阿里云控制台
2. 开通对象存储OSS服务
3. 创建存储桶（Bucket）
4. 获取访问密钥（AccessKey ID 和 AccessKey Secret）
5. 记录OSS访问端点（Endpoint）

### 2. 后台配置文件修改

在 `musicmanage-admin/src/main/resources/application.yml` 中配置OSS信息：

```yaml
# 阿里云OSS配置
oss:
  enabled: true
  endpoint: oss-cn-hangzhou.aliyuncs.com  # 替换为你的OSS端点
  access-key-id: your-access-key-id        # 替换为你的AccessKey ID
  access-key-secret: your-access-key-secret # 替换为你的AccessKey Secret
  bucket-name: your-bucket-name            # 替换为你的存储桶名称
  music-prefix: music/                     # 音乐文件存储前缀
  cover-prefix: covers/                    # 封面图片存储前缀
```

### 3. 安全配置建议

**重要：不要将真实的AccessKey信息直接写在配置文件中！**

推荐使用环境变量方式：

```yaml
oss:
  enabled: true
  endpoint: ${OSS_ENDPOINT:oss-cn-hangzhou.aliyuncs.com}
  access-key-id: ${OSS_ACCESS_KEY_ID}
  access-key-secret: ${OSS_ACCESS_KEY_SECRET}
  bucket-name: ${OSS_BUCKET_NAME}
  music-prefix: music/
  cover-prefix: covers/
```

然后在系统环境变量中设置：
- `OSS_ACCESS_KEY_ID=你的AccessKey ID`
- `OSS_ACCESS_KEY_SECRET=你的AccessKey Secret`
- `OSS_BUCKET_NAME=你的存储桶名称`
- `OSS_ENDPOINT=你的OSS端点`

## 功能特性

### 1. 文件上传
- 音乐文件自动上传到OSS的 `music/` 目录
- 封面图片自动上传到OSS的 `covers/` 目录
- 文件名使用UUID重命名，避免冲突
- 支持的音乐格式：mp3, wav, flac, m4a, aac, ogg
- 支持的封面格式：jpg, jpeg, png, gif, webp

### 2. 文件管理
- 删除音乐时自动删除OSS中的对应文件
- 批量删除支持
- 完整的错误处理和日志记录

### 3. 配置验证
- OSS服务可通过配置开关控制
- 文件上传前进行格式和大小验证
- 上传失败时提供详细错误信息

## 与客户端的一致性

- 存储方式：统一使用阿里云OSS
- 文件路径：保持相同的前缀结构
- 访问方式：通过OSS的公网URL访问
- 安全性：统一的访问控制策略

## 故障排除

### 1. OSS连接失败
- 检查网络连接
- 验证AccessKey信息是否正确
- 确认OSS端点地址是否正确
- 检查存储桶是否存在且有访问权限

### 2. 文件上传失败
- 检查文件格式是否支持
- 验证文件大小是否超过限制
- 确认OSS存储空间是否充足
- 查看应用日志获取详细错误信息

### 3. 文件访问失败
- 检查存储桶的访问权限设置
- 验证文件URL是否正确
- 确认文件是否已成功上传到OSS

## 监控和维护

### 1. 日志监控
- 关注文件上传成功/失败的日志
- 监控OSS连接异常
- 定期检查存储空间使用情况

### 2. 性能优化
- 根据用户地理位置选择合适的OSS端点
- 考虑使用CDN加速文件访问
- 定期清理无用的文件

### 3. 备份策略
- 配置OSS的跨区域复制
- 定期备份重要的音乐文件
- 建立文件恢复机制

## 开发说明

### 1. 核心类说明
- `OssConfig`: OSS配置类，读取配置文件中的OSS参数
- `OssService`: OSS服务类，提供文件上传、删除等功能
- `QlMusicServiceImpl`: 音乐服务实现类，集成OSS文件操作

### 2. 扩展开发
- 可以扩展支持更多文件类型
- 可以添加文件压缩功能
- 可以实现文件的批量操作

---

**配置完成后，重启应用即可生效。建议先在测试环境验证配置正确性，再部署到生产环境。**