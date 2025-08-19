import { get, post } from './request'

// 用户登录
export const login = (data) => {
  return post('/user/login', data)
}

// 用户注册
export const register = (data) => {
  return post('/user/register', data)
}

// 获取用户信息
export const getUserInfo = () => {
  return get('/user/info')
}

// 用户登出
export const logout = () => {
  return post('/user/logout')
}

// 刷新token
export const refreshToken = (data) => {
  return post('/user/refresh-token', data)
}

// 检查认证状态
export const checkAuth = () => {
  return get('/user/check-auth')
}

// 导出 authApi 对象
export const authApi = {
  login,
  register,
  getUserInfo,
  logout,
  refreshToken,
  checkAuth
}