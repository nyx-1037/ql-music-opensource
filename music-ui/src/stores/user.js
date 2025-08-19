import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { updateUserInfo, changePassword as changeUserPassword, getUserInfo, followUser, unfollowUser, getUserFollowers, getUserFollowing } from '@/api/user'
import { authApi } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref(null)
  const token = ref('')
  const refreshToken = ref('')
  const isLoading = ref(false)
  const loginTime = ref(null)
  const lastActiveTime = ref(null)
  
  // 计算属性
  const isLoggedIn = computed(() => {
    return !!token.value && !!user.value
  })
  
  const isTokenValid = computed(() => {
    if (!token.value || !lastActiveTime.value) return false
    
    // 检查 token 是否过期（JWT token 有效期为 2 小时）
    // 基于最后活跃时间而不是登录时间，这样用户活跃时token会保持有效
    const tokenExpireTime = 2 * 60 * 60 * 1000 // 2小时
    const now = Date.now()
    return (now - lastActiveTime.value) < tokenExpireTime
  })
  
  const userInfo = computed(() => {
    if (!user.value) return null
    
    return {
      id: user.value.id,
      username: user.value.username,
      email: user.value.email,
      nickname: user.value.nickname || user.value.username,
      avatar: user.value.avatarUrl || '/default-avatar.png',
      bio: user.value.bio || '',
      gender: user.value.gender || 0, // 0: 未知, 1: 男, 2: 女
      birthday: user.value.birthday || '',
      location: user.value.location || '',
      website: user.value.website || '',
      followersCount: user.value.followersCount || 0,
      followingCount: user.value.followingCount || 0,
      playlistsCount: user.value.playlistsCount || 0,
      favoritesCount: user.value.favoritesCount || 0,
      createdAt: user.value.createdAt,
      updatedAt: user.value.updatedAt,
      isVip: user.value.isVip || false,
      vipExpireTime: user.value.vipExpireTime || null,
      level: user.value.level || 1,
      experience: user.value.experience || 0
    }
  })
  
  // 用户头像计算属性
  const userAvatar = computed(() => {
    return user.value?.avatarUrl || '/default-avatar.png'
  })
  
  // 用户名计算属性
  const userName = computed(() => {
    return user.value?.nickname || user.value?.username || '未登录'
  })
  
  // 方法
  const login = async (credentials) => {
    try {
      isLoading.value = true
      
      const response = await authApi.login(credentials)
      
      // 检查响应是否有效
      if (!response || !response.user || !response.token) {
        throw new Error('登录响应数据格式错误')
      }
      
      const { user: userData, token: userToken, refreshToken: userRefreshToken } = response
      
      // 保存用户信息和 token
      user.value = userData
      token.value = userToken
      refreshToken.value = userRefreshToken
      loginTime.value = Date.now()
      lastActiveTime.value = Date.now()
      
      // 设置 axios 默认 header
      setAuthHeader(userToken)
      
      // 始终保存登录信息到localStorage以支持页面刷新
      localStorage.setItem('user_token', userToken)
      localStorage.setItem('user_refresh_token', userRefreshToken || '')
      localStorage.setItem('user_info', JSON.stringify(userData))
      localStorage.setItem('login_time', loginTime.value.toString())
      localStorage.setItem('last_active_time', lastActiveTime.value.toString())
      
      // 根据记住我选项处理持久化
      if (credentials.remember) {
        localStorage.setItem('remember_me', 'true')
      } else {
        localStorage.setItem('remember_me', 'false')
        // 如果用户没有选择记住我，设置页面关闭时清除登录状态
        console.log('用户未选择记住我，添加unload事件监听器')
        window.addEventListener('unload', () => {
          console.log('login unload事件触发，检查rememberMe状态:', localStorage.getItem('remember_me'))
          if (localStorage.getItem('remember_me') !== 'true') {
            console.log('用户未选择记住我，清除登录状态')
            clearUserData()
          }
        })
      }
      
      ElMessage.success('登录成功')
      
      return response
    } catch (error) {
      console.error('Login error:', error)
      ElMessage.error(error.message || '登录失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  const register = async (userData) => {
    try {
      isLoading.value = true
      
      const response = await authApi.register(userData)
      
      ElMessage.success('注册成功，请登录')
      
      return response
    } catch (error) {
      console.error('Register error:', error)
      ElMessage.error(error.message || '注册失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  const logout = async () => {
    try {
      // 调用登出接口
      if (token.value) {
        await authApi.logout()
      }
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      // 清除本地数据
      clearUserData()
      
      // 清除localStorage中的登录信息
      localStorage.removeItem('user_token')
      localStorage.removeItem('user_refresh_token')
      localStorage.removeItem('user_info')
      localStorage.removeItem('login_time')
      localStorage.removeItem('remember_me')
      
      ElMessage.success('已退出登录')
      
      // 跳转到首页
      router.push({ name: 'Home' })
    }
  }
  
  const refreshTokenAction = async () => {
    try {
      if (!refreshToken.value) {
        throw new Error('No refresh token available')
      }
      
      const response = await authApi.refreshToken({
        refreshToken: refreshToken.value
      })
      
      const { token: newToken, refreshToken: newRefreshToken } = response.data
      
      // 更新 token
      token.value = newToken
      refreshToken.value = newRefreshToken
      loginTime.value = Date.now()
      lastActiveTime.value = Date.now()
      
      // 更新 axios header
      setAuthHeader(newToken)
      
      return response
    } catch (error) {
      console.error('Refresh token error:', error)
      // 刷新失败，清除用户数据
      clearUserData()
      throw error
    }
  }
  
  const updateProfile = async (profileData) => {
    try {
      isLoading.value = true
      
      const response = await updateUserInfo(profileData)
      
      // 更新本地用户信息
      user.value = { ...user.value, ...response.data }
      
      ElMessage.success('个人信息更新成功')
      
      return response
    } catch (error) {
      console.error('Update profile error:', error)
      ElMessage.error(error.message || '更新失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  const updateAvatar = async (avatarFile) => {
    try {
      isLoading.value = true
      
      const formData = new FormData()
      formData.append('avatar', avatarFile)
      
      const response = await updateUserInfo(formData)
      
      // 更新本地头像
      user.value.avatarUrl = response.data.avatarUrl || response.data
      
      ElMessage.success('头像更新成功')
      
      return response
    } catch (error) {
      console.error('Update avatar error:', error)
      ElMessage.error(error.message || '头像更新失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  const changePassword = async (passwordData) => {
    try {
      isLoading.value = true
      
      const response = await changeUserPassword(passwordData)
      
      ElMessage.success('密码修改成功，请重新登录')
      
      // 密码修改成功后需要重新登录
      await logout()
      
      return response
    } catch (error) {
      console.error('Change password error:', error)
      ElMessage.error(error.message || '密码修改失败')
      throw error
    } finally {
      isLoading.value = false
    }
  }
  
  const getUserProfile = async (userId) => {
    try {
      const response = await getUserInfo(userId)
      return response.data
    } catch (error) {
      console.error('Get user profile error:', error)
      throw error
    }
  }
  
  const followUserAction = async (userId) => {
    try {
      const response = await followUser(userId)
      
      ElMessage.success('关注成功')
      
      return response
    } catch (error) {
      console.error('Follow user error:', error)
      ElMessage.error(error.message || '关注失败')
      throw error
    }
  }
  
  const unfollowUserAction = async (userId) => {
    try {
      const response = await unfollowUser(userId)
      
      ElMessage.success('取消关注成功')
      
      return response
    } catch (error) {
      console.error('Unfollow user error:', error)
      ElMessage.error(error.message || '取消关注失败')
      throw error
    }
  }
  
  const getFollowers = async (userId, page = 1, limit = 20) => {
    try {
      const response = await getUserFollowers({ page, limit })
      return response.data
    } catch (error) {
      console.error('Get followers error:', error)
      throw error
    }
  }
  
  const getFollowing = async (userId, page = 1, limit = 20) => {
    try {
      const response = await getUserFollowing({ page, limit })
      return response.data
    } catch (error) {
      console.error('Get following error:', error)
      throw error
    }
  }
  
  const checkAuth = async () => {
    try {
      if (!token.value) {
        console.log('无token，无法进行认证检查')
        clearUserData()
        throw new Error('No token available')
      }
      
      // 检查 token 是否有效
      const response = await authApi.checkAuth()
      
      // 添加调试日志，查看实际响应格式
      console.log('checkAuth响应数据:', response)
      console.log('响应类型:', typeof response)
      
      // 处理后端返回的两种可能格式：
      // 1. 标准格式：{code: 200, data: {user: ...}}
      // 2. 直接格式：{isAuthenticated: true, user: ...}
      let userData = null
      
      if (response && response.code === 200 && response.data && response.data.user) {
        // 标准格式
        userData = response.data.user
        console.log('使用标准响应格式')
      } else if (response && response.isAuthenticated === true && response.user) {
        // 直接格式
        userData = response.user
        console.log('使用直接响应格式')
      }
      
      if (userData) {
        user.value = userData
        lastActiveTime.value = Date.now()
        
        // 始终更新localStorage以确保状态持久化
        localStorage.setItem('user_info', JSON.stringify(user.value))
        localStorage.setItem('last_active_time', lastActiveTime.value.toString())
        
        console.log('认证验证成功，用户信息已更新，最后活跃时间已刷新:', user.value.username)
        return { user: userData }
      } else {
        // 认证失败，但不是网络错误，清除用户数据
        console.log('认证失败，响应格式不正确，清除用户数据')
        console.log('期望: (code=200且data.user存在) 或 (isAuthenticated=true且user存在)，实际:', {
          code: response?.code,
          isAuthenticated: response?.isAuthenticated,
          hasData: !!response?.data,
          hasUser: !!response?.user
        })
        clearUserData()
        throw new Error('Authentication failed')
      }
    } catch (error) {
      console.error('Check auth error:', error)
      
      // 区分错误类型：只有在认证相关错误时才清除用户数据
      // 网络错误、超时等不应该清除用户数据
      if (error.message && error.message.includes('Token无效')) {
        // 后端返回的4001错误，Token无效，清除用户数据但不抛出错误
        console.log('Token无效，清除用户数据')
        clearUserData()
        return null // 返回null表示认证失败但不是错误
      } else if (error.response) {
        // 有响应，说明是服务器返回的错误
        const status = error.response.status
        if (status === 401 || status === 403) {
          // 认证或授权失败，清除用户数据
          console.log('服务器返回认证授权失败(', status, ')，清除用户数据')
          clearUserData()
        } else {
          // 其他HTTP错误（如500等）不清除用户数据
          console.log('服务器错误(', status, ')，保持用户状态')
        }
      } else if (error.code === 'ECONNABORTED' || error.message.includes('timeout')) {
        // 请求超时，不清除用户数据
        console.log('请求超时，保持用户状态')
      } else if (error.message === 'No token available') {
        // 无token的情况已经在上面处理过了
        console.log('无token错误，已清除用户数据')
      } else if (error.message === 'Authentication failed') {
        // 明确的认证失败，已经在上面处理过了
        console.log('认证失败错误，已清除用户数据')
      } else {
        // 网络错误等其他情况，不清除用户数据
        console.log('网络错误或其他错误，保持用户状态:', error.message)
        // 网络错误不抛出异常，返回null但保持用户状态
        return null
      }
      
      // 只有在认证相关错误时才抛出异常
      if (error.message === 'No token available' || 
          error.message === 'Authentication failed' || 
          error.message.includes('Token无效') ||
          (error.response && (error.response.status === 401 || error.response.status === 403))) {
        throw error
      }
      
      // 其他错误（如网络错误）不抛出异常
      return null
    }
  }
  
  const updateLastActiveTime = () => {
    lastActiveTime.value = Date.now()
    // Pinia会自动处理持久化
  }
  
  // 更新用户信息（不调用API，直接更新本地状态）
  const updateUser = (userData) => {
    user.value = { ...user.value, ...userData }
    // Pinia会自动处理持久化
  }
  
  // 辅助方法
  const setAuthHeader = (token) => {
    // axios拦截器会自动从store中获取token，这里不需要额外设置
    // 保留此方法以保持兼容性
    console.log('Token已设置:', token ? '已设置' : '未设置')
  }
  
  const clearUserData = () => {
    console.log('=== 开始清除用户数据 ===')
    console.log('清除前的用户状态:', {
      user: user.value,
      token: token.value,
      isLoggedIn: isLoggedIn.value
    })
    
    user.value = null
    token.value = ''
    refreshToken.value = ''
    isLoading.value = false
    loginTime.value = null
    lastActiveTime.value = null
    
    // 清除记住我标记
    localStorage.removeItem('remember_me')
    
    console.log('用户数据已清除')
  }
  
  // 初始化用户状态（从localStorage恢复）
  const initUserState = () => {
    try {
      console.log('=== 开始初始化用户状态 ===')
      
      const savedToken = localStorage.getItem('user_token')
      const savedUser = localStorage.getItem('user_info')
      const savedLoginTime = localStorage.getItem('login_time')
      const savedRefreshToken = localStorage.getItem('user_refresh_token')
      const savedLastActiveTime = localStorage.getItem('last_active_time')
      const rememberMe = localStorage.getItem('remember_me')
      
      console.log('从localStorage读取的详细数据:', {
        savedToken: savedToken ? `存在(长度:${savedToken.length})` : '不存在',
        savedUser: savedUser ? `存在: ${savedUser}` : '不存在',
        savedLoginTime: savedLoginTime ? `存在: ${savedLoginTime}` : '不存在',
        savedRefreshToken: savedRefreshToken ? `存在(长度:${savedRefreshToken.length})` : '不存在',
        savedLastActiveTime: savedLastActiveTime ? `存在: ${savedLastActiveTime}` : '不存在',
        rememberMe: `值: ${rememberMe}`
      })
      
      console.log('localStorage所有键值对:')
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i)
        const value = localStorage.getItem(key)
        console.log(`  ${key}: ${value}`)
      }
      
      // 如果有保存的登录信息，尝试恢复
      if (savedToken && savedUser && savedLoginTime) {
        token.value = savedToken
        user.value = JSON.parse(savedUser)
        loginTime.value = parseInt(savedLoginTime)
        refreshToken.value = savedRefreshToken || ''
        // 如果没有保存的lastActiveTime，使用当前时间作为最后活跃时间
        lastActiveTime.value = savedLastActiveTime ? parseInt(savedLastActiveTime) : Date.now()
        
        setAuthHeader(savedToken)
        console.log('用户状态已从localStorage恢复，用户:', user.value.username)
        
        // 如果用户没有选择记住我，设置浏览器关闭时清除登录状态
        if (rememberMe !== 'true') {
          console.log('用户未选择记住我，添加unload事件监听器')
          window.addEventListener('unload', () => {
            console.log('initUserState unload事件触发，检查rememberMe状态:', localStorage.getItem('remember_me'))
            if (localStorage.getItem('remember_me') !== 'true') {
              console.log('用户未选择记住我，清除登录状态')
              clearUserData()
            }
          })
        }
        
        // 不在这里检查token有效性，让initAuth方法来处理
        // 这样可以避免在页面刷新时过早清除用户数据
      } else {
        // 没有保存的登录信息
        console.log('没有保存的登录信息，清除状态')
        clearUserData()
      }
    } catch (error) {
      console.error('初始化用户状态失败:', error)
      clearUserData()
    }
  }
  
  // 初始化时检查认证状态
  const initAuth = async () => {
    console.log('=== 开始初始化认证状态 ===')
    console.log('当前token状态:', {
      hasToken: !!token.value,
      tokenLength: token.value ? token.value.length : 0,
      hasRefreshToken: !!refreshToken.value,
      isTokenValid: isTokenValid.value,
      lastActiveTime: lastActiveTime.value
    })
    
    if (token.value) {
      try {
        // 首先检查 token 是否仍然有效（基于时间）
        if (isTokenValid.value) {
          setAuthHeader(token.value)
          console.log('Token时间有效，验证认证状态')
          try {
            const result = await checkAuth()
            if (result === null) {
              console.log('Token验证失败，用户状态已清除')
              return // checkAuth已经处理了用户数据清除
            }
            console.log('Token验证成功，用户状态保持')
          } catch (authError) {
            console.error('认证验证失败:', authError)
            // 只有在明确的认证失败时才清除用户数据
            if (authError.message === 'Authentication failed' || authError.message.includes('Token无效')) {
              console.log('认证失败，清除用户状态')
              clearUserData()
            }
          }
        } else {
          // token 过期，尝试刷新
          console.log('Token已过期，尝试刷新token')
          if (refreshToken.value) {
            try {
              await refreshTokenAction()
              // 刷新成功后再次验证认证状态
              if (token.value) {
                const result = await checkAuth()
                if (result === null) {
                  console.log('刷新后Token验证失败，用户状态已清除')
                  return // checkAuth已经处理了用户数据清除
                }
                console.log('Token刷新并验证成功')
              }
            } catch (refreshError) {
              console.error('Token刷新失败:', refreshError)
              clearUserData()
            }
          } else {
            console.warn('Token过期且无refresh token，清除用户状态')
            clearUserData()
          }
        }
      } catch (error) {
        console.error('Init auth error:', error)
        console.error('错误详情:', error)
        // 只有在明确的认证相关错误时才清除用户数据
        if (error.message === 'No token available' || 
            error.message === 'Authentication failed' || 
            error.message.includes('Token无效')) {
          console.log('认证相关错误，清除用户状态')
          clearUserData()
        } else {
          console.log('网络或其他错误，保持用户状态:', error.message)
        }
      }
    } else {
      console.log('无token，清除用户状态')
      clearUserData()
    }
    console.log('=== 认证状态初始化完成 ===')
  }
  
  // 清除token方法（为了兼容request.js中的调用）
  const clearToken = () => {
    clearUserData()
  }

  return {
    // 状态
    user,
    token,
    isLoading,
    loginTime,
    lastActiveTime,
    
    // 计算属性
    isLoggedIn,
    isTokenValid,
    userInfo,
    userAvatar,
    userName,
    
    // 方法
    login,
    register,
    logout,
    refreshToken: refreshTokenAction,
    updateProfile,
    updateAvatar,
    changePassword,
    getUserProfile,
    followUser: followUserAction,
    unfollowUser: unfollowUserAction,
    getFollowers,
    getFollowing,
    checkAuth,
    updateLastActiveTime,
    updateUser,
    clearUserData,
    clearToken,
    initUserState,
    initAuth
  }
})