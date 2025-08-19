import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { musicApi } from '@/api/music'

// 播放模式枚举
export const PlayMode = {
  SEQUENCE: 'sequence', // 顺序播放
  LOOP: 'loop', // 列表循环
  SINGLE: 'single', // 单曲循环
  RANDOM: 'random' // 随机播放
}

// 播放状态枚举
export const PlayState = {
  PLAYING: 'playing',
  PAUSED: 'paused',
  STOPPED: 'stopped',
  LOADING: 'loading',
  ERROR: 'error'
}

export const usePlayerStore = defineStore('player', () => {
  // 状态
  const currentMusic = ref(null) // 当前播放的音乐
  const playlist = ref([]) // 当前播放列表
  const currentIndex = ref(-1) // 当前播放索引
  const playState = ref(PlayState.STOPPED) // 播放状态
  const playMode = ref(PlayMode.SEQUENCE) // 播放模式
  const volume = ref(0.8) // 音量 (0-1)
  const muted = ref(false) // 是否静音
  const currentTime = ref(0) // 当前播放时间
  const duration = ref(0) // 音乐总时长
  const buffered = ref(0) // 缓冲进度
  const showLyrics = ref(false) // 是否显示歌词
  const showPlaylist = ref(false) // 是否显示播放列表
  const showMiniPlayer = ref(false) // 是否显示迷你播放器
  const audioElement = ref(null) // 音频元素引用
  const playHistory = ref([]) // 播放历史
  const favoriteIds = ref(new Set()) // 收藏的音乐ID集合
  
  // 计算属性
  const isPlaying = computed(() => playState.value === PlayState.PLAYING)
  const isPaused = computed(() => playState.value === PlayState.PAUSED)
  const isLoading = computed(() => playState.value === PlayState.LOADING)
  const hasError = computed(() => playState.value === PlayState.ERROR)
  
  const progress = computed(() => {
    if (duration.value === 0) return 0
    return (currentTime.value / duration.value) * 100
  })
  
  const bufferedProgress = computed(() => {
    if (duration.value === 0) return 0
    return (buffered.value / duration.value) * 100
  })
  
  const hasNext = computed(() => {
    if (playlist.value.length === 0) return false
    if (playlist.value.length === 1) return playMode.value === PlayMode.SINGLE
    return true // 支持循环播放，始终可以切换下一首
  })
  
  const hasPrev = computed(() => {
    if (playlist.value.length === 0) return false
    if (playlist.value.length === 1) return playMode.value === PlayMode.SINGLE
    return true // 支持循环播放，始终可以切换上一首
  })
  
  const currentMusicInfo = computed(() => {
    if (!currentMusic.value) return null
    
    return {
      id: currentMusic.value.id,
      title: currentMusic.value.title,
      artist: currentMusic.value.artist,
      album: currentMusic.value.album,
      cover: currentMusic.value.cover || '/default-cover.png',
      duration: currentMusic.value.duration,
      url: currentMusic.value.url,
      lyrics: currentMusic.value.lyrics,
      isFavorite: favoriteIds.value.has(currentMusic.value.id)
    }
  })
  
  const playModeText = computed(() => {
    switch (playMode.value) {
      case PlayMode.SEQUENCE:
        return '顺序播放'
      case PlayMode.SINGLE:
        return '单曲循环'
      case PlayMode.RANDOM:
        return '随机播放'
      default:
        return '顺序播放'
    }
  })
  
  const playModeIcon = computed(() => {
    switch (playMode.value) {
      case PlayMode.SEQUENCE:
        return 'icon-sequence'
      case PlayMode.LOOP:
        return 'icon-loop'
      case PlayMode.SINGLE:
        return 'icon-single'
      case PlayMode.RANDOM:
        return 'icon-random'
      default:
        return 'icon-sequence'
    }
  })

  // 时间格式化函数
  const formatTime = (seconds) => {
    if (!seconds || isNaN(seconds)) return '00:00'
    const mins = Math.floor(seconds / 60)
    const secs = Math.floor(seconds % 60)
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
  }

  // 格式化的当前时间
  const formattedCurrentTime = computed(() => formatTime(currentTime.value))

  // 格式化的总时长
  const formattedDuration = computed(() => formatTime(duration.value))
  
  // 播放控制方法
  const play = async (music = null, musicList = null, index = null) => {
    try {
      // 如果传入了新的音乐，设置为当前音乐
      if (music) {
        currentMusic.value = music
        
        // 如果传入了播放列表，更新播放列表
        if (musicList && Array.isArray(musicList)) {
          playlist.value = musicList
          currentIndex.value = index !== null ? index : musicList.findIndex(m => m.id === music.id)
        } else {
          // 如果没有传入播放列表，将当前音乐添加到播放列表
          const existingIndex = playlist.value.findIndex(m => m.id === music.id)
          if (existingIndex >= 0) {
            currentIndex.value = existingIndex
          } else {
            playlist.value.push(music)
            currentIndex.value = playlist.value.length - 1
          }
        }
        
        // 添加到播放历史
        addToHistory(music)
      }
      
      if (!currentMusic.value) {
        throw new Error('没有可播放的音乐')
      }
      
      playState.value = PlayState.LOADING
      
      // 获取音乐播放URL（如果需要）
      if (!currentMusic.value.url) {
        const urlResponse = await musicApi.getMusicUrl(currentMusic.value.id)
        // request.js已经解包了响应数据，直接使用response.url
        currentMusic.value.url = urlResponse.url
      }
      
      // 获取音乐封面URL（如果需要）
      if (!currentMusic.value.coverUrl) {
        try {
          currentMusic.value.coverUrl = musicApi.getMusicCoverUrl(currentMusic.value.id)
        } catch (error) {
          console.warn('Failed to get cover URL:', error)
          currentMusic.value.coverUrl = '/img/default-cover.jpg'
        }
      }
      
      // 播放音乐
      if (audioElement.value) {
        audioElement.value.src = currentMusic.value.url
        await audioElement.value.play()
        playState.value = PlayState.PLAYING
      }
      
    } catch (error) {
      console.error('Play error:', error)
      playState.value = PlayState.ERROR
      ElMessage.error(error.message || '播放失败')
    }
  }
  
  const pause = () => {
    console.log('Pause called - audioElement:', !!audioElement.value, 'isPlaying:', isPlaying.value)
    if (audioElement.value && isPlaying.value) {
      audioElement.value.pause()
      playState.value = PlayState.PAUSED
      console.log('Audio paused, state set to PAUSED')
    } else {
      console.log('Cannot pause - audioElement or not playing')
    }
  }
  
  const resume = async () => {
    console.log('Resume called - audioElement:', !!audioElement.value, 'isPaused:', isPaused.value)
    try {
      if (audioElement.value && isPaused.value) {
        await audioElement.value.play()
        playState.value = PlayState.PLAYING
        console.log('Audio resumed, state set to PLAYING')
      } else {
        console.log('Cannot resume - audioElement or not paused')
      }
    } catch (error) {
      console.error('Resume error:', error)
      playState.value = PlayState.ERROR
      ElMessage.error('播放失败')
    }
  }
  
  const stop = () => {
    if (audioElement.value) {
      audioElement.value.pause()
      audioElement.value.currentTime = 0
      currentTime.value = 0
      playState.value = PlayState.STOPPED
    }
  }
  
  const toggle = async () => {
    console.log('Toggle called - Current state:', {
      isPlaying: isPlaying.value,
      isPaused: isPaused.value,
      playState: playState.value,
      audioElement: !!audioElement.value,
      currentMusic: !!currentMusic.value
    })
    
    if (isPlaying.value) {
      console.log('Calling pause()')
      pause()
    } else if (isPaused.value) {
      console.log('Calling resume()')
      await resume()
    } else {
      console.log('Calling play()')
      await play()
    }
  }
  
  const next = async () => {
    console.log('Next called - playlist length:', playlist.value.length, 'currentIndex:', currentIndex.value, 'playMode:', playMode.value)
    if (playlist.value.length === 0) return
    
    let nextIndex = currentIndex.value
    
    switch (playMode.value) {
      case PlayMode.SEQUENCE:
      case PlayMode.LOOP:
        nextIndex = currentIndex.value + 1
        if (nextIndex >= playlist.value.length) {
          nextIndex = 0 // 循环到第一首
        }
        break
      case PlayMode.SINGLE:
        // 单曲循环，重新播放当前歌曲
        nextIndex = currentIndex.value
        break
      case PlayMode.RANDOM:
        // 随机播放时，确保不会选择当前正在播放的歌曲（除非只有一首歌）
        if (playlist.value.length > 1) {
          do {
            nextIndex = Math.floor(Math.random() * playlist.value.length)
          } while (nextIndex === currentIndex.value)
        } else {
          nextIndex = currentIndex.value
        }
        break
    }
    
    console.log('Next - calculated nextIndex:', nextIndex, 'will change:', nextIndex !== currentIndex.value || playMode.value === PlayMode.SINGLE)
    // 总是切换到计算出的索引
    currentIndex.value = nextIndex
    await play(playlist.value[nextIndex])
  }
  
  const prev = async () => {
    console.log('Prev called - playlist length:', playlist.value.length, 'currentIndex:', currentIndex.value, 'playMode:', playMode.value)
    if (playlist.value.length === 0) return
    
    let prevIndex = currentIndex.value
    
    switch (playMode.value) {
      case PlayMode.SEQUENCE:
      case PlayMode.LOOP:
        prevIndex = currentIndex.value - 1
        if (prevIndex < 0) {
          prevIndex = playlist.value.length - 1 // 循环到最后一首
        }
        break
      case PlayMode.SINGLE:
        // 单曲循环，重新播放当前歌曲
        prevIndex = currentIndex.value
        break
      case PlayMode.RANDOM:
        // 随机播放时，确保不会选择当前正在播放的歌曲（除非只有一首歌）
        if (playlist.value.length > 1) {
          do {
            prevIndex = Math.floor(Math.random() * playlist.value.length)
          } while (prevIndex === currentIndex.value)
        } else {
          prevIndex = currentIndex.value
        }
        break
    }
    
    console.log('Prev - calculated prevIndex:', prevIndex, 'will change:', prevIndex !== currentIndex.value || playMode.value === PlayMode.SINGLE)
    // 总是切换到计算出的索引
    currentIndex.value = prevIndex
    await play(playlist.value[prevIndex])
  }
  
  const seek = (time) => {
    if (audioElement.value && duration.value > 0) {
      const seekTime = Math.max(0, Math.min(time, duration.value))
      audioElement.value.currentTime = seekTime
      currentTime.value = seekTime
    }
  }
  
  const seekToProgress = (progress) => {
    if (duration.value > 0) {
      const time = (progress / 100) * duration.value
      seek(time)
    }
  }
  
  // 音量控制
  const setVolume = (vol) => {
    const newVolume = Math.max(0, Math.min(1, vol))
    volume.value = newVolume
    if (audioElement.value) {
      audioElement.value.volume = newVolume
    }
    if (newVolume > 0) {
      muted.value = false
    }
  }
  
  const toggleMute = () => {
    muted.value = !muted.value
    if (audioElement.value) {
      audioElement.value.muted = muted.value
    }
  }
  
  // 播放模式控制
  const togglePlayMode = () => {
    const modes = [PlayMode.RANDOM, PlayMode.SEQUENCE, PlayMode.SINGLE]
    const currentModeIndex = modes.indexOf(playMode.value)
    const nextModeIndex = (currentModeIndex + 1) % modes.length
    playMode.value = modes[nextModeIndex]
    
    ElMessage.success(`切换到${playModeText.value}`)
  }
  
  const setPlayMode = (mode) => {
    if (Object.values(PlayMode).includes(mode)) {
      playMode.value = mode
    }
  }
  
  // 播放列表管理
  const addToPlaylist = (music) => {
    const existingIndex = playlist.value.findIndex(m => m.id === music.id)
    if (existingIndex === -1) {
      // 为音乐设置封面URL（如果没有的话）
      if (!music.coverUrl) {
        try {
          music.coverUrl = musicApi.getMusicCoverUrl(music.id)
        } catch (error) {
          console.warn('Failed to get cover URL:', error)
          music.coverUrl = '/img/default-cover.jpg'
        }
      }
      playlist.value.push(music)
      ElMessage.success(`已添加到播放列表`)
    } else {
      ElMessage.info('歌曲已在播放列表中')
    }
  }
  
  const removeFromPlaylist = (index) => {
    if (index >= 0 && index < playlist.value.length) {
      playlist.value.splice(index, 1)
      
      // 如果删除的是当前播放的音乐
      if (index === currentIndex.value) {
        if (playlist.value.length === 0) {
          // 播放列表为空，停止播放
          stop()
          currentMusic.value = null
          currentIndex.value = -1
        } else {
          // 播放下一首
          if (currentIndex.value >= playlist.value.length) {
            currentIndex.value = 0
          }
          play(playlist.value[currentIndex.value])
        }
      } else if (index < currentIndex.value) {
        // 删除的是当前播放音乐之前的，调整索引
        currentIndex.value--
      }
      
      ElMessage.success(`已从播放列表移除`)
    }
  }
  
  const clearPlaylist = () => {
    playlist.value = []
    currentIndex.value = -1
    stop()
    currentMusic.value = null
    ElMessage.success('播放列表已清空')
  }
  
  const playMusicAt = async (index) => {
    if (index >= 0 && index < playlist.value.length) {
      currentIndex.value = index
      await play(playlist.value[index])
    }
  }
  
  const setPlaylist = (musicList) => {
    if (Array.isArray(musicList)) {
      // 为每个音乐项添加封面URL（如果没有的话）
      const processedList = musicList.map(music => {
        if (!music.coverUrl) {
          try {
            music.coverUrl = musicApi.getMusicCoverUrl(music.id)
          } catch (error) {
            console.warn('Failed to get cover URL:', error)
            music.coverUrl = '/img/default-cover.jpg'
          }
        }
        return music
      })
      
      playlist.value = processedList
      currentIndex.value = -1
      ElMessage.success(`已设置播放列表 (${musicList.length} 首音乐)`)
    }
  }
  
  // 播放历史管理
  const addToHistory = (music) => {
    // 移除已存在的记录
    const existingIndex = playHistory.value.findIndex(h => h.id === music.id)
    if (existingIndex >= 0) {
      playHistory.value.splice(existingIndex, 1)
    }
    
    // 添加到历史记录开头
    playHistory.value.unshift({
      ...music,
      playTime: Date.now()
    })
    
    // 限制历史记录数量
    if (playHistory.value.length > 100) {
      playHistory.value = playHistory.value.slice(0, 100)
    }
  }
  
  const clearHistory = () => {
    playHistory.value = []
    ElMessage.success('播放历史已清空')
  }
  
  // 收藏管理
  const toggleFavorite = async (musicId = null) => {
    try {
      const id = musicId || currentMusic.value?.id
      if (!id) return
      
      if (favoriteIds.value.has(id)) {
        // 取消收藏
        await musicApi.removeFavorite(id)
        favoriteIds.value.delete(id)
        ElMessage.success('已取消收藏')
      } else {
        // 添加收藏
        await musicApi.addFavorite(id)
        favoriteIds.value.add(id)
        ElMessage.success('已添加到收藏')
      }
    } catch (error) {
      console.error('Toggle favorite error:', error)
      ElMessage.error(error.message || '操作失败')
    }
  }
  
  const loadFavorites = async () => {
    try {
      const response = await musicApi.getFavorites()
      favoriteIds.value = new Set(response.data.map(music => music.id))
    } catch (error) {
      console.error('Load favorites error:', error)
    }
  }
  
  // 音频事件处理
  const setupAudioEvents = (audio) => {
    if (!audio) return
    
    audioElement.value = audio
    
    // 设置初始音量
    audio.volume = volume.value
    audio.muted = muted.value
    
    // 监听事件
    audio.addEventListener('loadstart', () => {
      playState.value = PlayState.LOADING
    })
    
    audio.addEventListener('canplay', () => {
      if (playState.value === PlayState.LOADING) {
        playState.value = PlayState.PAUSED
      }
    })
    
    audio.addEventListener('play', () => {
      playState.value = PlayState.PLAYING
    })
    
    audio.addEventListener('pause', () => {
      if (playState.value === PlayState.PLAYING) {
        playState.value = PlayState.PAUSED
      }
    })
    
    audio.addEventListener('ended', () => {
      console.log('Audio ended - playMode:', playMode.value, 'playlist length:', playlist.value.length)
      // 播放结束，自动播放下一首
      if (playMode.value === PlayMode.SINGLE) {
        // 单曲循环，重新播放当前歌曲
        console.log('Single mode - replaying current song')
        audio.currentTime = 0
        audio.play()
      } else if (playlist.value.length > 0) {
        // 其他模式，播放下一首
        console.log('Auto playing next song')
        next()
      }
    })
    
    audio.addEventListener('timeupdate', () => {
      currentTime.value = audio.currentTime
    })
    
    audio.addEventListener('durationchange', () => {
      duration.value = audio.duration || 0
    })
    
    audio.addEventListener('progress', () => {
      if (audio.buffered.length > 0) {
        buffered.value = audio.buffered.end(audio.buffered.length - 1)
      }
    })
    
    audio.addEventListener('error', (e) => {
      console.error('Audio error:', e)
      playState.value = PlayState.ERROR
      ElMessage.error('音频播放出错')
    })
    
    audio.addEventListener('volumechange', () => {
      volume.value = audio.volume
      muted.value = audio.muted
    })
  }
  
  // UI 控制
  const toggleLyrics = () => {
    showLyrics.value = !showLyrics.value
  }
  
  const togglePlaylistPanel = () => {
    showPlaylist.value = !showPlaylist.value
  }
  
  const toggleMiniPlayer = () => {
    showMiniPlayer.value = !showMiniPlayer.value
  }
  
  // 初始化
  const init = async () => {
    try {
      await loadFavorites()
    } catch (error) {
      console.error('Player init error:', error)
    }
  }
  
  return {
    // 状态
    currentMusic,
    playlist,
    currentIndex,
    playState,
    playMode,
    volume,
    muted,
    currentTime,
    duration,
    buffered,
    showLyrics,
    showPlaylist,
    showMiniPlayer,
    audioElement,
    playHistory,
    favoriteIds,
    
    // 计算属性
    isPlaying,
    isPaused,
    isLoading,
    hasError,
    progress,
    bufferedProgress,
    hasNext,
    hasPrev,
    hasPrevious: hasPrev,
    currentMusicInfo,
    playModeText,
    playModeIcon,
    formattedCurrentTime,
    formattedDuration,
    
    // 播放控制
    play,
    pause,
    resume,
    stop,
    toggle,
    togglePlay: toggle,
    next,
    prev,
    previous: prev,
    seek,
    seekToProgress,
    
    // 音量控制
    setVolume,
    toggleMute,
    
    // 播放模式
    togglePlayMode,
    setPlayMode,
    
    // 播放列表
    addToPlaylist,
    removeFromPlaylist,
    clearPlaylist,
    playMusicAt,
    setPlaylist,
    
    // 播放历史
    addToHistory,
    clearHistory,
    
    // 收藏
    toggleFavorite,
    loadFavorites,
    
    // 音频事件
  setupAudioEvents,
  
  // 设置音频元素
  setAudioElement: (element) => {
    audioElement.value = element
    if (element) {
      setupAudioEvents(element)
    }
  },
    
    // UI 控制
    toggleLyrics,
    togglePlaylistPanel,
    toggleMiniPlayer,
    
    // 初始化
    init
  }
}, {
  persist: {
    key: 'player',
    storage: localStorage,
    paths: [
      'playlist',
      'currentIndex',
      'playMode',
      'volume',
      'muted',
      'showLyrics',
      'showMiniPlayer',
      'playHistory',
      'favoriteIds'
    ]
  }
})