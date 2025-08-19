import Cookies from 'js-cookie'

const TokenKey = 'Music-Token'

/**
 * 获取Token
 * @returns {string|undefined}
 */
export function getToken() {
  return Cookies.get(TokenKey)
}

/**
 * 设置Token
 * @param {string} token
 * @returns {*}
 */
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

/**
 * 移除Token
 * @returns {*}
 */
export function removeToken() {
  return Cookies.remove(TokenKey)
}

/**
 * 检查是否已登录
 * @returns {boolean}
 */
export function isLoggedIn() {
  return !!getToken()
}

/**
 * 检查是否为管理员
 * @returns {boolean}
 */
export function isAdmin() {
  // 这里可以根据实际需求扩展，比如从token中解析用户角色
  // 目前简单判断是否有token
  return isLoggedIn()
}