# 七洛音乐平台 (Music Platform)

基于SpringBoot + Vue3的在线音乐播放器平台，支持用户注册登录、音乐上传、播放、歌单管理、社交互动等完整功能。

## 📋 项目概述

### 项目简介
七洛音乐是一个现代化的在线音乐平台，提供音乐上传、播放、管理、社交分享等完整功能。采用前后端分离架构，支持高并发访问和大文件处理，集成了阿里云OSS存储和CDN加速。

### 核心特性
- 🎵 **音乐播放**: 支持多种音频格式，流式播放，断点续传，播放历史记录
- 📤 **文件上传**: 支持大文件分片上传，带进度显示，拖拽上传
- 🖼️ **封面管理**: 音乐封面图片上传和显示，自动生成缩略图
- 🔍 **搜索功能**: 支持按标题、艺术家、专辑、标签等多字段搜索
- 👤 **用户系统**: 注册登录、JWT认证、权限控制、用户关注
- 📱 **歌单管理**: 创建歌单、收藏音乐、公开/私有歌单
- 💬 **社交互动**: 音乐评论、点赞、收藏、分享
- ☁️ **云存储**: 阿里云OSS存储，CDN加速访问，全球分发
- 📱 **响应式设计**: 支持多端访问，自适应布局，移动端适配
- 📊 **数据统计**: 播放统计、收藏统计、用户行为分析

## 🛠️ 技术栈

### 后端技术
- **框架**: SpringBoot 2.7.x + MyBatis-Plus 3.5.x
- **数据库**: MySQL 8.0 + Redis 6.0 (缓存)
- **认证**: JWT + Spring Security + 自定义拦截器
- **文件存储**: 阿里云OSS对象存储 + CDN加速
- **文档**: Swagger 3.x + SpringDoc OpenAPI
- **日志**: Logback + 文件日志切割
- **构建**: Maven 3.6+
- **JDK**: JDK 8

### 前端技术
- **框架**: Vue 3.x + Composition API
- **UI库**: Element-Plus 2.x
- **HTTP客户端**: Axios + 请求拦截器
- **路由**: Vue Router 4.x + 路由守卫
- **状态管理**: Pinia (替代Vuex)
- **构建工具**: Vite (开发) + Webpack (构建)
- **包管理**: npm
- **图标**: Element-Plus图标库

### 存储与部署
- **文件存储**: 阿里云OSS对象存储 (50TB容量)
- **CDN加速**: 阿里云CDN全球节点
- **缓存策略**: Redis集群 + 本地缓存
- **容器化**: Docker + Docker Compose
- **反向代理**: Nginx + 负载均衡

## 🏗️ 系统架构

```
前端(Vue3) <---> 后端API(SpringBoot) <---> 数据库(MySQL)
                        |
                    缓存层(Redis)
                        |
                阿里云OSS文件存储 + CDN加速
                        |
                日志收集(Logback) + 监控
```

### 模块划分
- **用户模块**: 注册、登录、JWT认证、用户资料、关注系统
- **音乐模块**: 音乐上传、查询、删除、播放、下载
- **歌单模块**: 歌单创建、编辑、收藏、公开/私有管理
- **社交模块**: 评论、点赞、收藏、分享、用户互动
- **文件模块**: 文件上传、存储、下载、格式转换
- **存储模块**: 阿里云OSS存储管理、CDN加速访问
- **统计模块**: 播放统计、用户行为分析、热门推荐

## 🚀 快速开始

### 环境要求
- JDK 8+
- Node.js 14+
- MySQL 8.0
- Redis 6.0
- Maven 3.6+

### 环境变量配置
在启动项目前，请配置以下环境变量：

#### 后端环境变量
```bash
# 数据库配置
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=music_db
export DB_USERNAME=root
export DB_PASSWORD=your_database_password

# Redis配置
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=your_redis_password
export REDIS_DB=2

# JWT配置
export JWT_SECRET=your_jwt_secret_key_here_should_be_at_least_64_characters_long

# OSS配置
export OSS_ENDPOINT=https://oss-cn-hangzhou.aliyuncs.com
export OSS_ACCESS_KEY_ID=your_oss_access_key_id
export OSS_ACCESS_KEY_SECRET=your_oss_access_key_secret
export OSS_BUCKET_NAME=your-bucket-name

# 可选配置
export OSS_CUSTOM_DOMAIN=https://cdn.yourdomain.com
export SWAGGER_ENABLED=true
```

