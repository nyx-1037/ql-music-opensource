import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import relativeTime from 'dayjs/plugin/relativeTime'
import duration from 'dayjs/plugin/duration'

// 配置dayjs
dayjs.locale('zh-cn')
dayjs.extend(relativeTime)
dayjs.extend(duration)

/**
 * 格式化时长（秒转为 mm:ss 或 hh:mm:ss）
 * @param {number} seconds 秒数
 * @returns {string} 格式化后的时长
 */
export function formatDuration(seconds) {
  if (!seconds || seconds < 0) return '00:00'
  
  const duration = dayjs.duration(seconds, 'seconds')
  const hours = Math.floor(duration.asHours())
  const minutes = duration.minutes()
  const secs = duration.seconds()
  
  if (hours > 0) {
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
  }
  
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

/**
 * 格式化播放次数
 * @param {number} count 播放次数
 * @returns {string} 格式化后的播放次数
 */
export function formatPlayCount(count) {
  if (!count || count < 0) return '0'
  
  if (count < 1000) {
    return count.toString()
  } else if (count < 10000) {
    return `${(count / 1000).toFixed(1)}K`
  } else if (count < 100000000) {
    return `${(count / 10000).toFixed(1)}万`
  } else {
    return `${(count / 100000000).toFixed(1)}亿`
  }
}

/**
 * 格式化文件大小
 * @param {number} bytes 字节数
 * @returns {string} 格式化后的文件大小
 */
export function formatFileSize(bytes) {
  if (!bytes || bytes < 0) return '0 B'
  
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(1024))
  
  return `${(bytes / Math.pow(1024, i)).toFixed(2)} ${sizes[i]}`
}

/**
 * 格式化日期
 * @param {string|Date} date 日期
 * @param {string} format 格式
 * @returns {string} 格式化后的日期
 */
export function formatDate(date, format = 'YYYY-MM-DD') {
  if (!date) return ''
  return dayjs(date).format(format)
}

/**
 * 格式化相对时间
 * @param {string|Date} date 日期
 * @returns {string} 相对时间
 */
export function formatRelativeTime(date) {
  if (!date) return ''
  return dayjs(date).fromNow()
}

/**
 * 防抖函数
 * @param {Function} func 要防抖的函数
 * @param {number} wait 等待时间
 * @param {boolean} immediate 是否立即执行
 * @returns {Function} 防抖后的函数
 */
export function debounce(func, wait, immediate = false) {
  let timeout
  
  return function executedFunction(...args) {
    const later = () => {
      timeout = null
      if (!immediate) func.apply(this, args)
    }
    
    const callNow = immediate && !timeout
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
    
    if (callNow) func.apply(this, args)
  }
}

/**
 * 节流函数
 * @param {Function} func 要节流的函数
 * @param {number} limit 时间间隔
 * @returns {Function} 节流后的函数
 */
export function throttle(func, limit) {
  let inThrottle
  
  return function executedFunction(...args) {
    if (!inThrottle) {
      func.apply(this, args)
      inThrottle = true
      setTimeout(() => inThrottle = false, limit)
    }
  }
}

/**
 * 深拷贝
 * @param {any} obj 要拷贝的对象
 * @returns {any} 拷贝后的对象
 */
export function deepClone(obj) {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime())
  if (obj instanceof Array) return obj.map(item => deepClone(item))
  if (typeof obj === 'object') {
    const clonedObj = {}
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clonedObj[key] = deepClone(obj[key])
      }
    }
    return clonedObj
  }
}

/**
 * 生成随机字符串
 * @param {number} length 长度
 * @returns {string} 随机字符串
 */
