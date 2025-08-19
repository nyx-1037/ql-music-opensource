<template>
  <div class="profile-page" v-loading="loading">
    <!-- 用户信息头部 -->
    <div class="profile-header">
      <div class="profile-cover">
        <!-- 背景封面 -->
        <div class="cover-bg">
          <div class="cover-overlay"></div>
        </div>
        
        <!-- 用户信息内容 -->
        <div class="profile-content">
          <!-- 左侧头像区域 -->
          <div class="avatar-section">
            <div class="avatar-container">
              <el-avatar :size="100" :src="userInfo?.avatar" class="user-avatar">
                <el-icon size="40"><User /></el-icon>
              </el-avatar>
              
              <!-- 头像上传按钮 -->
              <el-button
                v-if="isCurrentUser"
                circle
                size="small"
                class="avatar-upload-btn"
                @click="uploadAvatarVisible = true"
              >
                <el-icon><Camera /></el-icon>
              </el-button>
            </div>
          </div>
          
          <!-- 右侧用户信息 -->
          <div class="user-info">
            <!-- 用户名和认证标识 -->
            <div class="user-title">
              <h1 class="username">{{ userInfo?.username || '加载中...' }}</h1>
              <el-tag v-if="userInfo?.isVerified" type="success" size="small" class="verified-tag">
                <el-icon><Check /></el-icon>
                已认证
              </el-tag>
            </div>
            
            <!-- 用户简介 -->
            <div class="user-bio-section">
              <p class="user-bio" v-if="userInfo?.bio">{{ userInfo.bio }}</p>
              <p class="user-bio placeholder" v-else-if="isCurrentUser">还没有个人简介，可以在设置页面添加~</p>
            </div>
            
            <!-- 用户统计数据 -->
            <div class="user-stats">
              <div class="stat-item" @click="activeTab = 'following'">
                <span class="stat-number">{{ formatNumber(userInfo?.followingCount || 0) }}</span>
                <span class="stat-label">关注</span>
              </div>
              <div class="stat-item" @click="activeTab = 'followers'">
                <span class="stat-number">{{ formatNumber(userInfo?.followerCount || 0) }}</span>
                <span class="stat-label">粉丝</span>
              </div>
              <div class="stat-item" @click="activeTab = 'playlists'">
                <span class="stat-number">{{ formatNumber(userInfo?.playlistCount || 0) }}</span>
                <span class="stat-label">歌单</span>
              </div>
              <div class="stat-item" @click="activeTab = 'favorites'">
                <span class="stat-number">{{ formatNumber(userInfo?.favoriteCount || 0) }}</span>
                <span class="stat-label">收藏</span>
              </div>
            </div>
            
            <!-- 操作按钮 -->
            <div class="user-actions">
              <!-- 非当前用户的操作 -->
              <template v-if="!isCurrentUser">
                <el-button
                  :type="isFollowing ? 'default' : 'primary'"
                  size="default"
                  @click="toggleFollow"
                  :loading="followLoading"
                  class="action-btn"
                >
                  <el-icon v-if="!isFollowing"><Plus /></el-icon>
                  <el-icon v-else><Check /></el-icon>
                  {{ isFollowing ? '取消关注' : '关注' }}
                </el-button>
                
                <el-button size="default" @click="sendMessage" class="action-btn">
                  <el-icon><Message /></el-icon>
                  私信
                </el-button>
              </template>
              
              <!-- 当前用户的操作 -->
              <template v-else>
                <el-button size="default" @click="$router.push('/settings')" class="action-btn">
                  <el-icon><Edit /></el-icon>
                  编辑资料
                </el-button>
                
                <el-button size="default" @click="$router.push('/settings')" class="action-btn">
                  <el-icon><Setting /></el-icon>
                  设置
                </el-button>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 内容标签页 -->
    <div class="profile-content-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="播放列表" name="playlists">
          <div class="playlists-section">
            <div class="section-header">
              <h3>播放列表 ({{ playlists.length }})</h3>
              <el-button
                v-if="isCurrentUser"
                type="primary"
                @click="createPlaylistVisible = true"
              >
                <el-icon><Plus /></el-icon>
                创建播放列表
              </el-button>
            </div>
            
            <div class="playlists-grid" v-if="playlists.length > 0">
              <div
                v-for="playlist in playlists"
                :key="playlist.id"
                class="playlist-card"
                @click="handlePlaylistClick(playlist)"
              >
                <div class="playlist-cover">
                  <el-image
                    :src="playlist.coverUrl || '/img/default-playlist.jpg'"
                    fit="cover"
                    class="cover-img"
                  >
                    <template #error>
                      <div class="cover-placeholder">
                        <el-icon><Mic /></el-icon>
                      </div>
                    </template>
                  </el-image>
                  
                  <div class="play-overlay">
                    <el-button type="primary" circle @click.stop="playPlaylist(playlist)">
                      <el-icon><VideoPlay /></el-icon>
                    </el-button>
                  </div>
                </div>
                
                <div class="playlist-info">
                  <div class="playlist-name text-ellipsis">{{ playlist.name }}</div>
                  <div class="playlist-stats">
                    <span>{{ playlist.musicCount || 0 }} 首音乐</span>
                    <span>{{ formatPlayCount(playlist.playCount || 0) }} 次播放</span>
                  </div>
                </div>
              </div>
            </div>
            
            <el-empty
              v-else
              description="暂无播放列表"
            >
              <el-button
                v-if="isCurrentUser"
                type="primary"
                @click="createPlaylistVisible = true"
              >
                创建第一个播放列表
              </el-button>
            </el-empty>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="收藏" name="favorites">
          <div class="favorites-section">
            <div class="section-header">
              <h3>收藏的音乐 ({{ favorites.length }})</h3>
            </div>
            
            <div class="music-list" v-if="favorites.length > 0">
              <div
                v-for="(music, index) in favorites"
                :key="music.id"
                class="music-item"
                @dblclick="playMusic(music)"
              >
                <div class="music-index">{{ index + 1 }}</div>
                
                <div class="music-cover">
                  <el-image
                    :src="music.coverUrl || '/img/default-cover.jpg'"
                    fit="cover"
                  >
                    <template #error>
                      <div class="cover-placeholder">
                        <el-icon><Mic /></el-icon>
                      </div>
                    </template>
                  </el-image>
                </div>
                
                <div class="music-info">
                  <div class="music-title" @click="$router.push(`/music/${music.id}`)">
                    {{ music.title }}
                  </div>
                  <div class="music-artist">{{ music.artist }}</div>
                </div>
                
                <div class="music-album">{{ music.album }}</div>
                
                <div class="music-duration">{{ formatDuration(music.duration) }}</div>
                
                <div class="music-actions">
                  <el-button link size="small" @click="playMusic(music)">
                    <el-icon><VideoPlay /></el-icon>
                  </el-button>
                  
                  <el-button link size="small" @click="addToPlaylist(music)">
                    <el-icon><Plus /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
            
            <el-empty
              v-else
              description="暂无收藏的音乐"
            >
              <el-button type="primary" @click="$router.push('/music')">
                去发现音乐
              </el-button>
            </el-empty>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="关注" name="following">
          <div class="following-section">
            <div class="section-header">
              <h3>关注的用户 ({{ following.length }})</h3>
            </div>
            
            <div class="users-grid" v-if="following.length > 0">
              <div
                v-for="user in following"
                :key="user.id"
                class="user-card"
                @click="$router.push(`/user/${user.id}`)"
              >
                <el-avatar :size="60" :src="user.avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                
                <div class="user-name">{{ user.username }}</div>
                <div class="user-stats">
                  {{ user.followerCount || 0 }} 粉丝
                </div>
                
                <el-button
                  v-if="isCurrentUser"
                  type="default"
                  size="small"
                  @click.stop="unfollowUser(user)"
                >
                  取消关注
                </el-button>
              </div>
            </div>
            
            <el-empty
              v-else
              description="暂未关注任何用户"
            >
              <el-button type="primary" @click="$router.push('/discover/users')">
                去发现用户
              </el-button>
            </el-empty>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="粉丝" name="followers">
          <div class="followers-section">
            <div class="section-header">
              <h3>粉丝 ({{ followers.length }})</h3>
            </div>
            
            <div class="users-grid" v-if="followers.length > 0">
              <div
                v-for="user in followers"
                :key="user.id"
                class="user-card"
                @click="$router.push(`/user/${user.id}`)"
              >
                <el-avatar :size="60" :src="user.avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                
                <div class="user-name">{{ user.username }}</div>
                <div class="user-stats">
                  {{ user.followerCount || 0 }} 粉丝
                </div>
                
                <el-button
                  v-if="userStore.isLoggedIn && !isCurrentUser && !user.isFollowing"
                  type="primary"
                  size="small"
                  @click.stop="followUser(user)"
                >
                  关注
                </el-button>
                
                <el-button
                  v-else-if="userStore.isLoggedIn && !isCurrentUser && user.isFollowing"
                  type="default"
                  size="small"
                  @click.stop="unfollowUser(user)"
                >
                  已关注
                </el-button>
              </div>
            </div>
            
            <el-empty
              v-else
              description="暂无粉丝"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>




    <!-- 创建播放列表对话框 -->
    <el-dialog
      v-model="createPlaylistVisible"
      title="创建播放列表"
      width="500px"
      @close="resetCreateForm"
    >
      <el-form :model="createForm" :rules="createRules" ref="createFormRef" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="createForm.name" placeholder="请输入播放列表名称" />
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="createForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入播放列表描述"
          />
        </el-form-item>
        
        <el-form-item label="可见性" prop="isPublic">
          <el-radio-group v-model="createForm.isPublic">
            <el-radio :label="true">公开</el-radio>
            <el-radio :label="false">私有</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="createPlaylistVisible = false">取消</el-button>
        <el-button type="primary" @click="createPlaylist" :loading="creating">
          创建
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  getUserInfo,
  getUserFollowing,
  getUserFollowers,
  unfollowUser as unfollowUserAPI,
  followUser as followUserAPI,
  checkUserFollowed
} from '@/api/user'
import {
  getUserPlaylists
} from '@/api/playlist'
import {
  getFavoriteList
} from '@/api/favorite'
import { ElMessage } from 'element-plus'
import {
  User,
  Message,
  Mic,
  VideoPlay,
  Camera,
  Plus,
  Edit,
  Setting,
  Check
} from '@element-plus/icons-vue'