#### 管理后台环境变量
```bash
# 数据库配置
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=music_db
export DB_USERNAME=root
export DB_PASSWORD=your_database_password

# Redis配置
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=your_redis_password
export REDIS_DB=0

# JWT配置
export JWT_SECRET=your_jwt_secret_key_here_should_be_at_least_64_characters_long
export JWT_EXPIRATION=60

# OSS配置
export OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
export OSS_ACCESS_KEY_ID=your_oss_access_key_id
export OSS_ACCESS_KEY_SECRET=your_oss_access_key_secret
export OSS_BUCKET_NAME=your-bucket-name

# 文件上传路径
export UPLOAD_PATH=/home/ruoyi/uploadPath
export MUSIC_UPLOAD_PATH=/uploads/music/
export COVER_UPLOAD_PATH=/uploads/covers/
```

### 后端启动

1. **克隆项目**
```bash
git clone https://github.com/nyx-1037/ql-music-opensource.git
cd ql-music-opensource
```

2. **创建数据库**
```sql
-- 创建数据库
CREATE DATABASE music_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入数据库表结构和初始化数据
mysql -u root -p music_db < init.sql
```

3. **默认管理员账号**
导入数据库后，系统会自动创建默认管理员账号：
- **用户名**: admin
- **密码**: 123456