export function generateRandomString(length = 8) {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

/**
 * 生成UUID
 * @returns {string} UUID
 */
export function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

/**
 * 验证邮箱
 * @param {string} email 邮箱
 * @returns {boolean} 是否有效
 */
export function validateEmail(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

/**
 * 验证手机号
 * @param {string} phone 手机号
 * @returns {boolean} 是否有效
 */
export function validatePhone(phone) {
  const re = /^1[3-9]\d{9}$/
  return re.test(phone)
}

/**
 * 验证密码强度
 * @param {string} password 密码
 * @returns {object} 验证结果
 */
export function validatePassword(password) {
  const result = {
    isValid: false,
    strength: 0,
    message: ''
  }
  
  if (!password) {
    result.message = '密码不能为空'
    return result
  }
  
  if (password.length < 6) {
    result.message = '密码长度至少6位'
    return result
  }
  
  if (password.length > 20) {
    result.message = '密码长度不能超过20位'
    return result
  }
  
  let strength = 0
  
  // 包含小写字母
  if (/[a-z]/.test(password)) strength++
  
  // 包含大写字母
  if (/[A-Z]/.test(password)) strength++
  
  // 包含数字
  if (/\d/.test(password)) strength++
  
  // 包含特殊字符
  if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++
  
  result.strength = strength
  result.isValid = strength >= 2
  
  if (strength === 1) {
    result.message = '密码强度较弱'
  } else if (strength === 2) {
    result.message = '密码强度一般'
  } else if (strength === 3) {
    result.message = '密码强度较强'
  } else if (strength === 4) {
    result.message = '密码强度很强'
  }
  
  return result
}

/**
 * 本地存储工具
 */
export const storage = {
  /**
   * 设置localStorage
   * @param {string} key 键
   * @param {any} value 值
   */
  set(key, value) {
    try {
      localStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error('localStorage set error:', error)
    }
  },
  
  /**
   * 获取localStorage
   * @param {string} key 键
   * @param {any} defaultValue 默认值
   * @returns {any} 值
   */
  get(key, defaultValue = null) {
    try {
      const value = localStorage.getItem(key)
      return value ? JSON.parse(value) : defaultValue
    } catch (error) {
      console.error('localStorage get error:', error)
      return defaultValue
    }
  },
  
  /**
   * 删除localStorage
   * @param {string} key 键
   */
  remove(key) {
    try {
      localStorage.removeItem(key)
    } catch (error) {
      console.error('localStorage remove error:', error)
    }
  },
  
  /**
   * 清空localStorage
   */
  clear() {
    try {
      localStorage.clear()
    } catch (error) {
      console.error('localStorage clear error:', error)
    }
  }
}

/**
 * 会话存储工具
 */
export const sessionStorage = {
  /**
   * 设置sessionStorage
   * @param {string} key 键
   * @param {any} value 值
   */
  set(key, value) {
    try {
      window.sessionStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error('sessionStorage set error:', error)
    }
  },
  
  /**
   * 获取sessionStorage
   * @param {string} key 键
   * @param {any} defaultValue 默认值
   * @returns {any} 值
   */
  get(key, defaultValue = null) {
    try {
      const value = window.sessionStorage.getItem(key)
      return value ? JSON.parse(value) : defaultValue
    } catch (error) {
      console.error('sessionStorage get error:', error)
      return defaultValue
    }
  },
  
  /**
   * 删除sessionStorage
   * @param {string} key 键
   */
  remove(key) {
    try {
      window.sessionStorage.removeItem(key)
    } catch (error) {
      console.error('sessionStorage remove error:', error)
    }
  },
  
  /**
   * 清空sessionStorage
   */
  clear() {
    try {
      window.sessionStorage.clear()
    } catch (error) {
      console.error('sessionStorage clear error:', error)
    }
  }
}

/**
 * 下载文件
 * @param {Blob|string} data 文件数据或URL
 * @param {string} filename 文件名
 */
export function downloadFile(data, filename) {
  const link = document.createElement('a')
  
  if (typeof data === 'string') {
    // URL下载
    link.href = data
  } else {
    // Blob下载
    const url = window.URL.createObjectURL(data)
    link.href = url
  }
  
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  
  if (typeof data !== 'string') {
    window.URL.revokeObjectURL(link.href)
  }
}

/**
 * 复制到剪贴板
 * @param {string} text 要复制的文本
 * @returns {Promise<boolean>} 是否成功
 */
export async function copyToClipboard(text) {
  try {
    if (navigator.clipboard && window.isSecureContext) {
      await navigator.clipboard.writeText(text)
      return true
    } else {
      // 降级方案
      const textArea = document.createElement('textarea')
      textArea.value = text
      textArea.style.position = 'fixed'
      textArea.style.left = '-999999px'
      textArea.style.top = '-999999px'
      document.body.appendChild(textArea)
      textArea.focus()
      textArea.select()
      const result = document.execCommand('copy')
      document.body.removeChild(textArea)
      return result
    }
  } catch (error) {
    console.error('Copy to clipboard failed:', error)
    return false
  }
}

/**
 * 获取文件扩展名
 * @param {string} filename 文件名
 * @returns {string} 扩展名
 */
export function getFileExtension(filename) {
  if (!filename) return ''
  const lastDotIndex = filename.lastIndexOf('.')
  return lastDotIndex !== -1 ? filename.slice(lastDotIndex + 1).toLowerCase() : ''
}

/**
 * 检查是否为音频文件
 * @param {string} filename 文件名
 * @returns {boolean} 是否为音频文件
 */
export function isAudioFile(filename) {
  const audioExtensions = ['mp3', 'wav', 'flac', 'aac', 'ogg', 'm4a', 'wma']
  const extension = getFileExtension(filename)
  return audioExtensions.includes(extension)
}

/**
 * 检查是否为图片文件
 * @param {string} filename 文件名
 * @returns {boolean} 是否为图片文件
 */
export function isImageFile(filename) {
  const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
  const extension = getFileExtension(filename)
  return imageExtensions.includes(extension)
}

/**
 * 获取图片预览URL
 * @param {File} file 文件对象
 * @returns {Promise<string>} 预览URL
 */
export function getImagePreviewUrl(file) {
  return new Promise((resolve, reject) => {
    if (!file || !isImageFile(file.name)) {
      reject(new Error('不是有效的图片文件'))
      return
    }
    
    const reader = new FileReader()
    reader.onload = e => resolve(e.target.result)
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}

/**
 * 压缩图片
 * @param {File} file 图片文件
 * @param {number} quality 压缩质量 0-1
 * @param {number} maxWidth 最大宽度
 * @param {number} maxHeight 最大高度
 * @returns {Promise<Blob>} 压缩后的图片
 */
export function compressImage(file, quality = 0.8, maxWidth = 1920, maxHeight = 1080) {
  return new Promise((resolve, reject) => {
    if (!file || !isImageFile(file.name)) {
      reject(new Error('不是有效的图片文件'))
      return
    }
    
    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d')
    const img = new Image()
    
    img.onload = () => {
      // 计算压缩后的尺寸
      let { width, height } = img
      
      if (width > maxWidth) {
        height = (height * maxWidth) / width
        width = maxWidth
      }
      
      if (height > maxHeight) {
        width = (width * maxHeight) / height
        height = maxHeight
      }
      
      canvas.width = width
      canvas.height = height
      
      // 绘制图片
      ctx.drawImage(img, 0, 0, width, height)
      
      // 转换为Blob
      canvas.toBlob(resolve, file.type, quality)
    }
    
    img.onerror = reject
    img.src = URL.createObjectURL(file)
  })
}

/**
 * 获取音频时长
 * @param {File} file 音频文件
 * @returns {Promise<number>} 时长（秒）
 */
export function getAudioDuration(file) {
  return new Promise((resolve, reject) => {
    if (!file || !isAudioFile(file.name)) {
      reject(new Error('不是有效的音频文件'))
      return
    }
    
    const audio = new Audio()
    
    audio.onloadedmetadata = () => {
      resolve(audio.duration)
      URL.revokeObjectURL(audio.src)
    }
    
    audio.onerror = () => {
      reject(new Error('无法读取音频文件'))
      URL.revokeObjectURL(audio.src)
    }
    
    audio.src = URL.createObjectURL(file)
  })
}

/**
 * 滚动到顶部
 * @param {number} duration 动画时长
 */
export function scrollToTop(duration = 300) {
  const start = window.pageYOffset
  const startTime = performance.now()
  
  function scroll() {
    const now = performance.now()
    const time = Math.min(1, (now - startTime) / duration)
    const timeFunction = 1 - Math.pow(1 - time, 3) // easeOutCubic
    
    window.scroll(0, Math.ceil((1 - timeFunction) * start))
    
    if (time < 1) {
      requestAnimationFrame(scroll)
    }
  }
  
  requestAnimationFrame(scroll)
}

/**
 * 滚动到指定元素
 * @param {string|Element} element 元素或选择器
 * @param {number} offset 偏移量
 * @param {number} duration 动画时长
 */
export function scrollToElement(element, offset = 0, duration = 300) {
  const targetElement = typeof element === 'string' 
    ? document.querySelector(element) 
    : element
    
  if (!targetElement) return
  
  const targetPosition = targetElement.offsetTop + offset
  const startPosition = window.pageYOffset
  const distance = targetPosition - startPosition
  const startTime = performance.now()
  
  function scroll() {
    const now = performance.now()
    const time = Math.min(1, (now - startTime) / duration)
    const timeFunction = 1 - Math.pow(1 - time, 3) // easeOutCubic
    
    window.scroll(0, startPosition + distance * timeFunction)
    
    if (time < 1) {
      requestAnimationFrame(scroll)
    }
  }
  
  requestAnimationFrame(scroll)
}

/**
 * 获取URL参数
 * @param {string} name 参数名
 * @param {string} url URL（可选，默认当前页面URL）
 * @returns {string|null} 参数值
 */
export function getUrlParam(name, url = window.location.href) {
  const urlObj = new URL(url)
  return urlObj.searchParams.get(name)
}

/**
 * 设置URL参数
 * @param {string} name 参数名
 * @param {string} value 参数值
 * @param {boolean} replace 是否替换当前历史记录
 */
export function setUrlParam(name, value, replace = false) {
  const url = new URL(window.location.href)
  url.searchParams.set(name, value)
  
  if (replace) {
    window.history.replaceState({}, '', url.toString())
  } else {
    window.history.pushState({}, '', url.toString())
  }
}

/**
 * 删除URL参数
 * @param {string} name 参数名
 * @param {boolean} replace 是否替换当前历史记录
 */
export function removeUrlParam(name, replace = false) {
  const url = new URL(window.location.href)
  url.searchParams.delete(name)
  
  if (replace) {
    window.history.replaceState({}, '', url.toString())
  } else {
    window.history.pushState({}, '', url.toString())
  }
}