import request from '@/utils/request'

/**
 * 客户端用户管理API
 * 用于解决若依系统用户表冲突问题
 */

// 查询客户端用户列表
export function listQlUser(query) {
  return request({
    url: '/system/qluser/list',
    method: 'get',
    params: query
  })
}

// 查询客户端用户详细信息
export function getQlUser(id) {
  return request({
    url: '/system/qluser/' + id,
    method: 'get'
  })
}

// 新增客户端用户
export function addQlUser(data) {
  return request({
    url: '/system/qluser',
    method: 'post',
    data: data
  })
}

// 修改客户端用户
export function updateQlUser(data) {
  return request({
    url: '/system/qluser',
    method: 'put',
    data: data
  })
}

// 删除客户端用户
export function delQlUser(id) {
  return request({
    url: '/system/qluser/' + id,
    method: 'delete'
  })
}

// 导出客户端用户数据
export function exportQlUser(query) {
  return request({
    url: '/system/qluser/export',
    method: 'get',
    params: query
  })
}

// 重置客户端用户密码
export function resetQlUserPwd(id, password) {
  const data = {
    id,
    password
  }
  return request({
    url: '/system/qluser/resetPwd',
    method: 'put',
    data: data
  })
}

// 修改客户端用户状态
export function changeQlUserStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/qluser/changeStatus',
    method: 'put',
    data: data
  })
}