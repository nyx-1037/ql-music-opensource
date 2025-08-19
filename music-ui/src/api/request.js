import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'
import NProgress from 'nprogress'

// 创建axios实例
const request = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081/api',
  timeout: 30000, // 30秒超时
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 显示加载进度条
    NProgress.start()
    
    // 添加token到请求头
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    
    // 如果是FormData，设置正确的Content-Type
    if (config.data instanceof FormData) {
      config.headers['Content-Type'] = 'multipart/form-data'
    }
    
    return config
  },
  error => {
    NProgress.done()
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    NProgress.done()
    
    const { data } = response
    
    // 添加调试日志
    console.log('request.js - 响应数据:', data)
    console.log('request.js - 响应数据类型:', typeof data)
    console.log('request.js - 响应状态码:', data?.code)
    
    // 如果是文件下载，直接返回response
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // 正常响应
    if (data.code === 200) {
      console.log('request.js - 返回数据:', data.data)
      return data.data
    }
    
    // 业务错误
    if (data.code === 401) {
      // token过期或无效
      handleTokenExpired()
      return Promise.reject(new Error(data.message || '登录已过期'))
    }
    
    if (data.code === 403) {
      ElMessage.error(data.message || '权限不足')
      return Promise.reject(new Error(data.message || '权限不足'))
    }
    
    // 其他业务错误
    const errorMessage = data.message || '请求失败'
    ElMessage.error(errorMessage)
    return Promise.reject(new Error(errorMessage))
  },
  async error => {
    NProgress.done()
    
    console.error('响应错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 400:
          ElMessage.error(data.message || '请求参数错误')
          break
        case 401:
          // token过期或无效
          handleTokenExpired()
          break
        case 403:
          ElMessage.error(data.message || '权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error(data.message || '服务器内部错误')
          break
        case 502:
          ElMessage.error('网关错误')
          break
        case 503:
          ElMessage.error('服务不可用')
          break
        case 504:
          ElMessage.error('网关超时')
          break
        default:
          ElMessage.error(data.message || `请求失败 (${status})`)
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.message === 'Network Error') {
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      ElMessage.error(error.message || '请求失败')
    }
    
    return Promise.reject(error)
  }
)

// 处理token过期
const handleTokenExpired = () => {
  const userStore = useUserStore()
  
  // 清除用户信息
  userStore.clearToken()
  
  ElMessageBox.confirm(
    '登录已过期，请重新登录',
    '提示',
    {
      confirmButtonText: '重新登录',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    router.push({
      path: '/login',
      query: { redirect: router.currentRoute.value.fullPath }
    })
  }).catch(() => {
    // 用户取消，跳转到首页
    router.push('/')
  })
}

// 封装常用请求方法
export const get = (url, params = {}, config = {}) => {
  console.log('request.js - get方法被调用')
  console.log('request.js - url:', url)
  console.log('request.js - params:', params)
  console.log('request.js - params类型:', typeof params)
  console.log('request.js - config:', config)
  
  const requestConfig = {
    method: 'GET',
    url,
    params,
    ...config
  }
  
  console.log('request.js - 最终请求配置:', requestConfig)
  return request(requestConfig)
}

export const post = (url, data = {}, config = {}) => {
  return request({
    method: 'POST',
    url,
    data,
    ...config
  })
}

export const put = (url, data = {}, config = {}) => {
  return request({
    method: 'PUT',
    url,
    data,
    ...config
  })
}

export const del = (url, config = {}) => {
  return request({
    method: 'DELETE',
    url,
    ...config
  })
}

export const upload = (url, formData, config = {}) => {
  return request({
    method: 'POST',
    url,
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    ...config
  })
}

export const download = (url, params = {}, config = {}) => {
  return request({
    method: 'GET',
    url,
    params,
    responseType: 'blob',
    ...config
  })
}

export default request