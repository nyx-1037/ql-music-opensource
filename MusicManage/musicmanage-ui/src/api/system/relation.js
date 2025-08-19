import request from '@/utils/request'

// 查询音乐分类关联列表
export function listRelation(query) {
  return request({
    url: '/system/relation/list',
    method: 'get',
    params: query
  })
}

// 查询音乐分类关联详细
export function getRelation(id) {
  return request({
    url: '/system/relation/' + id,
    method: 'get'
  })
}

// 新增音乐分类关联
export function addRelation(data) {
  return request({
    url: '/system/relation',
    method: 'post',
    data: data
  })
}

// 修改音乐分类关联
export function updateRelation(data) {
  return request({
    url: '/system/relation',
    method: 'put',
    data: data
  })
}

// 删除音乐分类关联
export function delRelation(id) {
  return request({
    url: '/system/relation/' + id,
    method: 'delete'
  })
}
