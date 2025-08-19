import { get, post, put, del } from '@/utils/request'

// 获取音乐评论列表
const getMusicComments = (musicId, params = {}) => {
  const { page = 1, limit = 10, sortType = 'time', ...otherParams } = params
  return get(`/music/comment/list?musicId=${musicId}&page=${page}&limit=${limit}&sortType=${sortType}`, otherParams)
}

// 获取用户评论列表
const getUserComments = (userId, params) => {
  return get(`/music/comment/user`, params)
}

// 获取评论回复列表
const getCommentReplies = (commentId, params) => {
  return get(`/music/comment/replies/${commentId}`, params)
}

// 添加评论
const addComment = (commentData) => {
  const params = new URLSearchParams()
  params.append('musicId', commentData.musicId)
  params.append('content', commentData.content)
  if (commentData.parentId) {
    params.append('parentId', commentData.parentId)
  }
  return post('/music/comment/add', params, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

// 删除评论
const deleteComment = (commentId) => {
  return del(`/music/comment/${commentId}`)
}

// 点赞评论
const likeComment = (commentId) => {
  return post(`/music/comment/like/${commentId}`)
}

// 取消点赞评论
const unlikeComment = (commentId) => {
  return post(`/music/comment/unlike/${commentId}`)
}

// 回复评论
const replyComment = (commentId, data) => {
  return post(`/music/comment/${commentId}/reply`, data)
}

// 更新评论内容
const updateComment = (commentId, data) => {
  return put(`/music/comment/${commentId}`, data)
}

// 获取评论详情
const getCommentDetail = (commentId) => {
  return get(`/music/comment/detail/${commentId}`)
}

// 获取评论统计信息
const getCommentStats = (musicId) => {
  return get(`/music/comment/stats/${musicId}`)
}

// 获取最新评论列表
const getLatestComments = (params) => {
  return get('/music/comment/latest', params)
}

// 获取热门评论列表
const getHotComments = (musicId, params) => {
  return get(`/music/comment/hot/${musicId}`, params)
}

// 批量删除评论
const batchDeleteComments = (commentIds) => {
  return del('/music/comment/batch', { data: { commentIds } })
}

const commentApi = {
  getMusicComments,
  getUserComments,
  getCommentReplies,
  addComment,
  deleteComment,
  likeComment,
  unlikeComment,
  replyComment,
  updateComment,
  getCommentDetail,
  getCommentStats,
  getLatestComments,
  getHotComments,
  batchDeleteComments
}

// 导出单个函数供命名导入使用
export {
  getMusicComments,
  getUserComments,
  getCommentReplies,
  addComment,
  deleteComment,
  likeComment,
  unlikeComment,
  replyComment,
  updateComment,
  getCommentDetail,
  getCommentStats,
  getLatestComments,
  getHotComments,
  batchDeleteComments,
  commentApi
}

export default commentApi