export default {
  name: 'UserProfile',
  components: {
    User,
    Message,
    Mic,
    VideoPlay,
    Camera,
    Plus,
    Edit,
    Setting,
    Check
  },
  setup() {
    console.log('Profile.vue setup() 开始执行');
    
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    
    console.log('Profile.vue setup() - route.params:', route.params);
    console.log('Profile.vue setup() - userStore.isLoggedIn:', userStore.isLoggedIn);
    console.log('Profile.vue setup() - userStore.userInfo:', userStore.userInfo);
    
    // 响应式数据
    const userInfo = ref(null)
    const loading = ref(false)
    const followLoading = ref(false)
    const isFollowing = ref(false)
    const activeTab = ref('playlists')
    const playlists = ref([])
    const favorites = ref([])
    const following = ref([])
    const followers = ref([])
    const createPlaylistVisible = ref(false)
    const createForm = ref({
      name: '',
      description: ''
    })
    const createRules = ref({
      name: [
        { required: true, message: '请输入歌单名称', trigger: 'blur' }
      ]
    })
    const createFormRef = ref(null)
    const creating = ref(false)
    
    console.log('Profile.vue setup() - 初始化响应式数据完成');
    
    // 计算属性
    const userId = computed(() => {
      const id = route.params.id ? Number(route.params.id) : userStore.user?.id
      console.log('Profile.vue computed userId:', id);
      return id
    })
    
    const isCurrentUser = computed(() => {
      const result = userStore.isLoggedIn && userId.value === userStore.user?.id
      console.log('Profile.vue computed isCurrentUser:', result);
      return result
    })
    
    // 获取用户信息
    const fetchUserInfo = async () => {
      console.log('Profile.vue fetchUserInfo() 开始执行, userId:', userId.value);
      
      if (!userId.value) {
        console.log('Profile.vue fetchUserInfo() - userId为空，返回');
        return
      }
      
      loading.value = true
      console.log('Profile.vue fetchUserInfo() - 设置loading为true');
      
      try {
        console.log('Profile.vue fetchUserInfo() - 准备调用API, userId:', userId.value);
        const response = await getUserInfo(userId.value)
        console.log('Profile.vue fetchUserInfo() - API响应:', response);
        
        // 修复：直接使用response作为用户信息，因为API返回的就是UserDetailVO对象
        if (response) {
          userInfo.value = response
          console.log('Profile.vue fetchUserInfo() - 设置userInfo成功:', userInfo.value);
          
          // 检查关注状态（只有在非当前用户且已登录的情况下）
          if (userStore.isLoggedIn && userId.value !== userStore.user?.id) {
            try {
              const followResponse = await checkUserFollowed(userId.value)
              console.log('Profile.vue fetchUserInfo() - 关注状态响应:', followResponse);
              if (followResponse && followResponse.data) {
                isFollowing.value = followResponse.data.isFollowing
                console.log('Profile.vue fetchUserInfo() - 设置关注状态:', isFollowing.value);
              }
            } catch (followError) {
              console.error('Profile.vue fetchUserInfo() - 获取关注状态失败:', followError);
              // 关注状态获取失败不影响页面显示，只记录错误
            }
          } else {
            // 当前用户或未登录时，重置关注状态
            isFollowing.value = false
          }
          
          // 用户信息加载完成后，加载当前标签页的数据
          console.log('Profile.vue fetchUserInfo() - 准备加载标签页数据:', activeTab.value);
          await handleTabChange(activeTab.value)
        } else {
          console.error('Profile.vue fetchUserInfo() - API响应为空:', response);
          ElMessage.error('获取用户信息失败')
        }
      } catch (error) {
        console.error('Profile.vue fetchUserInfo() - 获取用户信息失败:', error);
        ElMessage.error('获取用户信息失败')
      } finally {
        loading.value = false
        console.log('Profile.vue fetchUserInfo() - 设置loading为false');
      }
    }
    
    // 标签页切换处理
    const handleTabChange = async (tab) => {
      console.log('切换到标签页:', tab)
      activeTab.value = tab
      
      if (!userId.value) {
        console.log('userId为空，无法加载数据')
        return
      }
      
      // 根据标签页加载对应数据
      try {
        switch (tab) {
          case 'playlists':
            await loadPlaylists()
            break
          case 'favorites':
            await loadFavorites()
            break
          case 'following':
            await loadFollowing()
            break
          case 'followers':
            await loadFollowers()
            break
        }
      } catch (error) {
        console.error('加载标签页数据失败:', error)
        ElMessage.error('加载数据失败')
      }
    }
    
    // 关注/取消关注
    const toggleFollow = async () => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      followLoading.value = true
      try {
        if (isFollowing.value) {
          // 取消关注逻辑
          await unfollowUserAPI(userId.value)
          ElMessage.success('已取消关注')
          isFollowing.value = false
          // 更新用户信息中的粉丝数
          if (userInfo.value) {
            userInfo.value.followerCount = Math.max(0, (userInfo.value.followerCount || 0) - 1)
          }
        } else {
          // 关注逻辑
          await followUserAPI(userId.value)
          ElMessage.success('关注成功')
          isFollowing.value = true
          // 更新用户信息中的粉丝数
          if (userInfo.value) {
            userInfo.value.followerCount = (userInfo.value.followerCount || 0) + 1
          }
        }
      } catch (error) {
        console.error('关注操作失败:', error)
        ElMessage.error('操作失败')
      } finally {
        followLoading.value = false
      }
    }
    
    // 发送消息
    const sendMessage = () => {
      ElMessage.info('消息功能开发中')
    }
    
    // 创建歌单
    const createPlaylist = async () => {
      if (!createFormRef.value) return
      
      try {
        const valid = await createFormRef.value.validate()
        if (!valid) return
        
        creating.value = true
        // 创建歌单逻辑
        ElMessage.success('歌单创建成功')
        createPlaylistVisible.value = false
        createForm.value = { name: '', description: '' }
      } catch (error) {
        console.error('创建歌单失败:', error)
        ElMessage.error('创建失败')
      } finally {
        creating.value = false
      }
    }
    
    // 处理播放列表点击
    const handlePlaylistClick = (playlist) => {
      console.log('=== 播放列表点击事件 ===')
      console.log('点击的播放列表:', playlist)
      console.log('播放列表ID:', playlist?.id)
      console.log('播放列表名称:', playlist?.name)
      
      try {
        if (!playlist) {
          console.error('播放列表对象为空')
          ElMessage.error('播放列表数据无效')
          return
        }
        
        if (!playlist.id) {
          console.error('播放列表ID为空，播放列表数据:', playlist)
          ElMessage.error('播放列表ID无效')
          return
        }
        
        const targetRoute = `/playlist/${playlist.id}`
        console.log('准备跳转到:', targetRoute)
        
        // 检查路由是否存在
        const route = router.resolve(targetRoute)
        console.log('路由解析结果:', route)
        
        router.push(targetRoute)
        console.log('路由跳转命令已发送')
        
      } catch (error) {
        console.error('跳转播放列表详情页失败:', error)
        console.error('错误堆栈:', error.stack)
        ElMessage.error('跳转失败: ' + error.message)
      }
      
      console.log('=== 播放列表点击事件处理完成 ===')
    }
    
    // 播放播放列表
    const playPlaylist = async (playlist) => {
      try {
        if (!playlist || !playlist.musics || playlist.musics.length === 0) {
          ElMessage.warning('播放列表为空')
          return
        }
        
        ElMessage.success(`开始播放：${playlist.name}`)
      } catch (error) {
        console.error('播放播放列表失败:', error)
        ElMessage.error('播放失败')
      }
    }
    
    // 播放音乐
    const playMusic = (music) => {
      try {
        ElMessage.success(`播放：${music.title}`)
      } catch (error) {
        console.error('播放音乐失败:', error)
        ElMessage.error('播放失败')
      }
    }
    
    // 添加到播放列表
    const addToPlaylist = () => {
      ElMessage.success('已添加到播放列表')
    }
    
    // 格式化播放次数
    const formatPlayCount = (count) => {
      if (!count && count !== 0) {
        return '0'
      }
      if (count >= 10000) {
        return `${(count / 10000).toFixed(1)}万`
      }
      return count.toString()
    }
    
    // 格式化数字显示
    const formatNumber = (num) => {
      if (!num && num !== 0) {
        return '0'
      }
      if (num >= 100000) {
        return `${(num / 10000).toFixed(1)}万`
      }
      if (num >= 10000) {
        return `${(num / 1000).toFixed(1)}k`
      }
      return num.toString()
    }
    
    // 格式化时长
    const formatDuration = (seconds) => {
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
    }
    
    // 加载歌单数据
    const loadPlaylists = async () => {
      console.log('=== 开始加载播放列表 ===')
      console.log('当前用户ID:', userId.value)
      console.log('当前用户ID类型:', typeof userId.value)
      console.log('userStore.user:', userStore.user)
      console.log('userStore.token:', userStore.token)
      
      if (!userId.value) {
        console.log('用户ID为空，无法加载播放列表')
        return
      }
      
      try {
        console.log('调用getUserPlaylists API，参数:', userId.value)
        console.log('API函数:', getUserPlaylists)
        
        const response = await getUserPlaylists(userId.value)
        console.log('API响应原始数据:', response)
        console.log('API响应类型:', typeof response)
        console.log('API响应是否为数组:', Array.isArray(response))
        
        if (response) {
          // 直接使用response，因为axios拦截器已经处理了data.data
          const newPlaylists = Array.isArray(response) ? response : []
          console.log('准备设置的播放列表数据:', newPlaylists)
          console.log('准备设置的播放列表数量:', newPlaylists.length)
          
          // 检查每个播放列表的结构
          newPlaylists.forEach((playlist, index) => {
            console.log(`播放列表 ${index}:`, {
              id: playlist.id,
              name: playlist.name,
              coverUrl: playlist.coverUrl,
              musicCount: playlist.musicCount,
              playCount: playlist.playCount,
              isPublic: playlist.isPublic
            })
          })
          
          playlists.value = newPlaylists
          console.log('设置播放列表完成，playlists.value.length:', playlists.value.length)
          console.log('设置后的playlists.value:', playlists.value)
          
          // 强制触发响应式更新
          console.log('强制检查响应式状态')
          console.log('playlists.value是否为响应式:', playlists.value.__v_isRef)
          
        } else {
          console.log('API响应为空，设置空数组')
          playlists.value = []
        }
      } catch (error) {
        console.error('获取播放列表失败:', error)
        console.error('错误详情:', error.response)
        console.error('错误消息:', error.message)
        console.error('错误堆栈:', error.stack)
        playlists.value = []
      }
      
      console.log('=== 播放列表加载完成，最终playlists.value.length:', playlists.value.length, ' ===')
    }
    
    // 加载收藏数据
    const loadFavorites = async () => {
      try {
        console.log('开始加载收藏数据, userId:', userId.value)
        const response = await getFavoriteList({ userId: userId.value })
        console.log('收藏数据响应:', response)
        favorites.value = response.data || []
      } catch (error) {
        console.error('加载收藏数据失败:', error)
        favorites.value = []
      }
    }
    
    // 加载关注数据
    const loadFollowing = async () => {
      try {
        console.log('开始加载关注数据, userId:', userId.value)
        const response = await getUserFollowing({ userId: userId.value })
        console.log('关注数据响应:', response)
        following.value = response || []
      } catch (error) {
        console.error('加载关注数据失败:', error)
        following.value = []
      }
    }
    
    // 加载粉丝数据
    const loadFollowers = async () => {
      try {
        console.log('开始加载粉丝数据, userId:', userId.value)
        const response = await getUserFollowers({ userId: userId.value })
        console.log('粉丝数据响应:', response)
        followers.value = response || []
      } catch (error) {
        console.error('加载粉丝数据失败:', error)
        followers.value = []
      }
    }

    // 取消关注用户
    const unfollowUser = async (user) => {
      try {
        if (!userStore.isLoggedIn) {
          ElMessage.warning('请先登录')
          return
        }
        
        await unfollowUserAPI(user.id)
        ElMessage.success(`已取消关注 ${user.username}`)
        
        // 刷新关注列表
        if (activeTab.value === 'following') {
          await loadFollowing()
        }
        // 刷新粉丝列表
        if (activeTab.value === 'followers') {
          await loadFollowers()
        }
      } catch (error) {
        console.error('取消关注失败:', error)
        ElMessage.error('取消关注失败')
      }
    }

    // 关注用户
    const followUser = async (user) => {
      try {
        if (!userStore.isLoggedIn) {
          ElMessage.warning('请先登录')
          return
        }
        
        await followUserAPI(user.id)
        ElMessage.success(`已关注 ${user.username}`)
        
        // 刷新关注列表
        if (activeTab.value === 'following') {
          await loadFollowing()
        }
        // 刷新粉丝列表
        if (activeTab.value === 'followers') {
          await loadFollowers()
        }
      } catch (error) {
        console.error('关注失败:', error)
        ElMessage.error('关注失败')
      }
    }

    // 重置创建表单
    const resetCreateForm = () => {
      createFormRef.value?.resetFields()
    }

    // 监听路由变化
    watch(
      () => route.params.id,
      (newId) => {
        // 如果有路由参数ID或者是当前用户访问自己的profile页面
        if (newId || (route.path === '/profile' && userStore.user)) {
          fetchUserInfo()
          // 重置数据
          playlists.value = []
          favorites.value = []
          following.value = []
          followers.value = []
          activeTab.value = 'playlists'
        }
      },
      { immediate: true }
    )
    
    // 监听用户登录状态变化
    watch(
      () => userStore.user,
      (newUser) => {
        // 当用户登录状态改变且当前在profile页面时，重新获取用户信息
        if (route.path === '/profile' && newUser) {
          fetchUserInfo()
        }
      }
    )
    
    // 监听标签页变化
    watch(activeTab, (newTab) => {
      console.log('activeTab变化:', newTab, 'userId:', userId.value)
      if (userId.value) {
        handleTabChange(newTab)
      }
    })
    
    return {
      userInfo,
      loading,
      activeTab,
      isFollowing,
      followLoading,
      playlists,
      favorites,
      following,
      followers,
      createPlaylistVisible,
      formatNumber,
      createForm,
      createRules,
      createFormRef,
      creating,
      isCurrentUser,
      userStore,
      handleTabChange,
      toggleFollow,
      sendMessage,
      createPlaylist,
      handlePlaylistClick,
      playPlaylist,
      playMusic,
      addToPlaylist,
      formatPlayCount,
      formatDuration,
      resetCreateForm,
      unfollowUser,
      followUser
    }
  }
}
</script>

