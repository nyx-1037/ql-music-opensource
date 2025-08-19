import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-plugin-persistedstate'

// 创建 Pinia 实例
const pinia = createPinia()

// 配置持久化插件
pinia.use(
  createPersistedState({
    // 默认使用 localStorage
    storage: localStorage,
    // 序列化函数
    serializer: {
      serialize: JSON.stringify,
      deserialize: JSON.parse
    },
    // 默认持久化所有 store
    auto: true,
    // 键名前缀
    key: id => `music-platform-${id}`
  })
)

export default pinia

// 导出所有 store
export { useUserStore } from './user'
export { usePlayerStore } from './player'
export { useMusicStore } from './music'
export { useUploadStore } from './upload'