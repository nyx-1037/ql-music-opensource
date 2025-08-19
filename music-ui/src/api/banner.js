import { get, post, put, del } from './request'

/**
 * Banner相关API
 */
export default {
  /**
   * 获取启用状态的Banner列表
   * @returns {Promise}
   */
  getActiveBanners() {
    return get('/banner/active')
  },

  /**
   * 获取所有Banner列表（管理员接口）
   * @returns {Promise}
   */
  getAllBanners() {
    return get('/banner/all')
  },

  /**
   * 创建Banner
   * @param {Object} banner - Banner数据
   * @returns {Promise}
   */
  createBanner(banner) {
    return post('/banner', banner)
  },

  /**
   * 更新Banner
   * @param {number} id - Banner ID
   * @param {Object} banner - Banner数据
   * @returns {Promise}
   */
  updateBanner(id, banner) {
    return put(`/banner/${id}`, banner)
  },

  /**
   * 删除Banner
   * @param {number} id - Banner ID
   * @returns {Promise}
   */
  deleteBanner(id) {
    return del(`/banner/${id}`)
  },

  /**
   * 更新Banner状态
   * @param {number} id - Banner ID
   * @param {number} status - 状态(0:禁用 1:启用)
   * @returns {Promise}
   */
  updateBannerStatus(id, status) {
    return put(`/banner/${id}/status?status=${status}`)
  }
}