<style scoped>
.profile-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 用户信息头部 */
.profile-header {
  margin-bottom: 30px;
}

.profile-cover {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.cover-bg {
  height: 240px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  position: relative;
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.3) 100%);
}

.profile-content {
  position: relative;
  padding: 0 40px 40px;
  background: white;
  margin-top: -80px;
  border-radius: 0 0 16px 16px;
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.avatar-section {
  position: relative;
  margin-top: -50px;
  flex-shrink: 0;
}

.avatar-container {
  position: relative;
  display: inline-block;
}

.user-avatar {
  border: 5px solid white;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.avatar-upload-btn {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background: #409eff;
  border: 2px solid white;
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.avatar-upload-btn:hover {
  background: #337ecc;
}

.user-info {
  flex: 1;
  padding-top: 30px;
  min-width: 0;
}

.user-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.username {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin: 0;
  line-height: 1.2;
}

.verified-tag {
  font-size: 12px;
  border-radius: 12px;
}

.user-bio-section {
  margin-bottom: 24px;
}

.user-bio {
  font-size: 16px;
  color: #606266;
  margin: 0;
  line-height: 1.6;
  max-width: 600px;
}

.user-bio.placeholder {
  color: #c0c4cc;
  font-style: italic;
}

.user-stats {
  display: flex;
  gap: 40px;
  margin-bottom: 30px;
  padding: 20px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 16px;
  border-radius: 8px;
}

.stat-item:hover {
  background-color: #f5f7fa;
  transform: translateY(-2px);
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  font-weight: 500;
}

.user-actions {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.action-btn {
  border-radius: 20px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 内容标签页 */
.profile-content-tabs {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

/* 播放列表网格 */
.playlists-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.playlist-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.playlist-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.playlist-cover {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.cover-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 40px;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.playlist-card:hover .play-overlay {
  opacity: 1;
}

.playlist-info {
  padding: 15px;
}

.playlist-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.playlist-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

/* 音乐列表 */
.music-list {
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

.music-item {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.music-item:hover {
  background-color: #f5f7fa;
}

.music-item:last-child {
  border-bottom: none;
}

.music-index {
  width: 40px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.music-cover {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 10px;
  flex-shrink: 0;
}

.music-info {
  flex: 1;
  min-width: 0;
  margin-right: 15px;
}

.music-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 2px;
  cursor: pointer;
}

.music-title:hover {
  color: #409eff;
}

.music-artist {
  font-size: 12px;
  color: #909399;
}

.music-album {
  width: 150px;
  font-size: 12px;
  color: #909399;
  margin-right: 15px;
}

.music-duration {
  width: 60px;
  text-align: right;
  font-size: 12px;
  color: #909399;
  margin-right: 15px;
}

.music-actions {
  display: flex;
  gap: 5px;
  opacity: 0;
  transition: opacity 0.2s;
}

.music-item:hover .music-actions {
  opacity: 1;
}

/* 用户网格 */
.users-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.user-card {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 10px 0 5px;
}

.user-stats {
  font-size: 12px;
  color: #909399;
  margin-bottom: 15px;
}

/* 对话框样式 */
.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar-preview {
  border-radius: 8px;
}

.avatar-uploader-icon {
  width: 150px;
  height: 150px;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.2s;
  font-size: 28px;
  color: #8c939d;
}

.avatar-uploader-icon:hover {
  border-color: #409eff;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
  line-height: 1.4;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-page {
    padding: 10px;
  }
  
  .profile-cover {
    border-radius: 12px;
  }
  
  .cover-bg {
    height: 180px;
  }
  
  .profile-content {
    flex-direction: column;
    text-align: center;
    padding: 20px;
    margin-top: -60px;
    gap: 20px;
  }
  
  .avatar-section {
    margin-top: -40px;
    align-self: center;
  }
  
  .user-info {
    padding-top: 10px;
  }
  
  .username {
    font-size: 28px;
  }
  
  .user-stats {
    justify-content: center;
    gap: 20px;
    padding: 15px 0;
  }
  
  .stat-item {
    padding: 6px 12px;
  }
  
  .stat-number {
    font-size: 20px;
  }
  
  .user-actions {
    justify-content: center;
  }
  
  .action-btn {
    padding: 8px 16px;
    font-size: 14px;
  }
  
  .playlists-grid,
  .users-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
  
  .music-album,
  .music-duration {
    display: none;
  }
}

@media (max-width: 576px) {
  .cover-bg {
    height: 150px;
  }
  
  .profile-content {
    padding: 15px;
  }
  
  .username {
    font-size: 24px;
  }
  
  .user-stats {
    gap: 15px;
    flex-wrap: wrap;
  }
  
  .stat-item {
    min-width: 80px;
  }
  
  .playlists-grid,
  .users-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .action-btn {
    flex: 1;
    min-width: 120px;
  }
}
</style>