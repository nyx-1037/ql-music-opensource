<template>
  <div id="app">
    <!-- 导航栏 -->
    <Navbar v-if="!$route.meta.hideNavbar" />
    
    <!-- 主要内容区域 -->
    <main class="main-content" :class="{ 'with-navbar': !$route.meta.hideNavbar }">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
      <!-- Copyright -->
      <div class="copyright" v-if="!$route.meta.hideNavbar">
        <p>&copy; 2025 Mr.Nie. <span data-i18n-key="footer.rights">保留所有权利</span></p>
        <p><a href="http://beian.miit.gov.cn/" target="_blank" rel="noopener" style="color:grey;text-decoration: none;">赣ICP备2025065901号</a></p>
      </div>
    </main>
    
    <!-- 音乐播放器 -->
    <MusicPlayer v-if="!$route.meta.hideNavbar" />
    
    <!-- 全局音频元素 -->
    <audio ref="audioRef" preload="metadata" />
    

  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/player'
import { useUserStore } from '@/stores/user'
import Navbar from '@/components/layout/Navbar.vue'
import MusicPlayer from '@/components/player/MusicPlayer.vue'

export default {
  name: 'App',
  components: {
    Navbar,
    MusicPlayer
  },
  setup() {
    const audioRef = ref(null)
    const playerStore = usePlayerStore()
    const userStore = useUserStore()
    
    onMounted(async () => {
      // 设置音频元素到播放器store
      if (audioRef.value) {
        playerStore.setAudioElement(audioRef.value)
      }
      
      // 初始化用户状态
      userStore.initUserState()
      
      // 如果有token，验证其有效性
      if (userStore.token) {
        try {
          await userStore.initAuth()
        } catch (error) {
          console.error('用户认证初始化失败:', error)
        }
      }
    })
    
    return {
      audioRef
    }
  }
}
</script>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  /* position: relative; */ /* 备案信息不再固定定位，因此不需要相对定位 */
}

.main-content {
  flex: 1;
  min-height: calc(100vh - 60px); /* 减去播放器高度 */
}

.main-content.with-navbar {
  padding-top: 60px; /* 导航栏高度 */
  min-height: calc(100vh - 120px); /* 减去导航栏和播放器高度 */
}

/* 隐藏音频元素 */
audio {
  display: none;
}

/* 版权信息样式 */
      .copyright {
        text-align: center;
        padding: 80px;
        background-color: #f0f0f0;
        color: #666;
        font-size: 14px;
        border-top: 1px solid #eee;

      }

.copyright p {
  margin: 2px 0;
}

.copyright a {
  color: #909399;
  text-decoration: none;
}

.copyright a:hover {
  color: #409eff;
}
</style>
