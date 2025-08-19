import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: parseInt(process.env.VUE_APP_API_TIMEOUT) || 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    
    // 添加token到请求头
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    
    // 添加请求时间戳（防止缓存）
    if (config.method === 'get') {
      console.log('utils/request.js - 请求拦截器处理GET请求')
      console.log('utils/request.js - 原始params:', config.params)
      config.params = {
        ...config.params,
        _t: Date.now()
      }
      console.log('utils/request.js - 添加时间戳后的params:', config.params)
    }
    
    // 显示加载状态
    if (config.showLoading !== false) {
      // 可以在这里添加全局loading
    }
    
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {

    
    const res = response.data
    
    // 隐藏加载状态
    // 可以在这里隐藏全局loading
    
    // 如果返回的状态码不是200，说明接口有问题
    if (res.code !== undefined && res.code !== 200) {
      // 处理业务错误
      handleBusinessError(res)
      return Promise.reject(new Error(res.message || 'Error'))
    }
    
    // 成功时返回data字段，这样前端就能直接使用数据
    return res.data
  },
  error => {
    console.error('Response error:', error)
    
    // 隐藏加载状态
    // 可以在这里隐藏全局loading
    
    // 处理HTTP错误
    handleHttpError(error)
    
    return Promise.reject(error)
  }
)

// 处理业务错误
function handleBusinessError(res) {
  const userStore = useUserStore()
  
  switch (res.code) {
    case 401:
      // 未授权，清除token并跳转到登录页
      ElMessage.error('登录已过期，请重新登录')
      userStore.logout()
      router.push('/login')
      break
    case 403:
      ElMessage.error('没有权限访问该资源')
      break
    case 404:
      ElMessage.error('请求的资源不存在')
      break
    case 422:
      // 表单验证错误
      if (res.errors && typeof res.errors === 'object') {
        const errorMessages = Object.values(res.errors).flat()
        ElMessage.error(errorMessages.join(', '))
      } else {
        ElMessage.error(res.message || '请求参数错误')
      }
      break
    case 429:
      ElMessage.error('请求过于频繁，请稍后再试')
      break
    case 500:
      ElMessage.error('服务器内部错误')
      break
    case 4001:
      // Token无效，这是认证检查的正常响应，不显示错误消息
      // 让checkAuth函数自己处理这种情况
      console.log('Token验证失败:', res.message)
      break
    default:
      ElMessage.error(res.message || '未知错误')
  }
}

// 处理HTTP错误
function handleHttpError(error) {
  const userStore = useUserStore()
  
  if (error.response) {
    // 服务器返回了错误状态码
    const { status, data } = error.response
    
    switch (status) {
      case 400:
        ElMessage.error(data?.message || '请求参数错误')
        break
      case 401:
        // Token过期或无效
        if (data?.code === 'TOKEN_EXPIRED') {
          // 尝试刷新token
          return refreshTokenAndRetry(error.config)
        } else {
          ElMessage.error('登录已过期，请重新登录')
          userStore.logout()
          router.push('/login')
        }
        break
      case 403:
        ElMessage.error('没有权限访问该资源')
        break
      case 404:
        ElMessage.error('请求的资源不存在')
        break
      case 408:
        ElMessage.error('请求超时')
        break
      case 429:
        ElMessage.error('请求过于频繁，请稍后再试')
        break
      case 500:
        ElMessage.error('服务器内部错误')
        break
      case 502:
        ElMessage.error('网关错误')
        break
      case 503:
        ElMessage.error('服务暂时不可用')
        break
      case 504:
        ElMessage.error('网关超时')
        break
      default:
        ElMessage.error(data?.message || `请求失败 (${status})`)
    }
  } else if (error.request) {
    // 请求已发出但没有收到响应
    if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.code === 'ERR_NETWORK') {
      ElMessage.error('网络连接失败，请检查网络设置')
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
  } else {
    // 其他错误
    ElMessage.error('请求配置错误')
  }
}

// 刷新token并重试请求
let isRefreshing = false
let failedQueue = []

function processQueue(error, token = null) {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(token)
    }
  })
  
  failedQueue = []
}

function refreshTokenAndRetry(originalRequest) {
  const userStore = useUserStore()
  
  if (isRefreshing) {
    // 如果正在刷新token，将请求加入队列
    return new Promise((resolve, reject) => {
      failedQueue.push({ resolve, reject })
    }).then(token => {
      originalRequest.headers.Authorization = `Bearer ${token}`
      return service(originalRequest)
    })
  }
  
  originalRequest._retry = true
  isRefreshing = true
  
  return new Promise((resolve, reject) => {
    userStore.refreshToken()
      .then(token => {
        originalRequest.headers.Authorization = `Bearer ${token}`
        processQueue(null, token)
        resolve(service(originalRequest))
      })
      .catch(error => {
        processQueue(error, null)
        userStore.logout()
        router.push('/login')
        reject(error)
      })
      .finally(() => {
        isRefreshing = false
      })
  })
}

// 请求方法封装
export const request = {
  get(url, params, config = {}) {
    return service({
      method: 'get',
      url,
      params,
      ...config
    })
  },
  
  post(url, data, config = {}) {
    return service({
      method: 'post',
      url,
      data,
      ...config
    })
  },
  
  put(url, data, config = {}) {
    return service({
      method: 'put',
      url,
      data,
      ...config
    })
  },
  
  delete(url, config = {}) {
    return service({
      method: 'delete',
      url,
      ...config
    })
  },
  
  patch(url, data, config = {}) {
    return service({
      method: 'patch',
      url,
      data,
      ...config
    })
  },
  
  upload(url, formData, config = {}) {
    return service({
      method: 'post',
      url,
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      ...config
    })
  },
  
  download(url, params, config = {}) {
    return service({
      method: 'get',
      url,
      params,
      responseType: 'blob',
      ...config
    })
  }
}

// 取消请求
export const CancelToken = axios.CancelToken
export const isCancel = axios.isCancel

// 创建取消令牌
export function createCancelToken() {
  return CancelToken.source()
}

// 批量请求
export function all(requests) {
  return axios.all(requests)
}

// 并发请求
export function spread(callback) {
  return axios.spread(callback)
}

// 单独导出请求方法
export const get = (url, params) => {
  console.log('utils/request.js - get方法被调用')
  console.log('utils/request.js - url:', url)
  console.log('utils/request.js - params:', params)
  return service.get(url, { params })
}
export const post = service.post
export const put = service.put
export const del = service.delete
export const upload = service.upload

// 默认导出
export default service