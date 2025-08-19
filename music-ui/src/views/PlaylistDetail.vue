<template>
  <div class="playlist-detail-page">
    <!-- 歌单信息头部 -->
    <div class="playlist-header" v-loading="loading">
      <div class="playlist-cover">
        <img 
          :src="playlistInfo.coverUrl || 'https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/default-product.jpg'" 
          :alt="playlistInfo.name"
          @error="handleImageError"
        />
      </div>
      
      <div class="playlist-info">
        <h1 class="playlist-title">{{ playlistInfo.name }}</h1>
        <p class="playlist-description">{{ playlistInfo.description || '暂无描述' }}</p>
        
        <div class="playlist-meta">
          <span class="creator">创建者：{{ playlistInfo.creatorName }}</span>
          <span class="create-time">创建时间：{{ formatDate(playlistInfo.createTime) }}</span>
          <span class="music-count">{{ musicList.length }}首音乐</span>
          <span class="play-count">播放{{ formatPlayCount(playlistInfo.playCount || 0) }}</span>
        </div>
        
        <div class="playlist-actions">
          <el-button 
            type="primary" 
            size="large"
            :disabled="musicList.length === 0"
            @click="playAll"
          >
            <el-icon><VideoPlay /></el-icon>
            播放全部
          </el-button>
          
          <el-button 
            v-if="isOwner && !isFavoriteMusic"
            size="large"
            @click="editPlaylist"
          >
            <el-icon><Edit /></el-icon>
            编辑歌单
          </el-button>
          
          <el-button 
            v-if="!isOwner && userStore.isLoggedIn && !isFavoriteMusic"
            size="large"
            :type="isCollected ? 'danger' : 'default'"
            @click="toggleCollect"
            :loading="collectLoading"
          >
            <el-icon><Star /></el-icon>
            {{ isCollected ? '取消收藏' : '收藏歌单' }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 音乐列表 -->
    <div class="music-list-section">
      <div class="section-header">
        <h2>音乐列表</h2>
        <div class="list-actions" v-if="isOwner && !isFavoriteMusic">
          <el-button 
            size="small"
            @click="showAddMusicDialog"
          >
            <el-icon><Plus /></el-icon>
            添加音乐
          </el-button>
        </div>
      </div>
      
      <div class="music-list" v-loading="musicLoading">
        <!-- 列表头部 -->
        <div class="list-header">
          <div class="col-index">#</div>
          <div class="col-title">标题</div>
          <div class="col-artist">艺术家</div>
          <div class="col-album">专辑</div>
          <div class="col-duration">时长</div>
          <div class="col-actions">操作</div>
        </div>
        
        <!-- 音乐项 -->
        <div 
          v-for="(music, index) in musicList" 
          :key="music.id"
          class="music-item"
          :class="{ 'playing': currentMusic?.id === music.id }"
          @dblclick="playMusic(music, index)"
        >
          <div class="col-index">
            <span v-if="currentMusic?.id !== music.id">{{ index + 1 }}</span>
            <el-icon v-else class="playing-icon"><VideoPlay /></el-icon>
          </div>
          
          <div class="col-title">
            <img 
              :src="music.coverUrl || '/default-music.png'" 
              :alt="music.title"
              class="music-cover"
              @error="handleImageError"
            />
            <div class="title-info">
              <span class="title">{{ music.title }}</span>
              <div class="tags" v-if="music.tags">
                <el-tag 
                  v-for="tag in music.tags.split(',')"
                  :key="tag"
                  size="small"
                  type="info"
                >
                  {{ tag.trim() }}
                </el-tag>
              </div>
            </div>
          </div>
          
          <div class="col-artist">{{ music.artist }}</div>
          <div class="col-album">{{ music.album || '-' }}</div>
          <div class="col-duration">{{ formatDuration(music.duration) }}</div>
          
          <div class="col-actions">
            <el-button 
              size="small"
              circle
              @click="playMusic(music, index)"
            >
              <el-icon><VideoPlay /></el-icon>
            </el-button>
            
            <el-dropdown 
              @command="(command) => handleMusicCommand(command, music)"
              trigger="click"
            >
              <el-button size="small" circle>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="addToPlaylist">
                    <el-icon><Plus /></el-icon>
                    添加到播放列表
                  </el-dropdown-item>
                  <el-dropdown-item command="download">
                    <el-icon><Download /></el-icon>
                    下载
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-if="isOwner && !isFavoriteMusic"
                    command="remove"
                    divided
                  >
                    <el-icon><Delete /></el-icon>
                    从歌单中移除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
        
        <!-- 空状态 -->
        <div v-if="!musicLoading && musicList.length === 0" class="empty-music">
          <el-empty description="歌单中还没有音乐">
            <el-button 
              v-if="isOwner && !isFavoriteMusic"
              type="primary" 
              @click="showAddMusicDialog"
            >
              添加音乐
            </el-button>
          </el-empty>
        </div>
      </div>
    </div>

    <!-- 编辑歌单对话框 -->
    <el-dialog 
      v-model="editDialogVisible" 
      title="编辑歌单"
      width="500px"
      @close="resetEditForm"
    >
      <el-form 
        ref="editFormRef" 
        :model="editForm" 
        :rules="editRules" 
        label-width="80px"
      >
        <el-form-item label="歌单名称" prop="name">
          <el-input 
            v-model="editForm.name" 
            placeholder="请输入歌单名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="歌单描述" prop="description">
          <el-input 
            v-model="editForm.description" 
            type="textarea" 
            placeholder="请输入歌单描述（可选）"
            :rows="3"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="歌单封面">
          <div class="cover-upload-section">
            <div class="current-cover">
              <img 
                :src="editForm.coverUrl || 'https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/default-product.jpg'" 
                alt="歌单封面"
                class="cover-preview"
              />
            </div>
            <div class="upload-actions">
              <el-upload
                ref="uploadRef"
                :action="uploadAction"
                :headers="uploadHeaders"
                :show-file-list="false"
                :before-upload="beforeCoverUpload"
                :on-success="handleCoverUploadSuccess"
                :on-error="handleCoverUploadError"
                accept="image/*"
              >
                <el-button size="small" type="primary">
                  <el-icon><Upload /></el-icon>
                  上传封面
                </el-button>
              </el-upload>
              <el-button 
                v-if="editForm.coverUrl"
                size="small" 
                @click="removeCover"
              >
                移除封面
              </el-button>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item label="可见性">
          <el-radio-group v-model="editForm.isPublic">
            <el-radio :label="true">公开</el-radio>
            <el-radio :label="false">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          :loading="editSubmitting" 
          @click="submitEdit"
        >
          保存
        </el-button>
      </template>
    </el-dialog>

    <!-- 添加音乐对话框 -->
    <el-dialog 
      v-model="addMusicDialogVisible" 
      title="添加音乐到歌单"
      width="800px"
    >
      <div class="add-music-content">
        <el-input 
          v-model="searchKeyword"
          placeholder="搜索音乐"
          @keyup.enter="searchMusic"
        >
          <template #append>
            <el-button @click="searchMusic">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        
        <div class="search-results" v-loading="searchLoading">
          <div 
            v-for="music in searchResults" 
            :key="music.id"
            class="search-item"
          >
            <img 
              :src="music.coverUrl || '/default-music.png'" 
              :alt="music.title"
              class="search-cover"
              @error="handleImageError"
            />
            <div class="search-info">
              <div class="search-title">{{ music.title }}</div>
              <div class="search-artist">{{ music.artist }}</div>
            </div>
            <el-button 
              size="small"
              type="primary"
              :disabled="isInPlaylist(music.id)"
              @click="addMusicToCurrentPlaylist(music)"
            >
              {{ isInPlaylist(music.id) ? '已添加' : '添加' }}
            </el-button>
          </div>
          
          <div v-if="!searchLoading && searchResults.length === 0 && searchKeyword" class="no-results">
            <el-empty description="没有找到相关音乐" />
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  VideoPlay,
  Edit,
  Star,
  Plus,
  MoreFilled,
  Download,
  Delete,
  Search,
  Upload
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { usePlayerStore } from '@/stores/player'
import {
  getPlaylistById,
  getPlaylistMusics,
  updatePlaylist,
  removeMusicFromPlaylist,
  addMusicToPlaylist,
  favoritePlaylist,
  unfavoritePlaylist,
  isPlaylistFavorited
} from '@/api/playlist'
import { searchMusic as searchMusicAPI } from '@/api/music'
import dayjs from 'dayjs'

export default {
  name: 'PlaylistDetail',
  components: {
    VideoPlay,
    Edit,
    Star,
    Plus,
    MoreFilled,
    Download,
    Delete,
    Search,
    Upload
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    const playerStore = usePlayerStore()
    
    // 响应式数据
    const loading = ref(false)
    const musicLoading = ref(false)
    const collectLoading = ref(false)
    const editSubmitting = ref(false)
    const searchLoading = ref(false)
    
    const playlistInfo = ref({})
    const musicList = ref([])
    const isCollected = ref(false)
    
    const editDialogVisible = ref(false)
    const addMusicDialogVisible = ref(false)
    const searchKeyword = ref('')
    const searchResults = ref([])
    
    const editFormRef = ref(null)
    const uploadRef = ref(null)
    
    // 上传相关
    const uploadAction = computed(() => {
      return `${import.meta.env.VITE_API_BASE_URL}/api/playlist/upload-cover`
    })
    
    const uploadHeaders = computed(() => {
      const token = userStore.token
      return token ? { Authorization: `Bearer ${token}` } : {}
    })
    
    // 编辑表单
    const editForm = reactive({
      name: '',
      description: '',
      isPublic: true,
      coverUrl: ''
    })
    
    const editRules = {
      name: [
        { required: true, message: '请输入歌单名称', trigger: 'blur' },
        { min: 1, max: 50, message: '歌单名称长度在 1 到 50 个字符', trigger: 'blur' }
      ]
    }
    
    // 计算属性
    const playlistId = computed(() => route.params.id)
    const isFavoriteMusic = computed(() => playlistId.value === '-1') // 判断是否为"收藏的音乐"
    const isOwner = computed(() => {
      return userStore.isLoggedIn && 
             (userStore.userInfo?.id === playlistInfo.value.creatorId || isFavoriteMusic.value)
    })
    const currentMusic = computed(() => playerStore.currentMusic)
    
    // 获取歌单详情
    const fetchPlaylistDetail = async () => {
      console.log('=== fetchPlaylistDetail 开始 ===')
      console.log('playlistId:', playlistId.value)
      console.log('isFavoriteMusic:', isFavoriteMusic.value)
      
      try {
        loading.value = true
        
        if (isFavoriteMusic.value) {
          console.log('处理收藏的音乐特殊情况')
          // 如果是"收藏的音乐"，设置特殊的歌单信息
          playlistInfo.value = {
            id: -1,
            name: '收藏的音乐',
            description: '我收藏的所有音乐',
            coverUrl: '/default-playlist.png',
            creatorName: userStore.userInfo?.username || '我',
            creatorId: userStore.userInfo?.id,
            createTime: new Date().toISOString(),
            playCount: 0,
            isPublic: false
          }
          console.log('收藏的音乐信息设置完成:', playlistInfo.value)
        } else {
          console.log('获取普通歌单信息')
          const response = await getPlaylistById(playlistId.value)
          // request.js已经解包了一层data，所以直接使用response
          playlistInfo.value = response || {}
          console.log('普通歌单信息获取完成:', playlistInfo.value)
          
          // 检查收藏状态（只有非"我喜欢的音乐"且非自己的歌单才需要检查收藏状态）
          if (userStore.isLoggedIn && !isOwner.value) {
            await checkCollectStatus()
          }
        }
      } catch (error) {
        console.error('获取歌单详情失败:', error)
        if (error.response?.status === 404) {
          ElMessage.error('歌单不存在')
          router.push('/playlist')
        } else {
          ElMessage.error('获取歌单详情失败')
        }
      } finally {
        loading.value = false
        console.log('=== fetchPlaylistDetail 结束 ===')
      }
    }
    
    // 获取歌单音乐列表
    const fetchMusicList = async () => {
      console.log('=== fetchMusicList 开始 ===')
      console.log('playlistId:', playlistId.value)
      
      try {
        musicLoading.value = true
        
        // 统一使用歌单音乐接口，后端会处理ID为-1的特殊情况
        console.log('调用 getPlaylistMusics API')
        const response = await getPlaylistMusics(playlistId.value)
        console.log('API响应:', response)
        // request.js已经解包了一层data，所以直接使用response
        musicList.value = response || []
        console.log('音乐列表设置完成，数量:', musicList.value.length)
      } catch (error) {
        console.error('获取音乐列表失败:', error)
        ElMessage.error('获取音乐列表失败')
      } finally {
        musicLoading.value = false
        console.log('=== fetchMusicList 结束 ===')
      }
    }
    
    // 检查收藏状态
    const checkCollectStatus = async () => {
      // 如果是"收藏的音乐"，不需要检查收藏状态
      if (isFavoriteMusic.value) {
        isCollected.value = false
        return
      }
      
      try {
        const response = await isPlaylistFavorited(playlistId.value)
        // request.js已经解包了一层data，所以直接使用response
        isCollected.value = response || false
      } catch (error) {
        console.error('检查收藏状态失败:', error)
      }
    }
    
    // 播放全部
    const playAll = () => {
      if (musicList.value.length === 0) {
        ElMessage.warning('歌单为空')
        return
      }
      
      playerStore.setPlaylist(musicList.value)
      playerStore.play(musicList.value[0], 0)
      ElMessage.success('开始播放歌单')
    }
    
    // 播放音乐
    const playMusic = (music, index) => {
      playerStore.setPlaylist(musicList.value)
      playerStore.play(music, index)
    }
    
    // 编辑歌单
    const editPlaylist = () => {
      editForm.name = playlistInfo.value.name
      editForm.description = playlistInfo.value.description || ''
      editForm.isPublic = playlistInfo.value.isPublic
      editForm.coverUrl = playlistInfo.value.coverUrl || ''
      editDialogVisible.value = true
    }
    
    // 重置编辑表单
    const resetEditForm = () => {
      editFormRef.value?.resetFields()
    }
    
    // 提交编辑
    const submitEdit = async () => {
      try {
        await editFormRef.value.validate()
        editSubmitting.value = true
        
        await updatePlaylist(playlistId.value, {
          name: editForm.name,
          description: editForm.description,
          isPublic: editForm.isPublic,
          coverUrl: editForm.coverUrl
        })
        
        ElMessage.success('歌单更新成功')
        editDialogVisible.value = false
        await fetchPlaylistDetail()
      } catch (error) {
        console.error('更新失败:', error)
        ElMessage.error('更新失败，请重试')
      } finally {
        editSubmitting.value = false
      }
    }
    
    // 切换收藏
    const toggleCollect = async () => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      try {
        collectLoading.value = true
        
        if (isCollected.value) {
          await unfavoritePlaylist(playlistId.value)
          isCollected.value = false
          ElMessage.success('取消收藏成功')
        } else {
          await favoritePlaylist(playlistId.value)
          isCollected.value = true
          ElMessage.success('收藏成功')
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        ElMessage.error('操作失败，请重试')
      } finally {
        collectLoading.value = false
      }
    }
    
    // 显示添加音乐对话框
    const showAddMusicDialog = () => {
      addMusicDialogVisible.value = true
      searchKeyword.value = ''
      searchResults.value = []
    }
    
    // 搜索音乐
    const searchMusic = async () => {
      if (!searchKeyword.value.trim()) {
        ElMessage.warning('请输入搜索关键词')
        return
      }
      
      try {
        searchLoading.value = true
        const response = await searchMusicAPI({
          keyword: searchKeyword.value,
          page: 1,
          size: 20
        })
        // request.js已经解包了一层data，检查response的结构
        if (Array.isArray(response)) {
          searchResults.value = response
        } else if (response?.records) {
          searchResults.value = response.records
        } else {
          searchResults.value = []
        }
      } catch (error) {
        console.error('搜索失败:', error)
        ElMessage.error('搜索失败，请重试')
      } finally {
        searchLoading.value = false
      }
    }
    
    // 检查音乐是否已在歌单中
    const isInPlaylist = (musicId) => {
      return musicList.value.some(music => music.id === musicId)
    }
    
    // 添加音乐到歌单
    const addMusicToCurrentPlaylist = async (music) => {
      try {
        await addMusicToPlaylist(playlistId.value, music.id)
        ElMessage.success('添加成功')
        await fetchMusicList()
        addMusicDialogVisible.value = false
        searchKeyword.value = ''
        searchResults.value = []
      } catch (error) {
        console.error('添加失败:', error)
        if (error.response?.status === 409) {
          ElMessage.warning('该音乐已在歌单中')
        } else {
          ElMessage.error('添加失败，请重试')
        }
      }
    }
    
    // 处理音乐操作命令
    const handleMusicCommand = async (command, music) => {
      switch (command) {
        case 'addToPlaylist':
          playerStore.addToPlaylist(music)
          ElMessage.success('已添加到播放列表')
          break
          
        case 'download':
          // 下载功能
          ElMessage.info('下载功能开发中')
          break
          
        case 'remove':
          await removeMusicFromPlaylistConfirm(music)
          break
      }
    }
    
    // 确认从歌单中移除音乐
    const removeMusicFromPlaylistConfirm = async (music) => {
      try {
        await ElMessageBox.confirm(
          `确定要从歌单中移除「${music.title}」吗？`,
          '确认移除',
          {
            type: 'warning'
          }
        )
        
        await removeMusicFromPlaylist(playlistId.value, music.id)
        ElMessage.success('移除成功')
        await fetchMusicList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('移除失败:', error)
          ElMessage.error('移除失败，请重试')
        }
      }
    }
    
    // 处理图片加载错误
    const handleImageError = (event) => {
      if (event.target.src.includes('default-music.png')) {
        return
      }
      event.target.src = '/default-music.png'
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
    
    // 格式化时长
    const formatDuration = (seconds) => {
      if (!seconds) return '00:00'
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
    }
    
    // 格式化日期
    const formatDate = (date) => {
      return dayjs(date).format('YYYY-MM-DD')
    }
    
    // 上传前验证
    const beforeCoverUpload = (file) => {
      const isImage = file.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5
      
      if (!isImage) {
        ElMessage.error('只能上传图片文件！')
        return false
      }
      if (!isLt5M) {
        ElMessage.error('图片大小不能超过 5MB！')
        return false
      }
      return true
    }
    
    // 上传成功处理
    const handleCoverUploadSuccess = (response) => {
      if (response.success && response.data) {
        editForm.coverUrl = response.data
        ElMessage.success('封面上传成功')
      } else {
        ElMessage.error('上传失败：' + (response.message || '未知错误'))
      }
    }
    
    // 上传失败处理
    const handleCoverUploadError = (error) => {
      console.error('上传失败:', error)
      ElMessage.error('上传失败，请重试')
    }
    
    // 移除封面
    const removeCover = () => {
      editForm.coverUrl = ''
      ElMessage.success('已移除封面')
    }
    
    // 监听路由变化
    watch(
      () => route.params.id,
      (newId) => {
        console.log('=== 路由参数变化 ===')
        console.log('新的播放列表ID:', newId)
        if (newId) {
          console.log('开始获取播放列表数据')
          fetchPlaylistDetail()
          fetchMusicList()
        }
      },
      { immediate: true }
    )
    
    onMounted(() => {
      console.log('=== PlaylistDetail 组件已挂载 ===')
      console.log('当前路由参数:', route.params)
      // 组件挂载时已通过 watch 触发数据获取
    })
    
    return {
      loading,
      musicLoading,
      collectLoading,
      editSubmitting,
      searchLoading,
      playlistInfo,
      musicList,
      isCollected,
      isOwner,
      isFavoriteMusic,
      currentMusic,
      editDialogVisible,
      addMusicDialogVisible,
      searchKeyword,
      searchResults,
      editFormRef,
      editForm,
      editRules,
      uploadRef,
      uploadAction,
      uploadHeaders,
      userStore,
      playAll,
      playMusic,
      editPlaylist,
      resetEditForm,
      submitEdit,
      toggleCollect,
      showAddMusicDialog,
      searchMusic,
      isInPlaylist,
      addMusicToCurrentPlaylist,
      handleMusicCommand,
      handleImageError,
      formatPlayCount,
      formatDuration,
      formatDate,
      beforeCoverUpload,
      handleCoverUploadSuccess,
      handleCoverUploadError,
      removeCover
    }
  }
}
</script>

<style scoped>
.playlist-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 歌单头部 */
.playlist-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.playlist-cover {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.playlist-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.playlist-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.playlist-title {
  margin: 0 0 12px 0;
  font-size: 32px;
  font-weight: 600;
  color: #333;
  line-height: 1.2;
}

.playlist-description {
  margin: 0 0 20px 0;
  font-size: 16px;
  color: #666;
  line-height: 1.5;
}

.playlist-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 24px;
  font-size: 14px;
  color: #999;
}

.playlist-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

/* 音乐列表部分 */
.music-list-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.section-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.music-list {
  min-height: 200px;
}

.list-header {
  display: grid;
  grid-template-columns: 60px 1fr 150px 150px 80px 120px;
  gap: 16px;
  padding: 12px 24px;
  background: #fafafa;
  font-size: 12px;
  font-weight: 600;
  color: #999;
  text-transform: uppercase;
}

.music-item {
  display: grid;
  grid-template-columns: 60px 1fr 150px 150px 80px 120px;
  gap: 16px;
  padding: 12px 24px;
  border-bottom: 1px solid #f5f5f5;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.music-item:hover {
  background: #f8f9fa;
}

.music-item.playing {
  background: #e8f4fd;
}

.col-index {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #999;
}

.playing-icon {
  color: #409eff;
}

.col-title {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.music-cover {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  object-fit: cover;
  flex-shrink: 0;
}

.title-info {
  min-width: 0;
  flex: 1;
}

.title {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 4px;
}

.tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.col-artist,
.col-album {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.col-duration {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #999;
}

.col-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.music-item:hover .col-actions {
  opacity: 1;
}

.empty-music {
  padding: 60px 20px;
  text-align: center;
}

/* 添加音乐对话框 */
.add-music-content {
  max-height: 500px;
}

.search-results {
  margin-top: 16px;
  max-height: 400px;
  overflow-y: auto;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 8px;
  transition: all 0.2s ease;
}

.search-item:hover {
  border-color: #409eff;
  background: #f8f9fa;
}

.search-cover {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
  flex-shrink: 0;
}

.search-info {
  flex: 1;
  min-width: 0;
}

.search-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.search-artist {
  font-size: 12px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.no-results {
  text-align: center;
  padding: 40px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .playlist-detail-page {
    padding: 15px;
  }
  
  .playlist-header {
    flex-direction: column;
    gap: 20px;
    padding: 20px;
  }
  
  .playlist-cover {
    width: 150px;
    height: 150px;
    align-self: center;
  }
  
  .playlist-title {
    font-size: 24px;
    text-align: center;
  }
  
  .playlist-meta {
    justify-content: center;
    text-align: center;
  }
  
  .playlist-actions {
    justify-content: center;
  }
  
  .list-header {
    display: none;
  }
  
  .music-item {
    grid-template-columns: 1fr;
    gap: 8px;
    padding: 16px;
  }
  
  .col-index {
    display: none;
  }
  
  .col-title {
    grid-column: 1;
  }
  
  .col-artist,
  .col-album,
  .col-duration {
    font-size: 12px;
    color: #999;
  }
  
  .col-actions {
    opacity: 1;
    justify-content: flex-end;
  }
}
</style>