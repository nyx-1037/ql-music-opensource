<template>
  <div class="music-page">
    <!-- 搜索和筛选区域 -->
    <div class="search-section">
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索音乐、艺术家、专辑..."
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button @click="handleSearch">
              搜索
            </el-button>
          </template>
        </el-input>
      </div>
      
      <div class="filter-bar">
        <div class="filter-group">
          <span class="filter-label">排序:</span>
          <el-select v-model="sortBy" @change="handleFilterChange" placeholder="选择排序" size="default" style="width: 120px;">
            <el-option label="最新" value="latest" />
            <el-option label="最热" value="popular" />
            <el-option label="播放量" value="playCount" />
            <el-option label="收藏量" value="favoriteCount" />
            <el-option label="标题" value="title" />
            <el-option label="艺术家" value="artist" />
          </el-select>
        </div>
        
        <div class="filter-group">
          <span class="filter-label">流派:</span>
          <el-select v-model="selectedGenre" @change="handleFilterChange" placeholder="选择流派" clearable size="default" style="width: 120px;">
            <el-option
              v-for="genre in genres"
              :key="genre.value"
              :label="genre.label"
              :value="genre.value"
            />
          </el-select>
        </div>
        
        <div class="filter-group">
          <span class="filter-label">时长:</span>
          <el-select v-model="durationFilter" @change="handleFilterChange" placeholder="选择时长" clearable size="default" style="width: 120px;">
            <el-option label="短 (< 3分钟)" value="short" />
            <el-option label="中 (3-6分钟)" value="medium" />
            <el-option label="长 (> 6分钟)" value="long" />
          </el-select>
        </div>
        
        <div class="view-toggle">
          <el-button-group>
            <el-button
              :type="viewMode === 'grid' ? 'primary' : 'default'"
              @click="viewMode = 'grid'"
            >
              <el-icon><Grid /></el-icon>
            </el-button>
            <el-button
              :type="viewMode === 'list' ? 'primary' : 'default'"
              @click="viewMode = 'list'"
            >
              <el-icon><List /></el-icon>
            </el-button>
          </el-button-group>
        </div>
      </div>
    </div>

    <!-- 音乐列表区域 -->
    <div class="music-section" v-loading="loading">
      <!-- 网格视图 -->
      <div v-if="viewMode === 'grid'" class="music-grid">
        <template v-for="music in musicList" :key="music?.id || Math.random()">
          <div
            v-if="music"
            class="music-card"
          >
          <div class="music-cover">
            <el-image
              :src="music.coverUrl || '/img/default-cover.jpg'"
              fit="cover"
              class="cover-img"
              @click="$router.push(`/music/${music.id}`)"
            >
              <template #error>
                <div class="cover-placeholder">
                  <el-icon><Mic /></el-icon>
                </div>
              </template>
            </el-image>
            
            <div class="play-overlay">
              <el-button type="primary" circle size="large" @click="music && playMusic(music)">
                <el-icon><VideoPlay /></el-icon>
              </el-button>
            </div>
          </div>
          
          <div class="music-info">
            <div class="music-title text-ellipsis" @click="music?.id && $router.push(`/music/${music.id}`)">
              {{ music?.title || '未知标题' }}
            </div>
            <div class="music-artist text-ellipsis">
              {{ music?.artist || '未知艺术家' }}
            </div>
            <div class="music-stats">
              <span class="play-count">
                <el-icon><Headset /></el-icon>
                {{ formatPlayCount(music?.playCount || 0) }}
              </span>
              <span class="duration">{{ formatDuration(music?.duration || 0) }}</span>
            </div>
          </div>
          
          <div class="music-actions">
            <el-button link size="small" @click="music && addToPlaylist(music)">
              <el-icon><Plus /></el-icon>
            </el-button>
            
            <el-dropdown @command="handleMusicAction">
              <el-button link size="small">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="{ action: 'download', music }">
                    <el-icon><Download /></el-icon>
                    下载
                  </el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'share', music }">
                    <el-icon><Share /></el-icon>
                    分享
                  </el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'report', music }">
                    <el-icon><Warning /></el-icon>
                    举报
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          </div>
        </template>
      </div>
      
      <!-- 列表视图 -->
      <div v-else class="music-list">
        <el-table
          :data="musicList"
          style="width: 100%"
          @row-dblclick="playMusic"
        >
          <el-table-column width="60">
            <template #default="{ $index }">
              <span class="row-index">{{ $index + 1 + (currentPage - 1) * pageSize }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="音乐" min-width="300">
            <template #default="{ row }">
              <div class="table-music-info">
                <div class="table-music-cover">
                  <el-image
                    :src="row.coverUrl || '/img/default-cover.jpg'"
                    fit="cover"
                  >
                    <template #error>
                      <div class="table-cover-placeholder">
                        <el-icon><Mic /></el-icon>
                      </div>
                    </template>
                  </el-image>
                </div>
                
                <div class="table-music-details">
                  <div class="table-music-title" @click="row?.id && $router.push(`/music/${row.id}`)">
                    {{ row?.title || '未知标题' }}
                  </div>
                  <div class="table-music-artist">{{ row?.artist || '未知艺术家' }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="album" label="专辑" width="200" show-overflow-tooltip />
          
          <el-table-column label="时长" width="100">
            <template #default="{ row }">
              {{ formatDuration(row?.duration || 0) }}
            </template>
          </el-table-column>
          
          <el-table-column label="播放量" width="120">
            <template #default="{ row }">
              <span class="play-count">
                <el-icon><Headset /></el-icon>
                {{ formatPlayCount(row?.playCount || 0) }}
              </span>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button link size="small" @click="row && playMusic(row)">
                <el-icon><VideoPlay /></el-icon>
              </el-button>
              
              <el-button link size="small" @click="row && addToPlaylist(row)">
                <el-icon><Plus /></el-icon>
              </el-button>
              
              <el-dropdown @command="handleMusicAction">
                <el-button link size="small">
                  <el-icon><MoreFilled /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :command="{ action: 'download', music: row }">
                      <el-icon><Download /></el-icon>
                      下载
                    </el-dropdown-item>
                    <el-dropdown-item :command="{ action: 'share', music: row }">
                      <el-icon><Share /></el-icon>
                      分享
                    </el-dropdown-item>
                    <el-dropdown-item :command="{ action: 'report', music: row }">
                      <el-icon><Warning /></el-icon>
                      举报
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 空状态 -->
      <el-empty
        v-if="!loading && musicList.length === 0"
        description="暂无音乐"
      />
    </div>

    <!-- 加载更多按钮 -->
    <div class="load-more-section" v-if="!loading && musicList.length > 0 && hasMore">
      <el-button
        type="primary"
        size="large"
        @click="loadMore"
        :loading="loadingMore"
        class="load-more-btn"
      >
        <span v-if="!loadingMore">加载更多</span>
        <span v-else>加载中...</span>
      </el-button>
    </div>
    
    <!-- 没有更多数据提示 -->
    <div class="no-more-data" v-if="!loading && musicList.length > 0 && !hasMore">
      <el-divider>
        <span class="no-more-text">没有更多数据了</span>
      </el-divider>
    </div>

    <!-- 右下角悬浮按钮 -->
    <div class="floating-actions">
      <el-button
        type="primary"
        circle
        size="large"
        class="action-btn refresh-btn"
        @click="fetchMusic"
        title="刷新"
      >
        <el-icon><Refresh /></el-icon>
      </el-button>
      
      <el-button
        type="info"
        circle
        size="large"
        class="action-btn back-to-top-btn"
        @click="scrollToTop"
        title="返回顶部"
      >
        <el-icon><Top /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore } from '@/stores/player'
import { getMusicList, searchMusic, getMusicCoverUrl, getMusicGenres } from '@/api/music'
import { ElMessage } from 'element-plus'
import {
  Search,
  Grid,
  List,
  Mic,
  VideoPlay,
  Headset,
  Plus,
  MoreFilled,
  Download,
  Share,
  Warning,
  Refresh,
  Top
} from '@element-plus/icons-vue'

export default {
  name: 'MusicList',
  components: {
    Search,
    Grid,
    List,
    Mic,
    VideoPlay,
    Headset,
    Plus,
    MoreFilled,
    Download,
    Share,
    Warning,
    Refresh,
    Top
  },
  setup() {
    const route = useRoute()
    const playerStore = usePlayerStore()
    
    // 数据
    const musicList = ref([])
    const genres = ref([])
    const loading = ref(false)
    const loadingMore = ref(false)
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(20)
    const hasMore = ref(true)
    
    // 搜索和筛选
    const searchQuery = ref('')
    const sortBy = ref('latest')
    const selectedGenre = ref('')
    const durationFilter = ref('')
    const viewMode = ref('grid')
    
    // 获取音乐列表
    const fetchMusic = async (isLoadMore = false) => {
      try {
        if (isLoadMore) {
          loadingMore.value = true
        } else {
          loading.value = true
          // 重新搜索时重置数据
          musicList.value = []
          currentPage.value = 1
          hasMore.value = true
        }
        
        const params = {
          page: currentPage.value,
          limit: pageSize.value,
          sort: sortBy.value,
          genre: selectedGenre.value,
          duration: durationFilter.value,
          search: searchQuery.value
        }
        
        console.log('请求参数:', params)
        
        let response
        if (searchQuery.value && searchQuery.value.trim() !== '') {
          // 只有在有搜索关键词时才调用搜索接口
          const searchParams = {
            keyword: searchQuery.value.trim(),
            current: currentPage.value,
            size: pageSize.value
          }
          console.log('Music.vue - 准备调用搜索接口')
          console.log('Music.vue - searchQuery.value:', searchQuery.value)
          console.log('Music.vue - 构建的searchParams:', searchParams)
          console.log('Music.vue - 即将调用searchMusic函数')
          response = await searchMusic(searchParams)
          console.log('Music.vue - searchMusic返回结果:', response)
        } else {
          console.log('Music.vue - 调用普通音乐列表接口')
          response = await getMusicList(params)
        }
        
        console.log('API响应:', response)
        
        // 处理响应数据
        let dataList = []
        let totalCount = 0
        
        if (response) {
          // 尝试不同的数据结构
          if (response.records) {
            dataList = response.records
            totalCount = response.total || 0
          } else if (response.list) {
            dataList = response.list
            totalCount = response.count || response.total || 0
          } else if (response.data) {
            if (Array.isArray(response.data)) {
              dataList = response.data
              totalCount = response.total || response.count || response.data.length
            } else if (response.data.records) {
              dataList = response.data.records
              totalCount = response.data.total || 0
            } else if (response.data.list) {
              dataList = response.data.list
              totalCount = response.data.count || response.data.total || 0
            }
          } else if (Array.isArray(response)) {
            dataList = response
            totalCount = response.length
          }
        }
        
        console.log('解析后的数据列表:', dataList)
        console.log('总数:', totalCount)
        
        // 过滤掉null或undefined的数据项
        const filteredMusic = Array.isArray(dataList) ? dataList.filter(music => music && music.id) : []
        // 为每个音乐项添加封面URL
        filteredMusic.forEach(music => {
          if (!music.coverUrl) {
            music.coverUrl = getMusicCoverUrl(music.id)
          }
        })
        
        if (isLoadMore) {
          // 加载更多时追加数据
          musicList.value = [...musicList.value, ...filteredMusic]
        } else {
          // 首次加载或重新搜索时替换数据
          musicList.value = filteredMusic
        }
        
        total.value = totalCount
        
        // 检查是否还有更多数据
        hasMore.value = musicList.value.length < totalCount
        
        console.log('最终音乐列表:', musicList.value)
        console.log('是否还有更多:', hasMore.value)
        return Promise.resolve()
      } catch (error) {
        console.error('获取音乐列表失败:', error)
        ElMessage.error('获取音乐列表失败')
        return Promise.reject(error)
      } finally {
        loading.value = false
        loadingMore.value = false
      }
    }
    
    // 获取流派列表
    const fetchGenres = async () => {
      try {
        const response = await getMusicGenres()
        console.log('获取流派列表响应:', response)
        
        let genreList = []
        if (response && response.data) {
          genreList = response.data
        } else if (Array.isArray(response)) {
          genreList = response
        }
        
        // 将流派字符串转换为选项格式
        genres.value = genreList.map(genre => ({
          value: genre,
          label: genre
        }))
        
        console.log('处理后的流派列表:', genres.value)
      } catch (error) {
        console.error('获取音乐类型失败:', error)
        // 如果API调用失败，使用默认流派列表
        genres.value = [
          { value: '流行', label: '流行' },
          { value: '摇滚', label: '摇滚' },
          { value: '民谣', label: '民谣' },
          { value: '电子', label: '电子' },
          { value: '古典', label: '古典' },
          { value: '爵士', label: '爵士' }
        ]
      }
    }
    
    // 搜索音乐
    const handleSearch = () => {
      fetchMusic(false) // 重新搜索
    }
    
    // 处理筛选变化
    const handleFilterChange = () => {
      // 重置分页和状态
      currentPage.value = 1
      hasMore.value = true
      isLoadingTriggered.value = false
      // 重新获取数据
      fetchMusic(false)
    }
    
    // 加载更多
    const loadMore = () => {
      return new Promise((resolve, reject) => {
        if (hasMore.value && !loadingMore.value) {
          currentPage.value++
          fetchMusic(true).then(() => {
            resolve()
          }).catch((error) => {
            reject(error)
          })
        } else {
          resolve()
        }
      })
    }
    
    // 防抖函数
    const debounce = (func, wait) => {
      let timeout
      return function executedFunction(...args) {
        const later = () => {
          clearTimeout(timeout)
          func(...args)
        }
        clearTimeout(timeout)
        timeout = setTimeout(later, wait)
      }
    }
    
    // 检测是否为移动端
    const isMobile = () => {
      return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) || window.innerWidth <= 768
    }
    
    // 滚动位置记录
    const lastScrollPosition = ref(0)
    const isLoadingTriggered = ref(false)
    
    // 滚动监听
    const handleScroll = () => {
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop
      const windowHeight = window.innerHeight
      const documentHeight = document.documentElement.scrollHeight
      
      // 移动端使用更大的触发距离，避免频繁触发
      const triggerDistance = isMobile() ? 200 : 100
      
      // 检查滚动方向，只在向下滚动时触发
      const isScrollingDown = scrollTop > lastScrollPosition.value
      lastScrollPosition.value = scrollTop
      
      // 当滚动到距离底部指定距离时自动加载更多
      if (isScrollingDown && scrollTop + windowHeight >= documentHeight - triggerDistance) {
        if (hasMore.value && !loading.value && !loadingMore.value && !isLoadingTriggered.value) {
          isLoadingTriggered.value = true
          
          // 记录当前滚动位置
          const currentScrollPosition = scrollTop
          
          loadMore().then(() => {
            // 加载完成后，在移动端保持滚动位置
            if (isMobile()) {
              // 使用 nextTick 确保 DOM 更新完成
              setTimeout(() => {
                // 计算新内容的高度差，调整滚动位置
                const newDocumentHeight = document.documentElement.scrollHeight
                const heightDiff = newDocumentHeight - documentHeight
                
                // 如果有新内容加载，保持用户当前的视觉位置
                if (heightDiff > 0) {
                  window.scrollTo({
                    top: currentScrollPosition,
                    behavior: 'auto' // 使用 auto 避免动画效果
                  })
                }
                
                // 重置触发标志
                setTimeout(() => {
                  isLoadingTriggered.value = false
                }, 500)
              }, 100)
            } else {
              // 桌面端延迟重置触发标志
              setTimeout(() => {
                isLoadingTriggered.value = false
              }, 300)
            }
          }).catch(() => {
            // 加载失败时也要重置标志
            isLoadingTriggered.value = false
          })
        }
      }
    }
    
    // 使用防抖的滚动监听
    const debouncedHandleScroll = debounce(handleScroll, isMobile() ? 150 : 100)
    
    // 播放音乐
    const playMusic = (music) => {
      playerStore.play(music)
    }
    

    
    // 添加到播放列表
    const addToPlaylist = (music) => {
      playerStore.addToPlaylist(music)
      ElMessage.success('已添加到播放列表')
    }
    
    // 处理音乐操作
    const handleMusicAction = ({ action }) => {
      switch (action) {
        case 'download':
          // TODO: 实现下载功能
          ElMessage.info('下载功能开发中')
          break
        case 'share':
          // TODO: 实现分享功能
          ElMessage.info('分享功能开发中')
          break
        case 'report':
          // TODO: 实现举报功能
          ElMessage.info('举报功能开发中')
          break
      }
    }
    
    // 格式化播放次数
    const formatPlayCount = (count) => {
      if (!count && count !== 0) {
        return '0'
      }
      if (count >= 10000) {
        return Math.floor(count / 10000) + '万'
      } else if (count >= 1000) {
        return Math.floor(count / 1000) + 'k'
      }
      return count.toString()
    }
    
    // 格式化时长
    const formatDuration = (seconds) => {
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
    }
    

    
    // 返回顶部
    const scrollToTop = () => {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
    
    // 监听路由参数变化
    watch(() => route.query, (newQuery) => {
      if (newQuery.sort) {
        sortBy.value = newQuery.sort
      }
      if (newQuery.genre) {
        selectedGenre.value = newQuery.genre
      }
      if (newQuery.search) {
        searchQuery.value = newQuery.search
      } else {
        searchQuery.value = ''
      }
      fetchMusic()
    }, { immediate: true })
    
    // 初始化
    onMounted(() => {
      fetchGenres()
      if (!route.query.sort && !route.query.genre && !route.query.search) {
        fetchMusic()
      }
      // 添加滚动监听
      window.addEventListener('scroll', debouncedHandleScroll)
    })
    
    // 清理
    onUnmounted(() => {
      // 移除滚动监听
      window.removeEventListener('scroll', debouncedHandleScroll)
    })
    
    return {
      musicList,
      genres,
      loading,
      loadingMore,
      total,
      currentPage,
      pageSize,
      hasMore,
      searchQuery,
      sortBy,
      selectedGenre,
      durationFilter,
      viewMode,
      fetchMusic,
      handleSearch,
      handleFilterChange,
      loadMore,
      playMusic,
      addToPlaylist,
      handleMusicAction,
      formatPlayCount,
      formatDuration,
      scrollToTop
    }
  }
}
</script>

<style scoped>
.music-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 搜索区域 */
.search-section {
  margin-bottom: 30px;
}

.search-bar {
  margin-bottom: 20px;
}

.filter-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 15px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.el-select .el-input__inner {
  color: #333 !important;
  font-weight: normal;
}

.el-select-dropdown__item {
  color: #333;
}

.filter-label {
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
}

.view-toggle {
  margin-left: auto;
}

/* 音乐网格 */
.music-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.music-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.music-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.music-cover {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.cover-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
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

.music-card:hover .play-overlay {
  opacity: 1;
}

.music-info {
  padding: 15px;
}

.music-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
  cursor: pointer;
}

.music-title:hover {
  color: #409eff;
}

.music-artist {
  font-size: 12px;
  color: #909399;
  margin-bottom: 10px;
}

.music-stats {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  color: #c0c4cc;
}

.play-count {
  display: flex;
  align-items: center;
}

.play-count .el-icon {
  margin-right: 3px;
}

.music-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 15px;
  border-top: 1px solid #f0f0f0;
  opacity: 0;
  transition: opacity 0.2s;
}

