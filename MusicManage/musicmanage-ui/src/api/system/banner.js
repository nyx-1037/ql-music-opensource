import request from '@/utils/request'

// 查询Banner列表
export function listBanner(query) {
  return request({
    url: '/system/qlbanner/list',
    method: 'get',
    params: query
  })
}

// 查询Banner详细
export function getBanner(id) {
  return request({
    url: '/system/qlbanner/' + id,
    method: 'get'
  })
}

// 新增Banner
export function addBanner(data) {
  return request({
    url: '/system/qlbanner',
    method: 'post',
    data: data
  })
}

// 修改Banner
export function updateBanner(data) {
  return request({
    url: '/system/qlbanner',
    method: 'put',
    data: data
  })
}

// 删除Banner
export function delBanner(id) {
  return request({
    url: '/system/qlbanner/' + id,
    method: 'delete'
  })
}

// 修改Banner状态
export function changeBannerStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/qlbanner/changeStatus',
    method: 'put',
    data: data
  })
}