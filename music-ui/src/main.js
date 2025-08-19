import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'nprogress/nprogress.css'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import relativeTime from 'dayjs/plugin/relativeTime'
import duration from 'dayjs/plugin/duration'

import App from './App.vue'
import router from './router'
import pinia from './stores'
import './styles/index.css'

// 配置dayjs
dayjs.locale('zh-cn')
dayjs.extend(relativeTime)
dayjs.extend(duration)

// 创建应用实例
const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局属性
app.config.globalProperties.$dayjs = dayjs

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  console.error('全局错误:', err)
  console.error('错误信息:', info)
  console.error('组件实例:', vm)
}

// 使用插件
app.use(pinia)
app.use(router)
app.use(ElementPlus, {
  size: 'default',
  zIndex: 3000
})

// 用户状态初始化已移至App.vue中处理

// 挂载应用
app.mount('#app')