.music-card:hover .music-actions {
  opacity: 1;
}

/* 音乐列表 */
.music-list {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.row-index {
  color: #909399;
  font-size: 14px;
}

.table-music-info {
  display: flex;
  align-items: center;
}

.table-music-cover {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 10px;
  flex-shrink: 0;
}

.table-cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 16px;
}

.table-music-details {
  flex: 1;
  min-width: 0;
}

.table-music-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 2px;
  cursor: pointer;
}

.table-music-title:hover {
  color: #409eff;
}

.table-music-artist {
  font-size: 12px;
  color: #909399;
}

/* 加载更多按钮样式 */
.load-more-section {
  text-align: center;
  padding: 30px 20px;
  margin-top: 20px;
}

.load-more-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 25px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.load-more-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.load-more-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.no-more-data {
  color: #999;
  font-size: 14px;
  padding: 20px;
}

/* 右下角悬浮按钮样式 */
.floating-actions {
  position: fixed;
  bottom: 70px;
  right: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.action-btn {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.refresh-btn {
  background: #409eff;
  color: #fff;
}

.refresh-btn:hover {
  background: #337ecc;
}

.back-to-top-btn {
  background: #909399;
  color: #fff;
}

.back-to-top-btn:hover {
  background: #73767a;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-page {
    padding: 10px;
  }
  
  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .view-toggle {
    margin-left: 0;
    align-self: center;
  }
  
  .music-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
}

@media (max-width: 576px) {
  .music-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .filter-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-label {
    margin-bottom: 5px;
  }
}
</style>