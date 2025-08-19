<template>
  <div class="music-player" :class="{ 'collapsed': isCollapsed }">
    <!-- 播放器主体 -->
    <div class="player-container">
      <!-- 音乐信息区域 -->
      <div class="music-info" v-if="currentMusic">
        <div class="music-cover">
          <el-image
            :src="currentMusic.coverUrl || '/img/default-cover.jpg'"
            fit="cover"
            class="cover-img"
            :class="{ 'rotating': playerStore.isPlaying }"
            @click="$router.push(`/music/${currentMusic.id}`)"
          >
            <template #error>
              <div class="cover-placeholder">
                <el-icon><Mic /></el-icon>
              </div>
            </template>
          </el-image>
        </div>
        
        <div class="music-details">
          <div class="music-title text-ellipsis" @click="$router.push(`/music/${currentMusic.id}`)">
            {{ currentMusic.title }}
          </div>
          <div class="music-artist text-ellipsis">
            {{ currentMusic.artist }}
          </div>
        </div>
      </div>
      
      <div class="music-info" v-else>
        <div class="music-cover">
          <div class="cover-placeholder">
            <el-icon><Headset /></el-icon>
          </div>
        </div>
        
        <div class="music-details">
          <div class="music-title">未播放</div>
          <div class="music-artist">选择音乐开始播放</div>
        </div>
      </div>
      
      <!-- 播放控制区域 -->
      <div class="player-controls">
        <!-- 播放控制按钮 -->
        <div class="control-buttons">
          <el-button
            link
            @click="handlePrevious"
            :class="{ 'disabled': !playerStore.hasPrevious || !currentMusic }"
          >
            <el-icon><Back /></el-icon>
          </el-button>
          
          <el-button
            link
            :disabled="!currentMusic"
            @click="playerStore.togglePlay"
            class="play-button"
          >
            <el-icon v-if="playerStore.isPlaying"><VideoPause /></el-icon>
            <el-icon v-else><VideoPlay /></el-icon>
          </el-button>
          
          <el-button
            link
            @click="handleNext"
            :class="{ 'disabled': !playerStore.hasNext || !currentMusic }"
          >
            <el-icon><Right /></el-icon>
          </el-button>
        </div>
        
        <!-- 播放进度条 -->
        <div class="progress-container">
          <span class="time-current">{{ playerStore.formattedCurrentTime }}</span>
          
          <el-slider
            v-model="sliderValue"
            :disabled="!currentMusic"
            @change="handleProgressChange"
            @input="handleProgressInput"
            :show-tooltip="true"
            :format-tooltip="formatTooltip"
            class="progress-slider"
          />
          
          <span class="time-total">{{ playerStore.formattedDuration }}</span>
        </div>
      </div>
      
      <!-- 播放选项区域 -->
      <div class="player-options">
        <!-- 收藏按钮 -->
        <el-tooltip
          :content="isFavorited ? '取消收藏' : '收藏'"
          placement="top"
        >
          <el-button
            link
            @click="toggleFavorite"
            :disabled="!currentMusic"
            class="favorite-button"
            :class="{ 'favorited': isFavorited }"
          >
            <el-icon v-if="isFavorited"><StarFilled /></el-icon>
            <el-icon v-else><Star /></el-icon>
          </el-button>
        </el-tooltip>
        
        <!-- 播放模式 -->
        <el-tooltip
          :content="playModeText"
          placement="top"
        >
          <el-button
            link
            @click="playerStore.togglePlayMode"
            class="mode-button"
          >
            <el-icon v-if="playerStore.playMode === 'sequence'"><List /></el-icon>
            <el-icon v-else-if="playerStore.playMode === 'single'"><Refresh /></el-icon>
            <el-icon v-else-if="playerStore.playMode === 'random'"><Sort /></el-icon>
            <el-icon v-else><List /></el-icon>
          </el-button>
        </el-tooltip>
        
        <!-- 音量控制 -->
        <div class="volume-control">
          <el-button
            link
            @click="handleToggleMute"
            class="volume-button"
          >
            <el-icon v-if="playerStore.muted"><Mute /></el-icon>
        <el-icon v-else><Bell /></el-icon>
          </el-button>
          
          <el-slider
            v-model="volumeValue"
            @change="handleVolumeChange"
            @input="handleVolumeInput"
            :show-tooltip="false"
            class="volume-slider"
          />
        </div>
        
        <!-- 播放列表按钮 -->
        <el-badge
          :value="playerStore.playlist.length"
          :hidden="playerStore.playlist.length === 0"
          class="playlist-badge"
        >
          <el-button
            link
            @click="showPlaylist = !showPlaylist"
            class="playlist-button"
          >
            <el-icon><Document /></el-icon>
          </el-button>
        </el-badge>
      </div>
    </div>
    
    <!-- 播放列表抽屉 -->
    <el-drawer
      v-model="showPlaylist"
      title="播放列表"
      direction="rtl"
      size="300px"
    >
      <div class="playlist-header">
        <div class="playlist-title">
          <span>当前播放 ({{ playerStore.playlist.length }})</span>
        </div>
        <div class="playlist-actions">
          <el-button
            link
            size="small"
            @click="playerStore.clearPlaylist"
            :disabled="playerStore.playlist.length === 0"
          >
            <el-icon><Delete /></el-icon>
            <span>清空</span>
          </el-button>
        </div>
      </div>
      
      <el-scrollbar height="calc(100% - 50px)">
        <el-empty
          v-if="playerStore.playlist.length === 0"
          description="播放列表为空"
        />
        
        <ul v-else class="playlist-items">
          <li
            v-for="(item, index) in playerStore.playlist"
            :key="item.id"
            :class="{
              'playlist-item': true,
              'active': index === playerStore.currentIndex
            }"
            @click="playerStore.play(item, index)"
          >
            <div class="item-info">
              <div class="item-index">
                <el-icon v-if="index === playerStore.currentIndex && playerStore.isPlaying">
                  <Loading />
                </el-icon>
                <span v-else>{{ index + 1 }}</span>
              </div>
              
              <div class="item-cover">
                <el-image
                  :src="item.coverUrl || '/img/default-cover.jpg'"
                  fit="cover"
                >
                  <template #error>
                    <div class="item-cover-placeholder">
                      <el-icon><Mic /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              
              <div class="item-details">
                <div class="item-title text-ellipsis">{{ item.title }}</div>
                <div class="item-artist text-ellipsis">{{ item.artist }}</div>
              </div>
            </div>
            
            <div class="item-actions">
              <el-button
                link
                size="small"
                @click.stop="playerStore.removeFromPlaylist(index)"
              >
                <el-icon><Close /></el-icon>
              </el-button>
            </div>
          </li>
        </ul>
      </el-scrollbar>
    </el-drawer>
    
    <!-- 折叠/展开按钮 -->
    <div class="collapse-button" @click="toggleCollapse">
      <el-icon v-if="isCollapsed"><ArrowUp /></el-icon>
      <el-icon v-else><ArrowDown /></el-icon>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { usePlayerStore } from '@/stores/player'
