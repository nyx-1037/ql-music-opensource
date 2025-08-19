<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <!-- 404图标 -->
      <div class="error-icon">
        <el-icon class="icon-404"><Warning /></el-icon>
      </div>
      
      <!-- 错误信息 -->
      <div class="error-info">
        <h1 class="error-code">404</h1>
        <h2 class="error-title">页面未找到</h2>
        <p class="error-description">
          抱歉，您访问的页面不存在或已被移除。
        </p>
      </div>
      
      <!-- 操作按钮 -->
      <div class="error-actions">
        <el-button type="primary" @click="goHome">
          <el-icon><House /></el-icon>
          返回首页
        </el-button>
        <el-button @click="goBack">
          <el-icon><Back /></el-icon>
          返回上页
        </el-button>
      </div>
      
      <!-- 推荐链接 -->
      <div class="recommend-links">
        <h3>您可能想要访问：</h3>
        <div class="link-list">
          <router-link to="/" class="recommend-link">
            <el-icon><House /></el-icon>
            首页
          </router-link>
          <router-link to="/search" class="recommend-link">
            <el-icon><Search /></el-icon>
            搜索音乐
          </router-link>
          <router-link to="/playlists" class="recommend-link">
            <el-icon><List /></el-icon>
            我的歌单
          </router-link>
          <router-link to="/favorites" class="recommend-link">
            <el-icon><Star /></el-icon>
            我的收藏
          </router-link>
        </div>
      </div>
    </div>
    
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="music-note note-1">♪</div>
      <div class="music-note note-2">♫</div>
      <div class="music-note note-3">♪</div>
      <div class="music-note note-4">♬</div>
      <div class="music-note note-5">♩</div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { Warning, House, Back, Search, List, Star } from '@element-plus/icons-vue'

export default {
  name: 'NotFoundPage',
  components: {
    Warning,
    House,
    Back,
    Search,
    List,
    Star
  },
  setup() {
    const router = useRouter()
    
    // 返回首页
    const goHome = () => {
      router.push('/')
    }
    
    // 返回上一页
    const goBack = () => {
      if (window.history.length > 1) {
        router.go(-1)
      } else {
        router.push('/')
      }
    }
    
    return {
      goHome,
      goBack
    }
  }
}
</script>

<style scoped>
.not-found-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.not-found-content {
  text-align: center;
  background: white;
  padding: 60px 40px;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  width: 90%;
  position: relative;
  z-index: 2;
}

.error-icon {
  margin-bottom: 30px;
}

.icon-404 {
  font-size: 80px;
  color: #f56c6c;
}

.error-info {
  margin-bottom: 40px;
}

.error-code {
  font-size: 72px;
  font-weight: 700;
  color: #409eff;
  margin: 0 0 10px 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.error-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 15px 0;
}

.error-description {
  font-size: 16px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

.error-actions {
  margin-bottom: 40px;
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.error-actions .el-button {
  padding: 12px 24px;
  font-size: 14px;
}

.recommend-links {
  border-top: 1px solid #ebeef5;
  padding-top: 30px;
}

.recommend-links h3 {
  font-size: 18px;
  color: #303133;
  margin: 0 0 20px 0;
}

.link-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 15px;
}

.recommend-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 10px;
  border-radius: 8px;
  text-decoration: none;
  color: #606266;
  transition: all 0.3s;
  border: 1px solid #ebeef5;
}

.recommend-link:hover {
  color: #409eff;
  border-color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.recommend-link .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 1;
}

.music-note {
  position: absolute;
  font-size: 40px;
  color: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.note-1 {
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.note-2 {
  top: 20%;
  right: 15%;
  animation-delay: 1s;
}

.note-3 {
  bottom: 30%;
  left: 20%;
  animation-delay: 2s;
}

.note-4 {
  bottom: 20%;
  right: 10%;
  animation-delay: 3s;
}

.note-5 {
  top: 50%;
  left: 5%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-content {
    padding: 40px 20px;
  }
  
  .error-code {
    font-size: 60px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .error-actions .el-button {
    width: 200px;
  }
  
  .link-list {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .music-note {
    font-size: 30px;
  }
}

@media (max-width: 480px) {
  .error-code {
    font-size: 48px;
  }
  
  .error-title {
    font-size: 20px;
  }
  
  .link-list {
    grid-template-columns: 1fr;
  }
}
</style>