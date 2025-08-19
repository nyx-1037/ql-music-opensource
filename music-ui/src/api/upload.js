/**
 * 文件上传相关API
 * @author nyx
 * @since 2024-12
 */
import request from './request'

/**
 * 上传音乐文件
 * @param {FormData} formData 包含音乐文件的表单数据
 * @param {Function} onUploadProgress 上传进度回调
 * @returns {Promise} 上传结果
 */
export const uploadMusic = (formData, onUploadProgress) => {
  return request({
    url: '/music/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress
  })
}

/**
 * 上传封面图片
 * @param {FormData} formData 包含图片文件的表单数据
 * @param {Function} onUploadProgress 上传进度回调
 * @returns {Promise} 上传结果
 */
export const uploadCover = (formData, onUploadProgress) => {
  return request({
    url: '/upload/cover',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress
  })
}