import { useUserStore } from '@/stores/user'
import { addFavorite, removeFavorite, isFavorite } from '@/api/favorite'
import { recordPlayHistory } from '@/api/history'
import { ElMessage } from 'element-plus'
import {
  Mic,
  Back,
  Right,
  VideoPlay,
  VideoPause,
  List,
  Sort,
  Refresh,
  Mute,
  Bell,
  Document,
  Delete,
  Close,
  Loading,
  ArrowUp,
  ArrowDown,
  Star,
  StarFilled,
  Headset
} from '@element-plus/icons-vue'

export default {
  name: 'MusicPlayer',
  components: {
    Mic,
    Back,
    Right,
    VideoPlay,
    VideoPause,
    List,
    Sort,
    Refresh,
    Mute,
    Bell,
    Document,
    Delete,
    Close,
    Loading,
    ArrowUp,
    ArrowDown,
    Star,
    StarFilled,
    Headset
  },
  setup() {
    const playerStore = usePlayerStore()
    const userStore = useUserStore()
    const showPlaylist = ref(false)
    const isCollapsed = ref(false)
    
    // 进度条值
    const sliderValue = ref(0)
    
    // 是否正在拖拽进度条
    const isDragging = ref(false)
    
    // 音量值
    const volumeValue = ref(playerStore.volume * 100)
    
    // 收藏状态
    const isFavorited = ref(false)
    
    // 当前音乐
    const currentMusic = computed(() => playerStore.currentMusic)
    
    // 播放模式文本
    const playModeText = computed(() => {
      const modes = {
        sequence: '顺序播放',
        single: '单曲循环',
        random: '随机播放'
      }
      return modes[playerStore.playMode] || '顺序播放'
    })
    
    // 监听播放进度变化（只在非拖拽状态下更新）
    watch(() => playerStore.progress, (newValue) => {
      if (!isDragging.value) {
        sliderValue.value = newValue
      }
    })
    
    // 监听音量变化
    watch(() => playerStore.volume, (newValue) => {
      volumeValue.value = newValue * 100
    })
    
    // 监听静音状态
    watch(() => playerStore.muted, (newValue) => {
      if (newValue) {
        volumeValue.value = 0
      } else {
        volumeValue.value = playerStore.volume * 100
      }
    })
    
    // 监听音量条变化，实现双向绑定
    watch(volumeValue, (newValue) => {
      // 当音量条为0时，如果当前不是静音状态，则切换为静音
      if (newValue === 0 && !playerStore.muted) {
        playerStore.toggleMute()
      }
    })
    
    // 处理进度条拖拽输入（实时更新但不跳转）
    const handleProgressInput = (value) => {
      isDragging.value = true
      sliderValue.value = value
    }
    
    // 处理进度条变化（拖拽结束时跳转）
    const handleProgressChange = (value) => {
      isDragging.value = false
      playerStore.seekToProgress(value)
    }
    
    // 格式化进度条tooltip
    const formatTooltip = (value) => {
      if (!playerStore.duration) return '00:00'
      const time = (value / 100) * playerStore.duration
      const mins = Math.floor(time / 60)
      const secs = Math.floor(time % 60)
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    }
    
    // 处理音量拖拽输入（实时响应）
    const handleVolumeInput = (value) => {
      // 如果当前是静音状态且音量不为0，则解除静音
      if (playerStore.muted && value > 0) {
        playerStore.toggleMute()
      }
    }
    
    // 处理音量变化（拖拽结束）
    const handleVolumeChange = (value) => {
      playerStore.setVolume(value / 100)
    }
    
    // 处理音量图标点击
    const handleToggleMute = () => {
      playerStore.toggleMute()
    }
    
    // 切换折叠状态
    const toggleCollapse = () => {
      isCollapsed.value = !isCollapsed.value
    }
    
    // 处理上一首点击
    const handlePrevious = () => {
      if (playerStore.hasPrevious && currentMusic.value) {
        playerStore.previous()
      }
    }
    
    // 处理下一首点击
    const handleNext = () => {
      if (playerStore.hasNext && currentMusic.value) {
        playerStore.next()
      }
    }
    
    // 检查收藏状态
    const checkFavoriteStatus = async () => {
      if (!currentMusic.value || !userStore.isLoggedIn) {
        isFavorited.value = false
        return
      }
      
      try {
        const response = await isFavorite(currentMusic.value.id)
        isFavorited.value = response?.data || false
      } catch (error) {
        console.error('检查收藏状态失败:', error)
        isFavorited.value = false
      }
    }
    
    // 切换收藏状态
    const toggleFavorite = async () => {
      if (!currentMusic.value) {
        return
      }
      
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      try {
        if (isFavorited.value) {
          await removeFavorite(currentMusic.value.id)
          isFavorited.value = false
          ElMessage.success('已取消收藏')
        } else {
          await addFavorite(currentMusic.value.id)
          isFavorited.value = true
          ElMessage.success('已添加收藏')
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        ElMessage.error('操作失败，请重试')
      }
    }
    
    // 记录播放历史
    const recordPlay = async (music) => {
      if (!music || !userStore.isLoggedIn) {
        return
      }
      
      try {
        await recordPlayHistory(music.id)
      } catch (error) {
        console.error('记录播放历史失败:', error)
      }
    }
    
    // 监听当前音乐变化
    watch(currentMusic, (newMusic) => {
      if (newMusic) {
        checkFavoriteStatus()
        recordPlay(newMusic)
      } else {
        isFavorited.value = false
      }
    }, { immediate: true })
    
    // 监听登录状态变化
    watch(() => userStore.isLoggedIn, () => {
      checkFavoriteStatus()
    })
    
    return {
      playerStore,
      showPlaylist,
      isCollapsed,
      sliderValue,
      volumeValue,
      currentMusic,
      playModeText,
      isFavorited,
      handleProgressInput,
      handleProgressChange,
      formatTooltip,
      handleVolumeInput,
      handleVolumeChange,
      handleToggleMute,
      toggleCollapse,
      handlePrevious,
      handleNext,
      toggleFavorite
    }
  }
}
</script>

<style scoped>
.music-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 72px;
  background: #fff;
  border-top: 1px solid #e4e7ed;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  z-index: 999;
  transition: transform 0.3s ease;
}

