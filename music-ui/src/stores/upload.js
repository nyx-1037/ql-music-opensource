import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { uploadApi } from '@/api'
import { useMusicStore } from './music'

export const useUploadStore = defineStore('upload', () => {
  // 上传队列
  const uploadQueue = ref([]) // 上传队列
  const uploadHistory = ref([]) // 上传历史
  const currentUpload = ref(null) // 当前上传的文件
  
  // 上传状态
  const isUploading = ref(false)
  const isPaused = ref(false)
  const totalProgress = ref(0) // 总体进度
  const uploadSpeed = ref(0) // 上传速度 (bytes/s)
  const remainingTime = ref(0) // 剩余时间 (seconds)
  
  // 上传设置
  const settings = ref({
    maxFileSize: 100 * 1024 * 1024, // 100MB
    maxFiles: 10, // 最大同时上传文件数
    chunkSize: 1024 * 1024, // 分片大小 1MB
    retryCount: 3, // 重试次数
    autoStart: true, // 自动开始上传
    allowedFormats: ['mp3', 'flac', 'wav', 'aac', 'm4a', 'ogg'], // 支持的格式
    quality: 'high', // 默认音质
    autoExtractMetadata: true, // 自动提取元数据
    autoGenerateThumbnail: true // 自动生成缩略图
  })
  
  // 错误信息
  const errors = ref([])
  
  // 计算属性
  const queuedFiles = computed(() => {
    return uploadQueue.value.filter(file => file.status === 'queued')
  })
  
  const uploadingFiles = computed(() => {
    return uploadQueue.value.filter(file => file.status === 'uploading')
  })
  
  const completedFiles = computed(() => {
    return uploadQueue.value.filter(file => file.status === 'completed')
  })
  
  const failedFiles = computed(() => {
    return uploadQueue.value.filter(file => file.status === 'failed')
  })
  
  const pausedFiles = computed(() => {
    return uploadQueue.value.filter(file => file.status === 'paused')
  })
  
  const totalFiles = computed(() => {
    return uploadQueue.value.length
  })
  
  const completedCount = computed(() => {
    return completedFiles.value.length
  })
  
  const failedCount = computed(() => {
    return failedFiles.value.length
  })
  
  const queuedCount = computed(() => {
    return queuedFiles.value.length
  })
  
  const uploadingCount = computed(() => {
    return uploadingFiles.value.length
  })
  
  const totalSize = computed(() => {
    return uploadQueue.value.reduce((total, file) => total + file.size, 0)
  })
  
  const uploadedSize = computed(() => {
    return uploadQueue.value.reduce((total, file) => {
      if (file.status === 'completed') {
        return total + file.size
      } else if (file.status === 'uploading') {
        return total + (file.size * file.progress / 100)
      }
      return total
    }, 0)
  })
  
  const canUpload = computed(() => {
    return queuedFiles.value.length > 0 && !isUploading.value
  })
  
  const canPause = computed(() => {
    return isUploading.value && uploadingFiles.value.length > 0
  })
  
  const canResume = computed(() => {
    return isPaused.value || pausedFiles.value.length > 0
  })
  
  const canRetry = computed(() => {
    return failedFiles.value.length > 0
  })
  
  const hasErrors = computed(() => {
    return errors.value.length > 0 || failedFiles.value.length > 0
  })
  
  // 添加文件到上传队列
  const addFiles = (files) => {
    const validFiles = []
    const invalidFiles = []
    
    Array.from(files).forEach(file => {
      const validation = validateFile(file)
      if (validation.valid) {
        const uploadFile = createUploadFile(file)
        validFiles.push(uploadFile)
      } else {
        invalidFiles.push({ file, errors: validation.errors })
      }
    })
    
    // 添加有效文件到队列
    uploadQueue.value.push(...validFiles)
    
    // 处理无效文件
    if (invalidFiles.length > 0) {
      invalidFiles.forEach(({ file, errors }) => {
        errors.forEach(error => {
          addError(`文件 ${file.name}: ${error}`)
        })
      })
    }
    
    // 自动开始上传
    if (settings.value.autoStart && validFiles.length > 0) {
      startUpload()
    }
    
    return {
      valid: validFiles.length,
      invalid: invalidFiles.length,
      total: files.length
    }
  }
  
  // 验证文件
  const validateFile = (file) => {
    const errors = []
    
    // 检查文件大小
    if (file.size > settings.value.maxFileSize) {
      errors.push(`文件大小超过限制 (${formatFileSize(settings.value.maxFileSize)})`)
    }
    
    // 检查文件格式
    const extension = file.name.split('.').pop().toLowerCase()
    if (!settings.value.allowedFormats.includes(extension)) {
      errors.push(`不支持的文件格式 (支持: ${settings.value.allowedFormats.join(', ')})`)
    }
    
    // 检查队列中是否已存在相同文件
    const exists = uploadQueue.value.some(queueFile => 
      queueFile.name === file.name && queueFile.size === file.size
    )
    if (exists) {
      errors.push('文件已在上传队列中')
    }
    
    return {
      valid: errors.length === 0,
      errors
    }
  }
  
  // 创建上传文件对象
  const createUploadFile = (file) => {
    return {
      id: generateId(),
      file,
      name: file.name,
      size: file.size,
      type: file.type,
      status: 'queued', // queued, uploading, paused, completed, failed
      progress: 0,
      uploadedBytes: 0,
      speed: 0,
      remainingTime: 0,
      retryCount: 0,
      error: null,
      metadata: null,
      thumbnail: null,
      musicId: null,
      createdAt: Date.now(),
      startedAt: null,
      completedAt: null
    }
  }
  
  // 开始上传
  const startUpload = async () => {
    if (isUploading.value || queuedFiles.value.length === 0) return
    
    try {
      isUploading.value = true
      isPaused.value = false
      
      // 并发上传文件
      const maxConcurrent = Math.min(settings.value.maxFiles, queuedFiles.value.length)
      const uploadPromises = []
      
      for (let i = 0; i < maxConcurrent; i++) {
        const file = queuedFiles.value[i]
        if (file) {
          uploadPromises.push(uploadFile(file))
        }
      }
      
      await Promise.allSettled(uploadPromises)
      
    } catch (error) {
      console.error('Upload error:', error)
      addError('上传过程中发生错误')
    } finally {
      isUploading.value = false
      updateTotalProgress()
    }
  }
  
  // 上传单个文件
  const uploadFile = async (uploadFile) => {
    try {
      uploadFile.status = 'uploading'
      uploadFile.startedAt = Date.now()
      currentUpload.value = uploadFile
      
      // 提取元数据
      if (settings.value.autoExtractMetadata) {
        uploadFile.metadata = await extractMetadata(uploadFile.file)
      }
      
      // 生成缩略图
      if (settings.value.autoGenerateThumbnail) {
        uploadFile.thumbnail = await generateThumbnail(uploadFile.file)
      }
      
      // 准备上传数据
      const formData = new FormData()
      formData.append('file', uploadFile.file)
      formData.append('metadata', JSON.stringify(uploadFile.metadata))
      if (uploadFile.thumbnail) {
        formData.append('thumbnail', uploadFile.thumbnail)
      }
      
      // 上传文件
      const response = await uploadApi.uploadMusicFile(formData, (progressEvent) => {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          uploadFile.progress = progress
          uploadFile.uploadedBytes = progressEvent.loaded
          
          // 计算上传速度
          const elapsed = (Date.now() - uploadFile.startedAt) / 1000
          uploadFile.speed = progressEvent.loaded / elapsed
          
          // 计算剩余时间
          const remaining = progressEvent.total - progressEvent.loaded
          uploadFile.remainingTime = remaining / uploadFile.speed
          
          updateTotalProgress()
        }
      )
      
      // 上传成功
      uploadFile.status = 'completed'
      uploadFile.progress = 100
      uploadFile.completedAt = Date.now()
      uploadFile.musicId = response.data.id
      
      // 添加到上传历史
      addToHistory(uploadFile)
      
      // 更新音乐store
      const musicStore = useMusicStore()
      musicStore.myMusics.unshift(response.data)
      
      ElMessage.success(`${uploadFile.name} 上传成功`)
      
    } catch (error) {
      console.error('Upload file error:', error)
      
      uploadFile.status = 'failed'
      uploadFile.error = error.message || '上传失败'
      
      // 重试逻辑
      if (uploadFile.retryCount < settings.value.retryCount) {
        uploadFile.retryCount++
        setTimeout(() => {
          retryUpload(uploadFile.id)
        }, 2000 * uploadFile.retryCount) // 递增延迟重试
      } else {
        addError(`${uploadFile.name} 上传失败: ${uploadFile.error}`)
      }
    } finally {
      if (currentUpload.value?.id === uploadFile.id) {
        currentUpload.value = null
      }
    }
  }
  
  // 暂停上传
  const pauseUpload = () => {
    isPaused.value = true
    uploadingFiles.value.forEach(file => {
      file.status = 'paused'
    })
  }
  
  // 恢复上传
  const resumeUpload = async () => {
    isPaused.value = false
    
    const pausedFiles = uploadQueue.value.filter(file => file.status === 'paused')
    pausedFiles.forEach(file => {
      file.status = 'queued'
    })
    
    if (pausedFiles.length > 0) {
      await startUpload()
    }
  }
  
  // 取消上传
  const cancelUpload = (fileId) => {
    const file = uploadQueue.value.find(f => f.id === fileId)
    if (file) {
      if (file.status === 'uploading') {
        // 取消正在上传的文件
        file.status = 'cancelled'
      } else {
        // 从队列中移除
        const index = uploadQueue.value.findIndex(f => f.id === fileId)
        if (index >= 0) {
          uploadQueue.value.splice(index, 1)
        }
      }
    }
  }
  
  // 重试上传
  const retryUpload = async (fileId) => {
    const file = uploadQueue.value.find(f => f.id === fileId)
    if (file && file.status === 'failed') {
      file.status = 'queued'
      file.progress = 0
      file.uploadedBytes = 0
      file.error = null
      
      if (!isUploading.value) {
        await startUpload()
      }
    }
  }
  
  // 重试所有失败的上传
  const retryAllFailed = async () => {
    failedFiles.value.forEach(file => {
      file.status = 'queued'
      file.progress = 0
      file.uploadedBytes = 0
      file.error = null
    })
    
    if (!isUploading.value) {
      await startUpload()
    }
  }
  
  // 清空队列
  const clearQueue = () => {
    uploadQueue.value = uploadQueue.value.filter(file => 
      file.status === 'uploading' || file.status === 'completed'
    )
  }
  
  // 清空已完成
  const clearCompleted = () => {
    uploadQueue.value = uploadQueue.value.filter(file => file.status !== 'completed')
  }
  
  // 清空失败
  const clearFailed = () => {
    uploadQueue.value = uploadQueue.value.filter(file => file.status !== 'failed')
  }
  
  // 提取音频元数据
  const extractMetadata = async (file) => {
    return new Promise((resolve) => {
      const audio = new Audio()
      const url = URL.createObjectURL(file)
      
      audio.addEventListener('loadedmetadata', () => {
        const metadata = {
          duration: audio.duration,
          title: file.name.replace(/\.[^/.]+$/, ''), // 移除扩展名
          artist: '',
          album: '',
          genre: '',
          year: null,
          track: null,
          bitrate: null,
          sampleRate: null,
          channels: null
        }
        
        URL.revokeObjectURL(url)
        resolve(metadata)
      })
      
      audio.addEventListener('error', () => {
        URL.revokeObjectURL(url)
        resolve(null)
      })
      
      audio.src = url
    })
  }
  
  // 生成缩略图
  const generateThumbnail = async (file) => {
    // 这里可以实现音频波形图生成或专辑封面提取
    // 暂时返回null
    console.log('生成缩略图:', file.name) // 使用file参数避免eslint警告
    return null
  }
  
  // 更新总体进度
  const updateTotalProgress = () => {
    if (totalFiles.value === 0) {
      totalProgress.value = 0
      return
    }
    
    const totalWeight = uploadQueue.value.reduce((total, file) => {
      return total + file.size
    }, 0)
    
    const uploadedWeight = uploadQueue.value.reduce((total, file) => {
      if (file.status === 'completed') {
        return total + file.size
      } else if (file.status === 'uploading') {
        return total + (file.size * file.progress / 100)
      }
      return total
    }, 0)
    
    totalProgress.value = Math.round((uploadedWeight / totalWeight) * 100)
    
    // 计算总体上传速度
    const uploadingSpeeds = uploadingFiles.value.map(file => file.speed || 0)
    uploadSpeed.value = uploadingSpeeds.reduce((total, speed) => total + speed, 0)
    
    // 计算剩余时间
    if (uploadSpeed.value > 0) {
      const remainingBytes = totalWeight - uploadedWeight
      remainingTime.value = remainingBytes / uploadSpeed.value
    } else {
      remainingTime.value = 0
    }
  }
  
  // 添加到历史记录
  const addToHistory = (uploadFile) => {
    const historyItem = {
      id: uploadFile.id,
      name: uploadFile.name,
      size: uploadFile.size,
      musicId: uploadFile.musicId,
      uploadedAt: uploadFile.completedAt,
      duration: uploadFile.completedAt - uploadFile.startedAt
    }
    
    uploadHistory.value.unshift(historyItem)
    
    // 限制历史记录数量
    if (uploadHistory.value.length > 100) {
      uploadHistory.value = uploadHistory.value.slice(0, 100)
    }
  }
  
  // 错误管理
  const addError = (message) => {
    errors.value.push({
      id: generateId(),
      message,
      timestamp: Date.now()
    })
  }
  
  const removeError = (errorId) => {
    const index = errors.value.findIndex(error => error.id === errorId)
    if (index >= 0) {
      errors.value.splice(index, 1)
    }
  }
  
  const clearErrors = () => {
    errors.value = []
  }
  
  // 工具函数
  const generateId = () => {
    return Date.now().toString(36) + Math.random().toString(36).substr(2)
  }
  
  const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  }
  
  const formatTime = (seconds) => {
    if (!seconds || seconds === Infinity) return '--'
    const hours = Math.floor(seconds / 3600)
    const minutes = Math.floor((seconds % 3600) / 60)
    const secs = Math.floor(seconds % 60)
    
    if (hours > 0) {
      return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    } else {
      return `${minutes}:${secs.toString().padStart(2, '0')}`
    }
  }
  
  const formatSpeed = (bytesPerSecond) => {
    return formatFileSize(bytesPerSecond) + '/s'
  }
  
  // 获取文件信息
  const getFileInfo = (fileId) => {
    return uploadQueue.value.find(file => file.id === fileId)
  }
  
  // 获取上传统计
  const getUploadStats = () => {
    return {
      total: totalFiles.value,
      completed: completedCount.value,
      failed: failedCount.value,
      queued: queuedCount.value,
      uploading: uploadingCount.value,
      totalSize: totalSize.value,
      uploadedSize: uploadedSize.value,
      progress: totalProgress.value,
      speed: uploadSpeed.value,
      remainingTime: remainingTime.value
    }
  }
  
  return {
    // 状态
    uploadQueue,
    uploadHistory,
    currentUpload,
    isUploading,
    isPaused,
    totalProgress,
    uploadSpeed,
    remainingTime,
    settings,
    errors,
    
    // 计算属性
    queuedFiles,
    uploadingFiles,
    completedFiles,
    failedFiles,
    pausedFiles,
    totalFiles,
    completedCount,
    failedCount,
    queuedCount,
    uploadingCount,
    totalSize,
    uploadedSize,
    canUpload,
    canPause,
    canResume,
    canRetry,
    hasErrors,
    
    // 方法
    addFiles,
    startUpload,
    pauseUpload,
    resumeUpload,
    cancelUpload,
    retryUpload,
    retryAllFailed,
    clearQueue,
    clearCompleted,
    clearFailed,
    addError,
    removeError,
    clearErrors,
    getFileInfo,
    getUploadStats,
    formatFileSize,
    formatTime,
    formatSpeed
  }
}, {
  persist: {
    key: 'upload',
    storage: localStorage,
    paths: [
      'uploadHistory',
      'settings'
    ]
  }
})