<template>
  <nav class="navbar">
    <div class="navbar-container">
      <!-- Logo和标题 -->
      <div class="navbar-brand">
        <router-link to="/" class="brand-link">
          <img src="/7L-music-icon.png" alt="七洛音乐" class="brand-icon-img" />
          <span class="brand-text">七洛音乐</span>
          <span class="beta-tag">Beta</span>
        </router-link>
      </div>
      
      <!-- 导航菜单 -->
      <div class="navbar-menu">
        <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          :ellipsis="false"
          @select="handleSelect"
          class="navbar-nav"
        >
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          
          <el-menu-item index="/music">
            <el-icon><Mic /></el-icon>
            <span>音乐库</span>
          </el-menu-item>
          
          <el-menu-item 
            v-if="userStore.isLoggedIn"
            index="/playlist"
          >
            <el-icon><Collection /></el-icon>
            <span>我的歌单</span>
          </el-menu-item>
        </el-menu>
      </div>
      
      <!-- 搜索框 -->
      <div class="navbar-search">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索音乐、艺术家、专辑..."
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
        >
          <template #suffix>
            <el-icon class="search-icon" @click="handleSearch">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </div>
      
      <!-- 用户操作区域 -->
      <div class="navbar-actions">

        

        
        <!-- 用户菜单 -->
        <div v-if="userStore.isLoggedIn" class="user-menu">
          <el-dropdown @command="handleUserCommand">
            <div class="user-info">
              <el-avatar
                :src="userStore.userAvatar"
                :size="32"
                class="user-avatar"
              >
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ userStore.userName }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="playlist">
                  <el-icon><Collection /></el-icon>
                  我的歌单
                </el-dropdown-item>
                <el-dropdown-item command="history">
                  <el-icon><Clock /></el-icon>
                  播放历史
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>
                  设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        <!-- 登录/注册按钮 -->
        <div v-else class="auth-buttons">
          <el-dropdown trigger="click" @command="handleAuthCommand">
            <el-button type="primary" size="small" class="auth-btn">
              登录/注册
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="login">
                  <el-icon><User /></el-icon>
                  登录
                </el-dropdown-item>
                <el-dropdown-item command="register">
                  <el-icon><UserFilled /></el-icon>
                  注册
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import {
  House,
  Mic,
  Search,
  Collection,
  User,
  UserFilled,
  ArrowDown,
  Clock,
  Setting,
  SwitchButton
} from '@element-plus/icons-vue'

export default {
  name: 'NavbarComponent',
  components: {
    House,
    Mic,
    Search,
    Collection,
    User,
    UserFilled,
    ArrowDown,
    Clock,
    Setting,
    SwitchButton
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userStore = useUserStore()
    
    const searchKeyword = ref('')

    
    // 当前激活的菜单项
    const activeIndex = computed(() => {
      return route.path
    })
    
    // 处理菜单选择
    const handleSelect = (index) => {
      router.push(index)
    }
    
    // 处理搜索
    const handleSearch = () => {
      if (searchKeyword.value.trim()) {
        router.push({
          path: '/music',
          query: { search: searchKeyword.value.trim() }
        })
      }
    }
    

    
    // 处理用户菜单命令
    const handleUserCommand = async (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'playlist':
          router.push('/playlist')
          break
        case 'history':
          router.push('/history')
          break
        case 'settings':
          router.push('/settings')
          break
        case 'logout':
          try {
            await ElMessageBox.confirm(
              '确定要退出登录吗？',
              '提示',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            
            await userStore.logout()
            router.push('/')
          } catch (error) {
            // 用户取消或其他错误
            console.log('取消退出登录')
          }
          break
      }
    }
    
    // 监听登录状态变化
    watch(() => userStore.isLoggedIn, (newVal) => {
      if (newVal) {
        // 登录后的初始化操作
      }
    })
    
    // 处理认证菜单命令
    const handleAuthCommand = (command) => {
      switch (command) {
        case 'login':
          router.push('/login')
          break
        case 'register':
          router.push('/register')
          break
      }
    }

    return {
      userStore,
      searchKeyword,
      activeIndex,
      handleSelect,
      handleSearch,
      handleUserCommand,
      handleAuthCommand
    }
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #2c3e50;
  border-bottom: 1px solid #34495e;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  z-index: 1000;
}

.navbar-container {
  display: flex;
  align-items: center;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Logo和品牌 */
.navbar-brand {
  flex-shrink: 0;
  margin-right: 30px;
}

.brand-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #409eff;
  font-size: 18px;
  font-weight: bold;
}

.brand-icon {
  font-size: 24px;
  margin-right: 8px;
}

.brand-icon-img {
  width: 32px;
  height: 32px;
  margin-right: 8px;
  object-fit: contain;
}

.brand-text {
  color: #ffffff;
}

.beta-tag {
  background-color: #f39c12;
  color: #ffffff;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 8px;
  margin-left: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* 导航菜单 */
.navbar-menu {
  flex: 1;
  max-width: 400px;
}

.navbar-nav {
  border-bottom: none;
  background-color: transparent;
}

.navbar-nav .el-menu-item {
  height: 60px;
  line-height: 60px;
  border-bottom: none;
  color: #ffffff;
  background-color: transparent;
}

.navbar-nav .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: #ffffff;
}

.navbar-nav .el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.2);
  color: #ffffff;
  border-bottom: 2px solid #409eff;
}

.navbar-nav .el-menu-item span {
  margin-left: 5px;
  color: inherit;
}

.navbar-nav .el-menu-item .el-icon {
  color: inherit;
}

/* 搜索框 */
.navbar-search {
  flex: 1;
  max-width: 300px;
  margin: 0 20px;
}

.search-input {
  width: 100%;
}

.search-icon {
  cursor: pointer;
  color: #909399;
}

.search-icon:hover {
  color: #409eff;
}

/* 用户操作区域 */
.navbar-actions {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-shrink: 0;
}

.upload-btn {
  display: flex;
  align-items: center;
  gap: 5px;
}

.notification-badge {
  display: flex;
  align-items: center;
}

.notification-btn {
  padding: 8px;
  font-size: 18px;
}

/* 用户菜单 */
.user-menu {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 10px;
  border-radius: 20px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  flex-shrink: 0;
}

.username {
  font-size: 14px;
  color: #ffffff;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  font-size: 12px;
  color: #909399;
}

/* 登录注册按钮 */
.auth-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.login-btn {
  color: #606266;
}

.register-btn {
  padding: 8px 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar-container {
    padding: 0 15px;
  }
  
  .navbar-menu {
    display: none;
  }
  
  .navbar-search {
    max-width: 200px;
    margin: 0 10px;
  }
  
  .username {
    display: none;
  }
  
  .upload-btn span {
    display: none;
  }
}

@media (max-width: 576px) {
  .brand-text {
    display: none;
  }
  
  .navbar-search {
    max-width: 150px;
  }
  
  .auth-buttons .login-btn {
    display: none;
  }
}
</style>