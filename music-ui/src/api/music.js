import { get, post, put, del, upload } from '@/utils/request'

// 获取音乐列表
const getMusicList = (params) => {
  return get('/music/list', params)
}

// 获取音乐详情
const getMusicDetail = (id) => {
  return get(`/music/${id}`)
}

// 搜索音乐
const searchMusic = (params) => {
  console.log('searchMusic 接收到的原始参数:', params)
  console.log('params类型:', typeof params)
  console.log('params是否为null/undefined:', params == null)
  
  const searchParams = {
    keyword: params.keyword || params.search,
    current: params.current || params.page || 1,
    size: params.size || params.limit || 10
  }
  
  console.log('构建的搜索参数:', searchParams)
  console.log('keyword值:', searchParams.keyword)
  console.log('current值:', searchParams.current)
  console.log('size值:', searchParams.size)
  
  // 验证必需参数
  if (!searchParams.keyword || searchParams.keyword.trim() === '') {
    console.error('搜索关键词为空，无法执行搜索')
    return Promise.reject(new Error('搜索关键词不能为空'))
  }
  
  console.log('即将发送GET请求到 /music/search，参数:', searchParams)
  return get('/music/search', searchParams)
}

// 获取热门音乐
const getHotMusic = (params) => {
  return get('/music/hot', params)
}

// 获取最新音乐
const getLatestMusic = (params) => {
  return get('/music/latest', params)
}

// 获取音乐流派列表
const getMusicGenres = () => {
  return get('/music/genres')
}

// 获取随机推荐音乐
const getRecommendMusic = (params) => {
  return get('/music/recommend', params)
}

// 获取我的音乐
const getMyMusic = (params) => {
  return get('/music/my', params)
}

// 按标题搜索音乐
const searchByTitle = (params) => {
  return get('/music/search/title', params)
}

// 按艺术家搜索音乐
const searchByArtist = (params) => {
  return get('/music/search/artist', params)
}

// 按专辑搜索音乐
const searchByAlbum = (params) => {
  return get('/music/search/album', params)
}

// 上传音乐
const uploadMusic = (formData, onProgress) => {
  return upload('/music/upload', formData, {
    onUploadProgress: (progressEvent) => {
      if (onProgress) {
        const percentCompleted = Math.round(
          (progressEvent.loaded * 100) / progressEvent.total
        )
        onProgress(percentCompleted)
      }
    }
  })
}

// 更新音乐信息
const updateMusic = (id, data) => {
  return put(`/music/${id}`, data)
}

// 删除音乐
const deleteMusic = (id) => {
  return del(`/music/${id}`)
}

// 播放音乐（增加播放次数）
const playMusic = (id) => {
  return post(`/music/play/${id}`)
}

// 获取音乐文件流
const getMusicStream = (id) => {
  return get(`/music/stream/${id}`, {}, { responseType: 'blob' })
}

// 获取音乐播放URL
const getMusicUrl = (id) => {
  return get(`/music/url/${id}`)
}

// 上传音乐封面图片
const uploadMusicCover = (musicId, formData, onProgress) => {
  return upload(`/music/cover/upload/${musicId}`, formData, {
    onUploadProgress: onProgress
  })
}

// 获取音乐封面图片URL
const getMusicCoverUrl = (id) => {
  return `/music/cover/${id}`
}

const musicApi = {
  getMusicList,
  getMusicDetail,
  searchMusic,
  getHotMusic,
  getLatestMusic,
  getMusicGenres,
  getRecommendMusic,
  getMyMusic,
  searchByTitle,
  searchByArtist,
  searchByAlbum,
  uploadMusic,
  updateMusic,
  deleteMusic,
  playMusic,
  getMusicStream,
  getMusicUrl,
  uploadMusicCover,
  getMusicCoverUrl
}

// 导出单个函数供命名导入使用
export {
  getMusicList,
  getMusicDetail,
  searchMusic,
  getHotMusic,
  getLatestMusic,
  getMusicGenres,
  getRecommendMusic,
  getMyMusic,
  searchByTitle,
  searchByArtist,
  searchByAlbum,
  uploadMusic,
  updateMusic,
  deleteMusic,
  playMusic,
  getMusicStream,
  getMusicUrl,
  uploadMusicCover,
  getMusicCoverUrl,
  musicApi
}
export default musicApi