import request from '@/utils/request'

// 查询用户关注关系列表
export function listFollow(query) {
  return request({
    url: '/system/follow/list',
    method: 'get',
    params: query
  })
}

// 查询用户关注关系详细
export function getFollow(id) {
  return request({
    url: '/system/follow/' + id,
    method: 'get'
  })
}

// 新增用户关注关系
export function addFollow(data) {
  return request({
    url: '/system/follow',
    method: 'post',
    data: data
  })
}

// 修改用户关注关系
export function updateFollow(data) {
  return request({
    url: '/system/follow',
    method: 'put',
    data: data
  })
}

// 删除用户关注关系
export function delFollow(id) {
  return request({
    url: '/system/follow/' + id,
    method: 'delete'
  })
}
