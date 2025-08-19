<template>
  <div class="home">
    <!-- 轮播图区域 -->
    <div class="banner-section">
      <el-carousel height="300px" indicator-position="outside">
        <el-carousel-item v-for="(item, index) in banners" :key="index">
          <div class="banner-item" :style="{ backgroundImage: `url(${item.image})` }">
            <div class="banner-content">
              <h2>{{ item.title }}</h2>
              <p>{{ item.description }}</p>
              <el-button type="primary" size="large" @click="handleBannerClick(item)">
                {{ item.buttonText }}
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 推荐音乐区域 -->
    <div class="section">
      <div class="section-header">
        <h3>推荐音乐</h3>
        <el-button link @click="$router.push('/music')">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="music-grid" v-loading="loading.recommended">
        <template v-for="music in recommendedMusic" :key="music?.id || Math.random()">
          <div
            v-if="music"
            class="music-card"
            @click="playMusic(music)"
          >
            <div class="music-cover">
              <el-image
                :src="music.coverUrl || '/img/default-cover.jpg'"
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
                <el-button type="primary" circle size="large">
                  <el-icon><VideoPlay /></el-icon>
                </el-button>
              </div>
            </div>
            
            <div class="music-info">
              <div class="music-title text-ellipsis">{{ music.title }}</div>
              <div class="music-artist text-ellipsis">{{ music.artist }}</div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <!-- 热门音乐区域 -->
    <div class="section">
      <div class="section-header">
        <h3>热门音乐</h3>
        <el-button link @click="$router.push('/music?sort=popular')">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="music-list" v-loading="loading.popular">
        <template v-for="(music, index) in popularMusic" :key="music?.id || Math.random()">
          <div
            v-if="music"
            class="music-item"
            @click="playMusic(music)"
          >
            <div class="item-index">{{ index + 1 }}</div>
            
            <div class="item-cover">
              <el-image
                :src="music.coverUrl || '/img/default-cover.jpg'"
                fit="cover"
              >
                <template #error>
                  <div class="item-cover-placeholder">
                    <el-icon><Mic /></el-icon>
                  </div>
                </template>
              </el-image>
            </div>
            
            <div class="item-info">
              <div class="item-title text-ellipsis">{{ music.title }}</div>
              <div class="item-artist text-ellipsis">{{ music.artist }}</div>
            </div>
            
            <div class="item-stats">
              <span class="play-count">
                <el-icon><Headset /></el-icon>
                {{ formatPlayCount(music.playCount) }}
              </span>
            </div>
            
            <div class="item-actions">
              <el-button link size="small" @click.stop="addToFavorite(music)">
                <el-icon><Star /></el-icon>
              </el-button>
              
              <el-button link size="small" @click.stop="addToPlaylist(music)">
                <el-icon><Plus /></el-icon>
              </el-button>
            </div>
          </div>
        </template>
      </div>
    </div>

    <!-- 最新音乐区域 -->
    <div class="section">
      <div class="section-header">
        <h3>最新音乐</h3>
        <el-button link @click="$router.push('/music?sort=latest')">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="music-grid" v-loading="loading.latest">
        <template v-for="music in latestMusic" :key="music?.id || Math.random()">
          <div
            v-if="music"
            class="music-card"
            @click="playMusic(music)"
          >
            <div class="music-cover">
              <el-image
                :src="music.coverUrl || '/img/default-cover.jpg'"
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
                <el-button type="primary" circle size="large">
                  <el-icon><VideoPlay /></el-icon>
                </el-button>
              </div>
            </div>
            
            <div class="music-info">
              <div class="music-title text-ellipsis">{{ music.title }}</div>
              <div class="music-artist text-ellipsis">{{ music.artist }}</div>
              <div class="music-date">{{ formatDate(music.createdAt) }}</div>
            </div>
          </div>
        </template>
      </div>
    </div>


  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePlayerStore } from '@/stores/player'
import { getHotMusic, getLatestMusic, getRecommendMusic, getMusicCoverUrl } from '@/api/music'
import bannerApi from '@/api/banner'
import { ElMessage } from 'element-plus'
import {
  ArrowRight,
  Mic,
  VideoPlay,
  Headset,
  Star,
  Plus
} from '@element-plus/icons-vue'

