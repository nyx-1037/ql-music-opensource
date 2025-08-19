import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { musicApi } from '@/api/music'
import { useUserStore } from './user'

export const useMusicStore = defineStore('music', () => {
  // 状态
  const musics = ref([]) // 所有音乐列表
  const currentMusic = ref(null) // 当前查看的音乐详情
  const myMusics = ref([]) // 我上传的音乐
  const favoriteMusics = ref([]) // 我收藏的音乐
  const recentMusics = ref([]) // 最近播放的音乐
  const recommendMusics = ref([]) // 推荐音乐
  const hotMusics = ref([]) // 热门音乐
  const newMusics = ref([]) // 最新音乐
  const isLoading = ref(false)
  const isUploading = ref(false)
  const uploadProgress = ref(0)
  
  // 分页信息
  const pagination = ref({
    page: 1,
    limit: 20,
    total: 0,
    hasMore: true
  })
  
  // 搜索和筛选
  const searchQuery = ref('')
  const sortBy = ref('createdAt') // createdAt, updatedAt, title, artist, playCount, duration
  const sortOrder = ref('desc') // asc, desc
  const genre = ref('all') // all, pop, rock, jazz, classical, etc.
  const duration = ref('all') // all, short(<3min), medium(3-6min), long(>6min)
  const quality = ref('all') // all, standard, high, lossless
  
  // 计算属性
  const filteredMusics = computed(() => {
    let result = [...musics.value]
    
    // 搜索过滤
    if (searchQuery.value.trim()) {
      const query = searchQuery.value.toLowerCase().trim()
      result = result.filter(music => 
        music.title.toLowerCase().includes(query) ||
        music.artist.toLowerCase().includes(query) ||
        music.album?.toLowerCase().includes(query) ||
        music.genre?.toLowerCase().includes(query)
      )
    }
    
    // 流派过滤
    if (genre.value !== 'all') {
      result = result.filter(music => music.genre === genre.value)
    }
    
    // 时长过滤
    if (duration.value !== 'all') {
      result = result.filter(music => {
        const durationInMinutes = music.duration / 60
        switch (duration.value) {
          case 'short':
            return durationInMinutes < 3
          case 'medium':
            return durationInMinutes >= 3 && durationInMinutes <= 6
          case 'long':
            return durationInMinutes > 6
          default:
            return true
        }
      })
    }
    
    // 音质过滤
    if (quality.value !== 'all') {
      result = result.filter(music => music.quality === quality.value)
    }
    
    // 排序
    result.sort((a, b) => {
      let aValue = a[sortBy.value]
      let bValue = b[sortBy.value]
      
      // 处理日期类型
      if (sortBy.value.includes('At')) {
        aValue = new Date(aValue).getTime()
        bValue = new Date(bValue).getTime()
      }
      
      // 处理数字类型
      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return sortOrder.value === 'asc' ? aValue - bValue : bValue - aValue
      }
      
      // 处理字符串类型
      if (typeof aValue === 'string' && typeof bValue === 'string') {
        return sortOrder.value === 'asc' 
          ? aValue.localeCompare(bValue)
          : bValue.localeCompare(aValue)
      }
      
      return 0
    })
    
    return result
  })
  
  const musicStats = computed(() => {
    return {
      total: musics.value.length,
      myCount: myMusics.value.length,
      favoriteCount: favoriteMusics.value.length,
      recentCount: recentMusics.value.length,
      totalDuration: musics.value.reduce((sum, music) => sum + (music.duration || 0), 0),
      totalPlayCount: musics.value.reduce((sum, music) => sum + (music.playCount || 0), 0),
      genreStats: getGenreStats(),
      qualityStats: getQualityStats()
    }
  })
  
  // 获取流派统计
  const getGenreStats = () => {
    const stats = {}
    musics.value.forEach(music => {
      const genre = music.genre || 'unknown'
      stats[genre] = (stats[genre] || 0) + 1
    })
    return stats
  }
  
  // 获取音质统计
  const getQualityStats = () => {
    const stats = {}
    musics.value.forEach(music => {
      const quality = music.quality || 'standard'
      stats[quality] = (stats[quality] || 0) + 1
    })
    return stats
  }
  
  // 获取音乐列表
  const getMusics = async (params = {}) => {
    try {
      isLoading.value = true
      
      const queryParams = {
        page: pagination.value.page,
        limit: pagination.value.limit,
        sortBy: sortBy.value,
        sortOrder: sortOrder.value,
        genre: genre.value !== 'all' ? genre.value : undefined,
        duration: duration.value !== 'all' ? duration.value : undefined,
        quality: quality.value !== 'all' ? quality.value : undefined,
        search: searchQuery.value.trim() || undefined,
        ...params
      }
      
      const response = await musicApi.getMusics(queryParams)
      const { data, total, page, limit } = response.data
      
      if (page === 1) {
        musics.value = data
      } else {
        musics.value.push(...data)
      }
      
      pagination.value = {
        page,
        limit,
        total,
        hasMore: data.length === limit
      }
      
      return response
    } catch (error) {
      console.error('Get musics error:', error)
      ElMessage.error(error.message || '获取音乐列表失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  // 获取音乐详情
  const getMusicDetail = async (id) => {
    try {
      isLoading.value = true
      
      const response = await musicApi.getMusicDetail(id)
      currentMusic.value = response.data
      
      return response
    } catch (error) {
      console.error('Get music detail error:', error)
      ElMessage.error(error.message || '获取音乐详情失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  // 获取我的音乐
  const getMyMusics = async () => {
    try {
      const userStore = useUserStore()
      if (!userStore.isLoggedIn) return
      
      const response = await musicApi.getMyMusics()
      myMusics.value = response.data
      
      return response
    } catch (error) {
      console.error('Get my musics error:', error)
      throw error
    }
  }
  
  // 获取收藏的音乐
  const getFavoriteMusics = async () => {
    try {
      const userStore = useUserStore()
      if (!userStore.isLoggedIn) return
      
      const response = await musicApi.getFavoriteMusics()
      favoriteMusics.value = response.data
      
      return response
    } catch (error) {
      console.error('Get favorite musics error:', error)
      throw error
    }
  }
  
  // 获取最近播放
  const getRecentMusics = async () => {
    try {
      const userStore = useUserStore()
      if (!userStore.isLoggedIn) return
      
      const response = await musicApi.getRecentMusics()
      recentMusics.value = response.data
      
      return response
    } catch (error) {
      console.error('Get recent musics error:', error)
      throw error
    }
  }
  
  // 获取推荐音乐
  const getRecommendMusics = async () => {
    try {
      const response = await musicApi.getRecommendMusics()
      recommendMusics.value = response.data
      
      return response
    } catch (error) {
      console.error('Get recommend musics error:', error)
      throw error
    }
  }
  
  // 获取热门音乐
  const getHotMusics = async () => {
    try {
      const response = await musicApi.getHotMusics()
      hotMusics.value = response.data
      
      return response
    } catch (error) {
      console.error('Get hot musics error:', error)
      throw error
    }
  }
  
  // 获取最新音乐
  const getNewMusics = async () => {
    try {
      const response = await musicApi.getNewMusics()
      newMusics.value = response.data
      
      return response
    } catch (error) {
      console.error('Get new musics error:', error)
      throw error
    }
  }
  
  // 上传音乐
  const uploadMusic = async (musicData, onProgress) => {
    try {
      isUploading.value = true
      uploadProgress.value = 0
      
      const response = await musicApi.uploadMusic(musicData, {
        onUploadProgress: (progressEvent) => {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          uploadProgress.value = progress
          if (onProgress) {
            onProgress(progress)
          }
        }
      })
      
      const newMusic = response.data
      
      // 添加到我的音乐列表
      myMusics.value.unshift(newMusic)
      
      // 如果当前在查看所有音乐，也添加到列表中
      if (musics.value.length > 0) {
        musics.value.unshift(newMusic)
      }
      
      ElMessage.success('音乐上传成功')
      
      return response
    } catch (error) {
      console.error('Upload music error:', error)
      ElMessage.error(error.message || '音乐上传失败')
      throw error
    } finally {
      isUploading.value = false
      uploadProgress.value = 0
    }
  }
  
  // 更新音乐信息
  const updateMusic = async (id, musicData) => {
    try {
      const response = await musicApi.updateMusic(id, musicData)
      const updatedMusic = response.data
      
      // 更新当前音乐
      if (currentMusic.value && currentMusic.value.id === id) {
        currentMusic.value = updatedMusic
      }
      
      // 更新我的音乐列表
      const myIndex = myMusics.value.findIndex(m => m.id === id)
      if (myIndex >= 0) {
        myMusics.value[myIndex] = updatedMusic
      }
      
      // 更新所有音乐列表
      const allIndex = musics.value.findIndex(m => m.id === id)
      if (allIndex >= 0) {
        musics.value[allIndex] = updatedMusic
      }
      
      ElMessage.success('音乐信息更新成功')
      
      return response
    } catch (error) {
      console.error('Update music error:', error)
      ElMessage.error(error.message || '更新音乐信息失败')
      throw error
    }
  }
  
  // 删除音乐
  const deleteMusic = async (id) => {
    try {
      await musicApi.deleteMusic(id)
      
      // 从我的音乐列表中移除
      const myIndex = myMusics.value.findIndex(m => m.id === id)
      if (myIndex >= 0) {
        myMusics.value.splice(myIndex, 1)
      }
      
      // 从所有音乐列表中移除
      const allIndex = musics.value.findIndex(m => m.id === id)
      if (allIndex >= 0) {
        musics.value.splice(allIndex, 1)
      }
      
      // 从收藏列表中移除
      const favoriteIndex = favoriteMusics.value.findIndex(m => m.id === id)
      if (favoriteIndex >= 0) {
        favoriteMusics.value.splice(favoriteIndex, 1)
      }
      
      // 如果删除的是当前查看的音乐，清空当前音乐
      if (currentMusic.value && currentMusic.value.id === id) {
        currentMusic.value = null
      }
      
      ElMessage.success('音乐删除成功')
      
    } catch (error) {
      console.error('Delete music error:', error)
      ElMessage.error(error.message || '删除音乐失败')
      throw error
    }
  }
  
  // 收藏/取消收藏音乐
  const toggleFavoriteMusic = async (id) => {
    try {
      const isFavorite = favoriteMusics.value.some(m => m.id === id)
      
      if (isFavorite) {
        await musicApi.unfavoriteMusic(id)
        
        // 从收藏列表中移除
        const index = favoriteMusics.value.findIndex(m => m.id === id)
        if (index >= 0) {
          favoriteMusics.value.splice(index, 1)
        }
        
        ElMessage.success('已取消收藏')
      } else {
        const response = await musicApi.favoriteMusic(id)
        
        // 添加到收藏列表
        favoriteMusics.value.unshift(response.data)
        
        ElMessage.success('已添加到收藏')
      }
      
      // 更新音乐的收藏状态
      const updateFavoriteStatus = (list) => {
        const music = list.find(m => m.id === id)
        if (music) {
          music.isFavorite = !isFavorite
          music.favoriteCount = music.favoriteCount + (isFavorite ? -1 : 1)
        }
      }
      
      updateFavoriteStatus(musics.value)
      updateFavoriteStatus(myMusics.value)
      updateFavoriteStatus(recommendMusics.value)
      updateFavoriteStatus(hotMusics.value)
      updateFavoriteStatus(newMusics.value)
      
      if (currentMusic.value && currentMusic.value.id === id) {
        currentMusic.value.isFavorite = !isFavorite
        currentMusic.value.favoriteCount = currentMusic.value.favoriteCount + (isFavorite ? -1 : 1)
      }
      
    } catch (error) {
      console.error('Toggle favorite music error:', error)
      ElMessage.error(error.message || '操作失败')
      throw error
    }
  }
  
  // 播放音乐（增加播放次数）
  const playMusic = async (id) => {
    try {
      await musicApi.playMusic(id)
      
      // 更新播放次数
      const updatePlayCount = (list) => {
        const music = list.find(m => m.id === id)
        if (music) {
          music.playCount = (music.playCount || 0) + 1
        }
      }
      
      updatePlayCount(musics.value)
      updatePlayCount(myMusics.value)
      updatePlayCount(favoriteMusics.value)
      updatePlayCount(recommendMusics.value)
      updatePlayCount(hotMusics.value)
      updatePlayCount(newMusics.value)
      
      if (currentMusic.value && currentMusic.value.id === id) {
        currentMusic.value.playCount = (currentMusic.value.playCount || 0) + 1
      }
      
      // 添加到最近播放（如果不存在）
      const recentIndex = recentMusics.value.findIndex(m => m.id === id)
      if (recentIndex >= 0) {
        // 移动到最前面
        const music = recentMusics.value.splice(recentIndex, 1)[0]
        recentMusics.value.unshift(music)
      } else {
        // 添加到最前面
        const music = musics.value.find(m => m.id === id) || 
                     myMusics.value.find(m => m.id === id) ||
                     currentMusic.value
        if (music) {
          recentMusics.value.unshift({ ...music, playTime: Date.now() })
          // 限制最近播放数量
          if (recentMusics.value.length > 50) {
            recentMusics.value = recentMusics.value.slice(0, 50)
          }
        }
      }
      
    } catch (error) {
      console.error('Play music error:', error)
    }
  }
  
  // 搜索和筛选
  const setSearchQuery = (query) => {
    searchQuery.value = query
    resetPagination()
  }
  
  const setSortBy = (field) => {
    if (sortBy.value === field) {
      sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
    } else {
      sortBy.value = field
      sortOrder.value = 'desc'
    }
    resetPagination()
  }
  
  const setGenre = (g) => {
    genre.value = g
    resetPagination()
  }
  
  const setDuration = (d) => {
    duration.value = d
    resetPagination()
  }
  
  const setQuality = (q) => {
    quality.value = q
    resetPagination()
  }
  
  // 分页
  const loadMore = async () => {
    if (!pagination.value.hasMore || isLoading.value) return
    
    pagination.value.page++
    await getMusics()
  }
  
  const resetPagination = () => {
    pagination.value.page = 1
    pagination.value.hasMore = true
    musics.value = []
  }
  
  // 刷新数据
  const refresh = async () => {
    resetPagination()
    await Promise.all([
      getMusics(),
      getMyMusics(),
      getFavoriteMusics(),
      getRecentMusics(),
      getRecommendMusics(),
      getHotMusics(),
      getNewMusics()
    ])
  }
  
  // 清空数据
  const clear = () => {
    musics.value = []
    currentMusic.value = null
    myMusics.value = []
    favoriteMusics.value = []
    recentMusics.value = []
    recommendMusics.value = []
    hotMusics.value = []
    newMusics.value = []
    resetPagination()
    searchQuery.value = ''
    sortBy.value = 'createdAt'
    sortOrder.value = 'desc'
    genre.value = 'all'
    duration.value = 'all'
    quality.value = 'all'
  }
  
  // 检查是否是我的音乐
  const isMyMusic = (musicId) => {
    return myMusics.value.some(m => m.id === musicId)
  }
  
  // 检查是否收藏了音乐
  const isFavoriteMusic = (musicId) => {
    return favoriteMusics.value.some(m => m.id === musicId)
  }
  
  // 获取音乐播放URL
  const getMusicUrl = async (id) => {
    try {
      const response = await musicApi.getMusicUrl(id)
      return response.data.url
    } catch (error) {
      console.error('Get music url error:', error)
      throw error
    }
  }
  
  // 获取音乐歌词
  const getMusicLyrics = async () => {
    // TODO: 实现获取歌词功能
    return '暂无歌词'
  }
  
  return {
    // 状态
    musics,
    currentMusic,
    myMusics,
    favoriteMusics,
    recentMusics,
    recommendMusics,
    hotMusics,
    newMusics,
    isLoading,
    isUploading,
    uploadProgress,
    pagination,
    searchQuery,
    sortBy,
    sortOrder,
    genre,
    duration,
    quality,
    
    // 计算属性
    filteredMusics,
    musicStats,
    
    // 方法
    getMusics,
    getMusicDetail,
    getMyMusics,
    getFavoriteMusics,
    getRecentMusics,
    getRecommendMusics,
    getHotMusics,
    getNewMusics,
    uploadMusic,
    updateMusic,
    deleteMusic,
    toggleFavoriteMusic,
    playMusic,
    setSearchQuery,
    setSortBy,
    setGenre,
    setDuration,
    setQuality,
    loadMore,
    resetPagination,
    refresh,
    clear,
    isMyMusic,
    isFavoriteMusic,
    getMusicUrl,
    getMusicLyrics
  }
}, {
  persist: {
    key: 'music',
    storage: localStorage,
    paths: [
      'myMusics',
      'favoriteMusics',
      'recentMusics',
      'sortBy',
      'sortOrder',
      'genre',
      'duration',
      'quality'
    ]
  }
})