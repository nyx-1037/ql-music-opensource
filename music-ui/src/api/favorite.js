import { get, post, del } from '@/utils/request'

// 添加收藏
const addFavorite = (musicId) => {
  return post(`/music/favorite/add/${musicId}`)
}

// 取消收藏
const removeFavorite = (musicId) => {
  return del(`/music/favorite/remove/${musicId}`)
}

// 检查是否已收藏
const isFavorite = (musicId) => {
  return get(`/music/favorite/check/${musicId}`)
}

// 获取收藏列表
const getFavoriteList = (params) => {
  return get('/music/favorite/list', params)
}

const favoriteApi = {
  addFavorite,
  removeFavorite,
  isFavorite,
  getFavoriteList
}

// 导出单个函数供命名导入使用
export {
  addFavorite,
  removeFavorite,
  isFavorite,
  getFavoriteList
}

// 默认导出
export default favoriteApi