import request from '@/utils/request'

// 查询歌单列表
export function listPlaylist(query) {
  return request({
    url: '/system/playlist/list',
    method: 'get',
    params: query
  })
}

// 查询歌单详细
export function getPlaylist(id) {
  return request({
    url: '/system/playlist/' + id,
    method: 'get'
  })
}

// 新增歌单
export function addPlaylist(data) {
  return request({
    url: '/system/playlist',
    method: 'post',
    data: data
  })
}

// 修改歌单
export function updatePlaylist(data) {
  return request({
    url: '/system/playlist',
    method: 'put',
    data: data
  })
}

// 删除歌单
export function delPlaylist(id) {
  return request({
    url: '/system/playlist/' + id,
    method: 'delete'
  })
}
