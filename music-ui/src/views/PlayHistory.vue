<template>
  <!-- eslint-disable vue/multi-word-component-names -->
  <div class="play-history">
    <div class="page-header">
      <h2>播放历史</h2>
      <div class="header-actions">
        <el-button
          v-if="historyList.length > 0"
          type="danger"
          size="small"
          @click="handleClearHistory"
        >
          <el-icon><Delete /></el-icon>
          清空历史
        </el-button>
      </div>
    </div>

    <div class="history-content" v-loading="loading">
      <!-- 空状态 -->
      <div v-if="!loading && historyList.length === 0" class="empty-state">
        <el-empty description="暂无播放历史">
          <el-button type="primary" @click="$router.push('/music')">
            去发现音乐
          </el-button>
        </el-empty>
      </div>

      <!-- 历史列表 -->
      <div v-else class="history-list">
        <div
          v-for="(item, index) in historyList"
          :key="item.id || index"
          class="history-item"
          @click="playMusic(item)"
        >
          <div class="item-index">{{ index + 1 }}</div>
          
          <div class="item-cover">
            <el-image
              :src="item.coverUrl || '/img/default-cover.jpg'"
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
              <el-button type="primary" circle size="small">
                <el-icon><VideoPlay /></el-icon>
              </el-button>
            </div>
          </div>
          
          <div class="item-info">
            <div class="item-title text-ellipsis">{{ item.title }}</div>
            <div class="item-artist text-ellipsis">{{ item.artist }}</div>
            <div class="item-meta">
              <span class="play-time">{{ formatPlayTime(item.playedAt) }}</span>
              <span v-if="item.playDuration" class="duration">
                播放时长: {{ formatDuration(item.playDuration) }}
              </span>
            </div>
          </div>
          
          <div class="item-actions">
            <el-button
              link
              size="small"
              @click.stop="addToFavorite(item)"
              title="收藏"
            >
              <el-icon><Star /></el-icon>
            </el-button>
            
            <el-button
              link
              size="small"
              @click.stop="addToPlaylist(item)"
              title="添加到播放列表"
            >
              <el-icon><Plus /></el-icon>
            </el-button>
            
            <el-button
              link
              size="small"
              type="danger"
              @click.stop="removeFromHistory(index)"
              title="从历史中移除"
            >
              <el-icon><Close /></el-icon>
            </el-button>
          </div>
        </div>
      </div>

      <!-- 加载更多 -->
      <div v-if="hasMore && historyList.length > 0" class="load-more">
        <el-button
          @click="loadMore"
          :loading="loadingMore"
          type="primary"
          plain
        >
          加载更多
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePlayerStore } from '@/stores/player'
import { useUserStore } from '@/stores/user'
import { getPlayHistory, clearPlayHistory } from '@/api/history'
import { getMusicCoverUrl } from '@/api/music'
import {
  Delete,
  Mic,
  VideoPlay,
  Star,
  Plus,
  Close
} from '@element-plus/icons-vue'

