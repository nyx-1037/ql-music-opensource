import request from '@/utils/request'

// 查询歌单音乐关联列表
export function listPlaylistMusic(query) {
  return request({
    url: '/system/playlistMusic/list',
    method: 'get',
    params: query
  })
}

// 查询歌单音乐关联详细
export function getPlaylistMusic(id) {
  return request({
    url: '/system/playlistMusic/' + id,
    method: 'get'
  })
}

// 新增歌单音乐关联
export function addPlaylistMusic(data) {
  return request({
    url: '/system/playlistMusic',
    method: 'post',
    data: data
  })
}

// 修改歌单音乐关联
export function updatePlaylistMusic(data) {
  return request({
    url: '/system/playlistMusic',
    method: 'put',
    data: data
  })
}

// 删除歌单音乐关联
export function delPlaylistMusic(id) {
  return request({
    url: '/system/playlistMusic/' + id,
    method: 'delete'
  })
}