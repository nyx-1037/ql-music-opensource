import request from './request'

// 用户相关API
export const userApi = {
  // 用户注册
  register(data) {
    return request({
      url: '/user/register',
      method: 'post',
      data
    })
  },
  
  // 用户登录
  login(data) {
    return request({
      url: '/user/login',
      method: 'post',
      data
    })
  },
  
  // 获取用户信息
  getUserInfo() {
    return request({
      url: '/user/info',
      method: 'get'
    })
  },
  
  // 更新用户信息
  updateUserInfo(data) {
    return request({
      url: '/user/info',
      method: 'put',
      data
    })
  },
  
  // 修改密码
  changePassword(data) {
    return request({
      url: '/user/password',
      method: 'put',
      data
    })
  },
  
  // 登出
  logout() {
    return request({
      url: '/auth/logout',
      method: 'post'
    })
  }
}

// 音乐相关API
export const musicApi = {
  // 获取音乐列表
  getMusicList(params) {
    return request({
      url: '/music/list',
      method: 'get',
      params
    })
  },
  
  // 获取音乐详情
  getMusicDetail(musicId) {
    return request({
      url: `/music/${musicId}`,
      method: 'get'
    })
  },
  
  // 获取我的音乐
  getMyMusic(params) {
    return request({
      url: '/music/my',
      method: 'get',
      params
    })
  },
  
  // 搜索音乐
  searchMusic(params) {
    return request({
      url: '/music/search',
      method: 'get',
      params
    })
  },
  
  // 上传音乐
  uploadMusic(data) {
    return request({
      url: '/music/upload',
      method: 'post',
      data
    })
  },
  
  // 更新音乐信息
  updateMusic(musicId, data) {
    return request({
      url: `/music/${musicId}`,
      method: 'put',
      data
    })
  },
  
  // 删除音乐
  deleteMusic(musicId) {
    return request({
      url: `/music/${musicId}`,
      method: 'delete'
    })
  },
  
  // 播放音乐（获取音乐流）
  playMusic(musicId) {
    return request({
      url: `/music/${musicId}/stream`,
      method: 'get',
      responseType: 'blob'
    })
  },

  // 获取音乐播放URL
  getMusicUrl(musicId) {
    return request({
      url: `/music/url/${musicId}`,
      method: 'get'
    })
  }
}

// 上传相关API
export const uploadApi = {
  // 上传音乐文件
  uploadMusicFile(formData, onUploadProgress) {
    return request({
      url: '/upload/music',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      timeout: 300000, // 5分钟超时
      onUploadProgress
    })
  }
}

// 默认导出
export default {
  userApi,
  musicApi,
  uploadApi
}