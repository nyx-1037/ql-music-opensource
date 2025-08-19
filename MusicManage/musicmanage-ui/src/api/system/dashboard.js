import request from '@/utils/request'

// 获取概览统计数据
export function getOverviewStats() {
  return request({
    url: '/system/dashboard/overview',
    method: 'get'
  })
}

// 获取用户统计数据
export function getUserStats() {
  return request({
    url: '/system/dashboard/userStats',
    method: 'get'
  })
}

// 获取音乐统计数据
export function getMusicStats() {
  return request({
    url: '/system/dashboard/musicStats',
    method: 'get'
  })
}

// 获取播放统计数据
export function getPlayStats() {
  return request({
    url: '/system/dashboard/playStats',
    method: 'get'
  })
}

// 获取收藏统计数据
export function getFavoriteStats() {
  return request({
    url: '/system/dashboard/favoriteStats',
    method: 'get'
  })
}

// 获取歌单统计数据
export function getPlaylistStats() {
  return request({
    url: '/system/dashboard/playlistStats',
    method: 'get'
  })
}

// 获取分类统计数据
export function getCategoryStats() {
  return request({
    url: '/system/dashboard/categoryStats',
    method: 'get'
  })
}

// 获取关注统计数据
export function getFollowStats() {
  return request({
    url: '/system/dashboard/followStats',
    method: 'get'
  })
}