.music-player.collapsed {
  transform: translateY(calc(100% - 5px));
}

.player-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 音乐信息区域 */
.music-info {
  display: flex;
  align-items: center;
  width: 240px;
  cursor: pointer;
}

.music-cover {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
}

.cover-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.cover-img.rotating {
  animation: rotate 3s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 20px;
  border-radius: 50%;
}

.music-details {
  flex: 1;
  min-width: 0;
}

.music-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 2px;
}

.music-artist {
  font-size: 12px;
  color: #909399;
}

/* 播放控制区域 */
.player-controls {
  flex: 1;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.control-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 5px;
}

.control-buttons .el-button {
  font-size: 24px;
  padding: 8px;
}

.play-button {
  font-size: 28px !important;
  margin: 0 10px;
  padding: 10px !important;
}

.progress-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.progress-slider {
  flex: 1;
}

.progress-slider :deep(.el-slider__runway) {
  height: 4px;
  background-color: var(--el-border-color-light);
}

.progress-slider :deep(.el-slider__bar) {
  height: 4px;
  background-color: var(--el-color-primary);
}

.progress-slider :deep(.el-slider__button) {
  width: 12px;
  height: 12px;
  border: 2px solid var(--el-color-primary);
  background-color: #fff;
}

.progress-slider :deep(.el-slider__button):hover {
  transform: scale(1.2);
}