export default {
  name: 'HomePage',
  components: {
    ArrowRight,
    Mic,
    VideoPlay,
    Headset,
    Star,
    Plus
  },
  setup() {
    const router = useRouter()
    const playerStore = usePlayerStore()
    
    // 数据
    const banners = ref([])
    
    const recommendedMusic = ref([])
    const popularMusic = ref([])
    const latestMusic = ref([])
    
    // 加载状态
    const loading = ref({
      banners: false,
      recommended: false,
      popular: false,
      latest: false
    })
    
    // 获取Banner数据
    const fetchBanners = async () => {
      try {
        loading.value.banners = true
        const response = await bannerApi.getActiveBanners()
        
        console.log('Banner API响应:', response)
        
        // 处理响应数据，将后端数据格式转换为前端需要的格式
        let bannerList = []
        if (response && Array.isArray(response)) {
          bannerList = response.map(banner => ({
            title: banner.title,
            description: banner.description,
            image: banner.imageUrl || 'https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/default-product.jpg',
            buttonText: banner.buttonText || '了解更多',
            action: banner.actionType || 'explore',
            link: banner.link
          }))
        }
        
        // 如果没有Banner数据，使用默认数据
        if (bannerList.length === 0) {
          bannerList = [
            {
              title: '发现好音乐',
              description: '海量音乐库，总有一首打动你',
              image: 'https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/default-product.jpg',
              buttonText: '立即探索',
              action: 'explore'
            }
          ]
        }
        
        // 最多显示10张图片
        banners.value = bannerList.slice(0, 10)
        
        console.log('Banner解析后数据:', banners.value)
      } catch (error) {
        console.error('获取Banner失败:', error)
        // 使用默认Banner数据
        banners.value = [
          {
            title: '发现好音乐',
            description: '海量音乐库，总有一首打动你',
            image: 'https://nie1037-oss.oss-cn-hangzhou.aliyuncs.com/default-product.jpg',
            buttonText: '立即探索',
            action: 'explore'
          }
        ]
      } finally {
        loading.value.banners = false
      }
    }
    
    // 获取推荐音乐
    const fetchRecommendedMusic = async () => {
      try {
        loading.value.recommended = true
        const response = await getRecommendMusic({ limit: 10, page: 1 })
        
        console.log('推荐音乐API响应:', response)
        
        // 处理响应数据
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
        
        console.log('推荐音乐解析后数据:', musicList)
        const filteredMusic = Array.isArray(musicList) ? musicList.filter(item => item && item.id) : []
        // 为每个音乐项添加封面URL
        filteredMusic.forEach(music => {
          if (!music.coverUrl) {
            music.coverUrl = getMusicCoverUrl(music.id)
          }
        })
        recommendedMusic.value = filteredMusic
      } catch (error) {
        console.error('获取推荐音乐失败:', error)
        ElMessage.error('获取推荐音乐失败')
        recommendedMusic.value = []
      } finally {
        loading.value.recommended = false
      }
    }
    
    // 获取热门音乐
    const fetchPopularMusic = async () => {
      try {
        loading.value.popular = true
        const response = await getHotMusic({ page: 1, limit: 10 })
        
        console.log('热门音乐API响应:', response)
        
        // 处理响应数据
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
        
        console.log('热门音乐解析后数据:', musicList)
        const filteredMusic = Array.isArray(musicList) ? musicList.filter(item => item && item.id) : []
        // 为每个音乐项添加封面URL
        filteredMusic.forEach(music => {
          if (!music.coverUrl) {
            music.coverUrl = getMusicCoverUrl(music.id)
          }
        })
        popularMusic.value = filteredMusic
      } catch (error) {
        console.error('获取热门音乐失败:', error)
        ElMessage.error('获取热门音乐失败')
        popularMusic.value = []
      } finally {
        loading.value.popular = false
      }
    }
    
    // 获取最新音乐
    const fetchLatestMusic = async () => {
      try {
        loading.value.latest = true
        const response = await getLatestMusic({ page: 1, limit: 10 })
        
        console.log('最新音乐API响应:', response)
        
        
        // 处理响应数据
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
        
        console.log('最新音乐解析后数据:', musicList)
        const filteredMusic = Array.isArray(musicList) ? musicList.filter(item => item && item.id) : []
        // 为每个音乐项添加封面URL
        filteredMusic.forEach(music => {
          if (!music.coverUrl) {
            music.coverUrl = getMusicCoverUrl(music.id)
          }
        })
        latestMusic.value = filteredMusic
      } catch (error) {
        console.error('获取最新音乐失败:', error)
        ElMessage.error('获取最新音乐失败')
        latestMusic.value = []
      } finally {
        loading.value.latest = false
      }
    }
    

    
    // 播放音乐
    const playMusic = (music) => {
      playerStore.play(music)
    }
    

    
    // 添加到播放列表
    const addToPlaylist = (music) => {
      playerStore.addToPlaylist(music)
      ElMessage.success('已添加到播放列表')
    }
    
    // 处理轮播图点击
    const handleBannerClick = (banner) => {
      // 如果有自定义链接，优先使用
      if (banner.link) {
        if (banner.link.startsWith('http')) {
          // 外部链接
          window.open(banner.link, '_blank')
        } else {
          // 内部路由
          router.push(banner.link)
        }
        return
      }
      
      // 根据action类型进行跳转
      switch (banner.action) {
        case 'explore':
          router.push('/music')
          break
        case 'create':
          router.push('/upload')
          break
        case 'share':
          router.push('/music')
          break
        default:
          router.push('/music')
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
    
    // 格式化日期
    const formatDate = (date) => {
      const now = new Date()
      const target = new Date(date)
      const diff = now - target
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (days === 0) {
        return '今天'
      } else if (days === 1) {
        return '昨天'
      } else if (days < 7) {
        return `${days}天前`
      } else {
        return target.toLocaleDateString()
      }
    }
    
    // 初始化数据
    onMounted(() => {
      fetchBanners()
      fetchRecommendedMusic()
      fetchPopularMusic()
      fetchLatestMusic()
    })
    
    return {
      banners,
      recommendedMusic,
      popularMusic,
      latestMusic,
      loading,
      fetchBanners,
      playMusic,
      addToPlaylist,
      handleBannerClick,
      formatPlayCount,
      formatDate
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 轮播图区域 */
.banner-section {
  margin-bottom: 40px;
}

.banner-item {
  height: 300px;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
}

.banner-content {
  text-align: center;
  color: white;
  z-index: 1;
}

.banner-content h2 {
  font-size: 32px;
  margin-bottom: 10px;
}

.banner-content p {
  font-size: 16px;
  margin-bottom: 20px;
  opacity: 0.9;
}

/* 区域样式 */
.section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

/* 音乐网格 */
.music-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 20px;
}

.music-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.music-card:hover {
  transform: translateY(-2px);
}

.music-cover {
  position: relative;
  width: 100%;
  height: 180px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 10px;
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

.music-card:hover .play-overlay {
  opacity: 1;
}

.music-info {
  text-align: center;
}

.music-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.music-artist {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.music-date {
  font-size: 12px;
  color: #c0c4cc;
}

/* 音乐列表 */
.music-list {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.music-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.music-item:last-child {
  border-bottom: none;
}

.music-item:hover {
  background-color: #f5f7fa;
}

.item-index {
  width: 30px;
  text-align: center;
  font-size: 14px;
  color: #909399;
  margin-right: 15px;
}

.item-cover {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 15px;
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
  font-size: 20px;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.item-artist {
  font-size: 12px;
  color: #909399;
}

.item-stats {
  margin-right: 20px;
}

.play-count {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.play-count .el-icon {
  margin-right: 5px;
}

.item-actions {
  display: flex;
  align-items: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.music-item:hover .item-actions {
  opacity: 1;
}

/* 播放列表网格 */
.playlist-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.playlist-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.playlist-card:hover {
  transform: translateY(-2px);
}

.playlist-cover {
  position: relative;
  width: 100%;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 10px;
}

.playlist-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: 20px 10px 10px;
  color: white;
}

.playlist-count {
  font-size: 12px;
  text-align: right;
}

.playlist-info {
  text-align: center;
}

.playlist-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.playlist-creator {
  font-size: 12px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home {
    padding: 10px;
  }
  
  .music-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
  
  .playlist-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
  
  .banner-content h2 {
    font-size: 24px;
  }
  
  .banner-content p {
    font-size: 14px;
  }
}

@media (max-width: 576px) {
  .music-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .playlist-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .music-item {
    padding: 10px 15px;
  }
  
  .item-index {
    display: none;
  }
}
</style>