该账号同时适用于：
- **客户端** (http://localhost:8080)
- **管理后台** (http://localhost:8090)

4. **配置环境变量**
```bash
# 创建环境配置文件
cp .env.example .env
# 编辑.env文件，填入你的配置
```

4. **启动后端服务**
```bash
# 方法1：使用环境变量启动
cd music-system
export $(cat ../.env | xargs)
mvn clean install
mvn spring-boot:run

# 方法2：使用配置文件启动
cd music-system
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=../config/application-local.yml"
```

后端服务将在 `http://localhost:8081` 启动

### 前端启动

1. **安装依赖**
```bash
cd music-ui
npm install
```

2. **配置环境变量**
```bash
# 创建环境配置文件
cp .env.example .env.development
# 编辑.env.development文件
```

3. **启动开发服务器**
```bash
# 默认端口 8080
npm run serve

# 或使用多端口启动脚本
./start-multi-port.bat
```

前端服务将在 `http://localhost:8080` 启动

### 管理后台启动

1. **配置环境变量**
```bash
# 确保已设置管理后台的环境变量
export $(cat ../.env | xargs)
```

2. **启动管理后台**
```bash
cd MusicManage
mvn clean install
mvn spring-boot:run -pl musicmanage-admin
```

管理后台将在 `http://localhost:8090` 启动

### 配置文件示例

#### .env 文件示例
```bash
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=music_db
DB_USERNAME=root
DB_PASSWORD=your_secure_password

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_DB=2

# JWT密钥（建议使用强密码）
JWT_SECRET=your_super_secure_jwt_secret_key_64_characters_minimum

# OSS配置（阿里云对象存储）
OSS_ENDPOINT=https://oss-cn-hangzhou.aliyuncs.com
OSS_ACCESS_KEY_ID=your_access_key_id
OSS_ACCESS_KEY_SECRET=your_access_key_secret
OSS_BUCKET_NAME=your-bucket-name
OSS_CUSTOM_DOMAIN=https://cdn.yourdomain.com

# 开发配置
SWAGGER_ENABLED=true
```

#### application-local.yml 文件示例
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/music_db
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password:

oss:
  enabled: true
  endpoint: https://oss-cn-hangzhou.aliyuncs.com
  access-key-id: your_access_key_id
  access-key-secret: your_access_key_secret
  bucket-name: your-bucket-name
```

### 多端口启动选项

项目支持多种启动方式：

```bash
# 默认端口 8080
npm run serve

# 端口 3000
npm run serve:3000

# 端口 9000
npm run serve:9000

# 自定义端口
cross-env VUE_APP_DEV_PORT=9999 npm run serve
```

### Docker部署（推荐）

1. **创建 docker-compose.yml**
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: ${DB_NAME}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:6.0-alpine
    ports:
      - "6379:6379"
    command: redis-server --requirepass ${REDIS_PASSWORD}

  music-platform:
    build: ./music-system
    ports:
      - "8081:8081"
    depends_on:
      - mysql
      - redis
    environment:
      - DB_HOST=mysql
      - DB_PORT=3306
      - DB_NAME=${DB_NAME}
      - DB_USERNAME=root
      - DB_PASSWORD=${DB_PASSWORD}
      - REDIS_HOST=redis
      - REDIS_PORT=6379
      - REDIS_PASSWORD=${REDIS_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - OSS_ACCESS_KEY_ID=${OSS_ACCESS_KEY_ID}
      - OSS_ACCESS_KEY_SECRET=${OSS_ACCESS_KEY_SECRET}
      - OSS_BUCKET_NAME=${OSS_BUCKET_NAME}

volumes:
  mysql_data:
```

2. **启动服务**
```bash
docker-compose up -d
```

### 环境配置
```bash
# 生产环境配置
export OSS_ACCESS_KEY_ID=your_access_key
export OSS_ACCESS_KEY_SECRET=your_secret_key
export MYSQL_PASSWORD=your_mysql_password
export REDIS_PASSWORD=your_redis_password
export JWT_SECRET=your_jwt_secret
```
## 项目展示图片(数据仅供展示，实际数据请自行上传):

![图片1](E:\opensource\music-platform\assert\图片1.png)

![图片2](E:\opensource\music-platform\assert\图片2.png)

![图片3](E:\opensource\music-platform\assert\图片3.png)

![图片4](E:\opensource\music-platform\assert\图片4.png)

![图片5](E:\opensource\music-platform\assert\图片5.png)

![图片6](E:\opensource\music-platform\assert\图片6.png)

![图片7](E:\opensource\music-platform\assert\图片7.png)

![图片8](E:\opensource\music-platform\assert\图片8.png)











## 🔮 扩展功能规划

### 已完成功能 ✅
- [x] 用户注册登录系统
- [x] JWT认证和权限控制
- [x] 音乐上传和播放
- [x] 阿里云OSS文件存储
- [x] CDN加速访问
- [x] 音乐封面图片显示
- [x] 用户歌单管理
- [x] 音乐收藏功能
- [x] 播放历史记录
- [x] 音乐评论系统
- [x] 用户关注功能
- [x] 搜索功能
- [x] 响应式设计

### 短期扩展计划 🚧
- [ ] 音乐歌词显示和同步
- [ ] 音乐可视化效果
- [ ] 用户个人主页定制
- [ ] 音乐推荐算法
- [ ] 社交分享功能
- [ ] 移动端APP开发
- [ ] 音乐排行榜
- [ ] 用户等级系统

### 长期扩展计划 🎯
- [ ] AI音乐推荐引擎
- [ ] 音乐识别功能(听歌识曲)
- [ ] 实时聊天系统
- [ ] 音乐直播功能
- [ ] 付费会员系统
- [ ] 音乐人入驻平台
- [ ] 数字专辑发行
- [ ] 跨平台桌面应用

## 🤝 贡献指南

### 开发规范
- **代码规范**：遵循阿里巴巴Java开发手册
- **前端规范**：Vue3 Composition API规范
- **提交规范**：Git Conventional Commits
- **代码审查**：Pull Request必须经过review
- **测试要求**：新功能必须有对应测试

### 开发流程
1. Fork 项目到个人仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: add amazing feature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request
6. 等待代码审查和合并

### 代码风格
```java
// 后端代码示例
@RestController
@RequestMapping("/api/music")
@Validated
public class MusicController {
    
    @GetMapping("/list")
    public Result<PageResult<MusicVO>> getMusicList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword) {
        // 代码实现
    }
}
```

```javascript
// 前端代码示例
<script setup>
import { ref, computed } from 'vue'
import { useMusicStore } from '@/stores/music'

const musicStore = useMusicStore()
const searchKeyword = ref('')

const filteredMusic = computed(() => {
  return musicStore.musicList.filter(music =>
    music.title.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})
</script>
```

## 📄 许可证

本项目采用 **MIT License** 开源许可证，详见 [LICENSE](LICENSE) 文件。