.progress-slider:hover :deep(.el-slider__button) {
  opacity: 1;
}

.progress-slider :deep(.el-slider__button) {
  opacity: 0;
  transition: opacity 0.2s, transform 0.2s;
}

.time-current,
.time-total {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  min-width: 40px;
  text-align: center;
  font-family: 'Courier New', monospace;
  font-weight: 500;
}

/* 播放选项区域 */
.player-options {
  display: flex;
  align-items: center;
  width: 240px;
  justify-content: flex-end;
}

.mode-button, .volume-button, .playlist-button, .favorite-button {
  font-size: 22px;
  padding: 8px;
  color: #333 !important;
}

.volume-button :deep(.el-icon) {
  color: #333 !important;
}

.favorite-button.favorited {
  color: #f7ba2a !important;
}

.favorite-button.favorited :deep(.el-icon) {
  color: #f7ba2a !important;
}

.volume-control {
  display: flex;
  align-items: center;
  margin: 0 18px;
  gap: 12px;
}

.volume-slider {
  width: 100px;
}

/* 播放列表 */
.playlist-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.playlist-title {
  font-size: 14px;
  font-weight: 500;
}

.playlist-actions {
  display: flex;
  align-items: center;
}

.playlist-items {
  list-style: none;
  padding: 0;
  margin: 0;
}

.playlist-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.playlist-item:hover {
  background-color: #f5f7fa;
}

.playlist-item.active {
  background-color: #ecf5ff;
}

.item-info {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.item-index {
  width: 20px;
  text-align: center;
  font-size: 12px;
  color: #909399;
  margin-right: 10px;
}

.item-cover {
  width: 30px;
  height: 30px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 10px;
  flex-shrink: 0;
}

.item-cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 14px;
}

.item-details {
  flex: 1;
  min-width: 0;
}

.item-title {
  font-size: 13px;
  color: #303133;
  margin-bottom: 2px;
}

.item-artist {
  font-size: 12px;
  color: #909399;
}