export default {
  name: 'PlayHistoryPage',
  components: {
    Delete,
    Mic,
    VideoPlay,
    Star,
    Plus,
    Close
  },
  setup() {
    const playerStore = usePlayerStore()
    const userStore = useUserStore()
    
    const historyList = ref([])
    const loading = ref(false)
    const loadingMore = ref(false)
    const hasMore = ref(true)
    const currentPage = ref(1)
    const pageSize = 20
    
    // 获取播放历史
    const fetchPlayHistory = async (page = 1, append = false) => {
      try {
        if (page === 1) {
          loading.value = true
        } else {
          loadingMore.value = true
        }
        
        const response = await getPlayHistory({
          page,
          limit: pageSize
        })
        
        console.log('播放历史API响应:', response)
        
        let musicList = []
        if (response) {
          if (response.records) {
            musicList = response.records
          } else if (response.list) {
            musicList = response.list
          } else if (response.data) {
            if (Array.isArray(response.data)) {
              musicList = response.data
            } else if (response.data.records) {
              musicList = response.data.records
            } else if (response.data.list) {
              musicList = response.data.list
            }
          } else if (Array.isArray(response)) {
            musicList = response
          }
        }
        
        // 为每个音乐项添加封面URL
        musicList.forEach(music => {
          if (!music.coverUrl && music.id) {
            music.coverUrl = getMusicCoverUrl(music.id)
          }
        })
        
        if (append) {
          historyList.value.push(...musicList)
        } else {
          historyList.value = musicList
        }
        
        // 检查是否还有更多数据
        hasMore.value = musicList.length === pageSize
        currentPage.value = page
        
      } catch (error) {
        console.error('获取播放历史失败:', error)
        ElMessage.error('获取播放历史失败')
      } finally {
        loading.value = false
        loadingMore.value = false
      }
    }
    
    // 加载更多
    const loadMore = () => {
      if (!loadingMore.value && hasMore.value) {
        fetchPlayHistory(currentPage.value + 1, true)
      }
    }
    
    // 播放音乐
    const playMusic = (music) => {
      playerStore.play(music)
    }
    
    // 添加到收藏
    const addToFavorite = (music) => {
      // TODO: 实现收藏功能
      console.log('添加到收藏:', music.title)
      ElMessage.success('收藏功能开发中...')
    }
    
    // 添加到播放列表
    const addToPlaylist = (music) => {
      playerStore.addToPlaylist(music)
      ElMessage.success('已添加到播放列表')
    }
    
    // 从历史中移除
    const removeFromHistory = (index) => {
      historyList.value.splice(index, 1)
      ElMessage.success('已从历史中移除')
    }
    
    // 清空播放历史
    const handleClearHistory = async () => {
      try {
        await ElMessageBox.confirm(
          '确定要清空所有播放历史吗？此操作不可恢复。',
          '确认清空',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        await clearPlayHistory()
        historyList.value = []
        ElMessage.success('播放历史已清空')
        
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空播放历史失败:', error)
          ElMessage.error('清空播放历史失败')
        }
      }
    }
    
    // 格式化播放时间
    const formatPlayTime = (timestamp) => {
      if (!timestamp) return ''
      
      const now = new Date()
      const playTime = new Date(timestamp)
      const diff = now - playTime
      
      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (minutes < 1) {
        return '刚刚'
      } else if (minutes < 60) {
        return `${minutes}分钟前`
      } else if (hours < 24) {
        return `${hours}小时前`
      } else if (days < 7) {
        return `${days}天前`
      } else {
        return playTime.toLocaleDateString()
      }
    }
    
    // 格式化播放时长
    const formatDuration = (seconds) => {
      if (!seconds) return '0秒'
      
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      
      if (minutes > 0) {
        return `${minutes}分${remainingSeconds}秒`
      } else {
        return `${remainingSeconds}秒`
      }
    }
    
    // 初始化
    onMounted(() => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      fetchPlayHistory()
    })
    
    return {
      historyList,
      loading,
      loadingMore,
      hasMore,
      fetchPlayHistory,
      loadMore,
      playMusic,
      addToFavorite,
      addToPlaylist,
      removeFromHistory,
      handleClearHistory,
      formatPlayTime,
      formatDuration
    }
  }
}
</script>

<style scoped>
.play-history {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.history-content {
  min-height: 400px;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.history-list {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.history-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.history-item:last-child {
  border-bottom: none;
}

.history-item:hover {
  background-color: #f5f7fa;
}

.item-index {
  width: 40px;
  text-align: center;
  font-size: 14px;
  color: #909399;
  margin-right: 15px;
  flex-shrink: 0;
}

.item-cover {
  position: relative;
  width: 60px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.cover-img {
  width: 100%;
  height: 100%;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 24px;
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

.history-item:hover .play-overlay {
  opacity: 1;
}

.item-info {
  flex: 1;
  min-width: 0;
  margin-right: 15px;
}

.item-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.item-artist {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.item-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 12px;
  color: #909399;
}

.play-time {
  color: #409eff;
}

.item-actions {
  display: flex;
  align-items: center;
  gap: 5px;
  opacity: 0;
  transition: opacity 0.2s;
}

.history-item:hover .item-actions {
  opacity: 1;
}

.load-more {
  text-align: center;
  padding: 30px;
}

.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .play-history {
    padding: 15px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .history-item {
    padding: 12px 15px;
  }
  
  .item-index {
    display: none;
  }
  
  .item-cover {
    width: 50px;
    height: 50px;
  }
  
  .item-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
}

@media (max-width: 576px) {
  .item-actions {
    opacity: 1;
  }
  
  .item-actions .el-button {
    padding: 5px;
  }
}
</style>