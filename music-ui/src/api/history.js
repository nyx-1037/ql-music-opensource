import { get, post, del } from '@/utils/request'

// 记录播放历史
const recordPlayHistory = (musicId, playDuration = 0, playPosition = 0) => {
  const params = new URLSearchParams()
  params.append('musicId', musicId)
  if (playDuration > 0) {
    params.append('playDuration', playDuration)
  }
  if (playPosition > 0) {
    params.append('playPosition', playPosition)
  }
  return post('/history/record', params, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

// 获取播放历史
const getPlayHistory = (params) => {
  return get('/history/list', params)
}

// 清空播放历史
const clearPlayHistory = () => {
  return del('/history/clear')
}

// 获取最近播放的音乐
const getRecentPlayMusic = (params) => {
  return get('/history/recent', params)
}

const historyApi = {
  recordPlayHistory,
  getPlayHistory,
  clearPlayHistory,
  getRecentPlayMusic
}

// 导出单个函数供命名导入使用
export {
  recordPlayHistory,
  getPlayHistory,
  clearPlayHistory,
  getRecentPlayMusic
}

// 默认导出
export default historyApi