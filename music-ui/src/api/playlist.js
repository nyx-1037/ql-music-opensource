import request from './request'

/**
 * 歌单相关API
 */

// 创建歌单
export function createPlaylist(data) {
  return request({
    url: '/playlist',
    method: 'post',
    data
  })
}

// 更新歌单信息
export function updatePlaylist(id, data) {
  return request({
    url: `/playlist/${id}`,
    method: 'put',
    data
  })
}

// 删除歌单
export function deletePlaylist(id) {
  return request({
    url: `/playlist/${id}`,
    method: 'delete'
  })
}

// 分页查询歌单
export function getPlaylists(params) {
  return request({
    url: '/playlist',
    method: 'get',
    params
  })
}

// 根据ID查询歌单详情
export function getPlaylistById(id) {
  return request({
    url: `/playlist/${id}`,
    method: 'get'
  })
}

// 查询用户歌单列表
export function getUserPlaylists(userId, params) {
  return request({
    url: `/playlist/user/${userId}`,
    method: 'get',
    params
  })
}

// 查询当前用户歌单列表
export function getMyPlaylists(params) {
  return request({
    url: '/playlist/my',
    method: 'get',
    params
  })
}

// 添加音乐到歌单
export function addMusicToPlaylist(playlistId, musicId) {
  return request({
    url: `/playlist/${playlistId}/music/${musicId}`,
    method: 'post'
  })
}

// 从歌单移除音乐
export function removeMusicFromPlaylist(playlistId, musicId) {
  return request({
    url: `/playlist/${playlistId}/music/${musicId}`,
    method: 'delete'
  })
}

// 获取歌单中的音乐列表
export function getPlaylistMusics(playlistId, params) {
  return request({
    url: `/playlist/${playlistId}/music`,
    method: 'get',
    params
  })
}

// 增加歌单播放次数
export function incrementPlaylistPlayCount(playlistId) {
  return request({
    url: `/playlist/${playlistId}/play`,
    method: 'post'
  })
}

// 更新歌单音乐排序
export function updatePlaylistMusicOrder(playlistId, data) {
  return request({
    url: `/playlist/${playlistId}/order`,
    method: 'put',
    data
  })
}

// 收藏歌单
export function favoritePlaylist(playlistId) {
  return request({
    url: `/playlist/${playlistId}/favorite`,
    method: 'post'
  })
}

// 取消收藏歌单
export function unfavoritePlaylist(playlistId) {
  return request({
    url: `/playlist/${playlistId}/favorite`,
    method: 'delete'
  })
}

// 检查是否收藏了歌单
export function isPlaylistFavorited(playlistId) {
  return request({
    url: `/playlist/${playlistId}/favorite/status`,
    method: 'get'
  })
}

// 获取收藏的歌单列表
export function getFavoritePlaylists(params) {
  return request({
    url: '/playlist/favorites',
    method: 'get',
    params
  })
}