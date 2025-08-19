import { get, post, put, del } from './request'

// 获取用户信息
export const getUserInfo = (userId = null) => {
  if (userId) {
    return get(`/user/info/${userId}`)
  }
  return get('/user/info')
}

// 更新用户信息
export const updateUserInfo = (data) => {
  return put('/user/update', data)
}

// 上传用户头像
export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return post('/user/upload-avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 修改密码
export const changePassword = (data) => {
  return put('/user/password', data)
}

// 获取用户关注列表
export const getUserFollowing = (params) => {
  return get(`/user/following/${params.userId}`)
}

// 获取用户粉丝列表
export const getUserFollowers = (params) => {
  return get(`/user/followers/${params.userId}`)
}

// 关注用户
export const followUser = (userId) => {
  return post(`/user/follow/${userId}`)
}

// 取消关注用户
export const unfollowUser = (userId) => {
  return del(`/user/follow/${userId}`)
}

// 检查是否已关注用户
export const checkUserFollowed = (userId) => {
  return get(`/user/follow/status/${userId}`)
}

// 获取用户关注数量
export const getUserFollowingCount = () => {
  return get('/user/following/count')
}

// 获取用户粉丝数量
export const getUserFollowersCount = () => {
  return get('/user/followers/count')
}

// 搜索用户
export const searchUsers = (params) => {
  return get('/user/search', params)
}

// 获取推荐用户
export const getRecommendUsers = (params) => {
  return get('/user/recommend', params)
}

// 获取活跃用户
export const getActiveUsers = (params) => {
  return get('/user/active', params)
}

// 获取新用户
export const getNewUsers = (params) => {
  return get('/user/new', params)
}

// 获取用户详情（公开信息）
export const getUserDetail = (userId) => {
  return get(`/user/${userId}`)
}

// 获取用户公开播放列表
export const getUserPublicPlaylists = (userId, params) => {
  return get(`/user/${userId}/playlists`, params)
}

// 获取用户公开收藏
export const getUserPublicFavorites = (userId, params) => {
  return get(`/user/${userId}/favorites`, params)
}

// 获取用户最近播放（公开）
export const getUserPublicRecentPlayed = (userId, params) => {
  return get(`/user/${userId}/recent-played`, params)
}

// 举报用户
export const reportUser = (userId, reason) => {
  return post(`/user/${userId}/report`, { reason })
}

// 拉黑用户
export const blockUser = (userId) => {
  return post(`/user/${userId}/block`)
}

// 取消拉黑用户
export const unblockUser = (userId) => {
  return del(`/user/${userId}/block`)
}

// 获取拉黑用户列表
export const getBlockedUsers = (params) => {
  return get('/user/blocked', params)
}

// 检查用户是否被拉黑
export const checkUserBlocked = (userId) => {
  return get(`/user/${userId}/blocked`)
}

// 获取用户通知
export const getUserNotifications = (params) => {
  return get('/user/notifications', params)
}

// 标记通知为已读
export const markNotificationAsRead = (notificationId) => {
  return put(`/user/notifications/${notificationId}/read`)
}

// 标记所有通知为已读
export const markAllNotificationsAsRead = () => {
  return put('/user/notifications/read-all')
}

// 删除通知
export const deleteNotification = (notificationId) => {
  return del(`/user/notifications/${notificationId}`)
}

// 清空所有通知
export const clearAllNotifications = () => {
  return del('/user/notifications/all')
}

// 获取未读通知数量
export const getUnreadNotificationCount = () => {
  return get('/user/notifications/unread/count')
}

// 更新通知设置
export const updateNotificationSettings = (data) => {
  return put('/user/notification-settings', data)
}

// 获取通知设置
export const getNotificationSettings = () => {
  return get('/user/notification-settings')
}

// 获取用户偏好设置
export const getUserPreferences = () => {
  return get('/user/preferences')
}

// 更新用户偏好设置
export const updateUserPreferences = (data) => {
  return put('/user/preferences', data)
}

// 获取用户隐私设置
export const getUserPrivacySettings = () => {
  return get('/user/privacy-settings')
}

// 更新用户隐私设置
export const updateUserPrivacySettings = (data) => {
  return put('/user/privacy-settings', data)
}

// 注销账户
export const deleteAccount = (password) => {
  return del('/user/account', { data: { password } })
}

// 导出用户数据
export const exportUserData = () => {
  return get('/user/export')
}

// 获取用户登录历史
export const getUserLoginHistory = (params) => {
  return get('/user/login-history', params)
}

// 获取用户在线状态
export const getUserOnlineStatus = (userId) => {
  return get(`/user/${userId}/online-status`)
}

// 更新用户在线状态
export const updateUserOnlineStatus = (status) => {
  return put('/user/online-status', { status })
}