.item-actions {
  display: flex;
  align-items: center;
}

/* 折叠按钮 */
.collapse-button {
  position: absolute;
  top: -15px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 15px;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-bottom: none;
  border-radius: 4px 4px 0 0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1;
}

.collapse-button:hover {
  background-color: #f5f7fa;
}

/* 禁用状态 */
.disabled {
  color: #c0c4cc !important;
  cursor: not-allowed !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-player {
    height: 85px;
    padding-bottom: 8px;
  }
  
  .player-container {
    padding: 8px 12px;
    flex-direction: row;
    gap: 10px;
    align-items: center;
    justify-content: space-between;
  }
  
  .music-info {
    width: 140px;
    flex-shrink: 0;
  }
  
  .player-controls {
    flex: 1;
    max-width: 300px;
    min-width: 200px;
  }
  
  .control-buttons {
    margin-bottom: 4px;
  }
  
  .control-buttons .el-button {
    font-size: 18px;
    padding: 6px;
  }
  
  .play-button {
    font-size: 24px !important;
    margin: 0 10px;
    padding: 6px;
  }
  
  .player-options {
    width: 140px;
    gap: 6px;
    flex-shrink: 0;
  }
  
  .mode-button, .volume-button, .playlist-button, .favorite-button {
    font-size: 16px;
    padding: 6px;
  }
  
  .volume-control {
    margin: 0 6px;
    gap: 5px;
    position: relative;
  }
  
  .volume-slider {
    width: 120px;
    position: absolute;
    bottom: 100%;
    left: 50%;
    transform: translateX(-50%) rotate(-90deg);
    transform-origin: center;
    opacity: 0;
    visibility: hidden;
    transition: all 0.3s ease;
    z-index: 1000;
    background: rgba(255, 255, 255, 0.95);
    padding: 15px 20px;
    border-radius: 6px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  }
  
  .volume-control:hover .volume-slider {
    opacity: 1;
    visibility: visible;
  }
}

@media (max-width: 576px) {
  .music-player {
    height: 95px;
    padding-bottom: 12px;
  }
  
  .player-container {
    padding: 10px 8px;
    gap: 8px;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
  
  .music-info {
    width: 100px;
    flex-shrink: 0;
  }
  
  .music-cover {
    width: 32px;
    height: 32px;
    margin-right: 6px;
  }
  
  .music-details {
    display: block;
  }
  
  .music-title {
    font-size: 11px;
    line-height: 1.2;
  }
  
  .music-artist {
    font-size: 9px;
    line-height: 1.2;
  }
  
  .player-controls {
    flex: 1;
    max-width: 200px;
    min-width: 150px;
  }
  
  .control-buttons .el-button {
    font-size: 16px;
    padding: 4px;
  }
  
  .play-button {
    font-size: 22px !important;
    margin: 0 8px;
    padding: 4px;
  }
  
  .player-options {
    width: 100px;
    gap: 4px;
    flex-shrink: 0;
    justify-content: flex-end;
  }
  
  .mode-button, .volume-button, .playlist-button, .favorite-button {
    font-size: 14px;
    padding: 4px;
  }
  
  .volume-control {
    margin: 0 3px;
    gap: 3px;
    position: relative;
  }
  
  .volume-slider {
    width: 100px;
    position: absolute;
    bottom: 100%;
    left: 50%;
    transform: translateX(-50%) rotate(-90deg);
    transform-origin: center;
    opacity: 0;
    visibility: hidden;
    transition: all 0.3s ease;
    z-index: 1000;
    background: rgba(255, 255, 255, 0.95);
    padding: 12px 18px;
    border-radius: 5px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.18);
  }
  
  .volume-control:hover .volume-slider {
    opacity: 1;
    visibility: visible;
  }
  
  .time-current, .time-total {
    font-size: 9px;
    min-width: 25px;
  }
  
  .progress-container {
    gap: 6px;
  }
  
  .progress-slider :deep(.el-slider__runway) {
    height: 3px;
  }
  
  .progress-slider :deep(.el-slider__bar) {
    height: 3px;
  }
  
  .progress-slider :deep(.el-slider__button) {
    width: 10px;
    height: 10px;
  }
}
</style>