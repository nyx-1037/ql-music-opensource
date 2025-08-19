import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

// 路由组件懒加载
const Home = () => import('@/views/Home.vue')
const Profile = () => import('@/views/Profile.vue')
const MusicDetail = () => import('@/views/MusicDetail.vue')
const Music = () => import('@/views/Music.vue')

const Login = () => import('@/views/Login.vue')
const PlayHistory = () => import('@/views/PlayHistory.vue')
const Settings = () => import('@/views/Settings.vue')
const Playlist = () => import('@/views/Playlist.vue')
const PlaylistDetail = () => import('@/views/PlaylistDetail.vue')
const BannerManagement = () => import('@/views/admin/BannerManagement.vue')
const NotFound = () => import('@/views/NotFound.vue')

// 路由配置
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '首页',
      requiresAuth: false,
      keepAlive: true
    }
  },
  {
    path: '/music',
    name: 'Music',
    component: Music,
    meta: {
      title: '音乐库',
      requiresAuth: false,
      keepAlive: true
    }
  },
  {
    path: '/profile/:id?',
    name: 'Profile',
    component: Profile,
    meta: {
      title: '个人中心',
      requiresAuth: false,
      keepAlive: false
    },
    props: true
  },

  {
    path: '/music/:id',
    name: 'MusicDetail',
    component: MusicDetail,
    meta: {
      title: '音乐详情',
      requiresAuth: false,
      keepAlive: false
    },
    props: true
  },


  {
    path: '/history',
    name: 'PlayHistory',
    component: PlayHistory,
    meta: {
      title: '播放历史',
      requiresAuth: true,
      keepAlive: false
    }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: {
      title: '设置',
      requiresAuth: true,
      keepAlive: false
    }
  },
  {
    path: '/playlist',
    name: 'Playlist',
    component: Playlist,
    meta: {
      title: '我的歌单',
      requiresAuth: true,
      keepAlive: false
    }
  },
  {
    path: '/playlist/:id',
    name: 'PlaylistDetail',
    component: PlaylistDetail,
    meta: {
      title: '歌单详情',
      requiresAuth: false,
      keepAlive: false
    },
    props: true
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      title: '登录',
      requiresAuth: false,
      keepAlive: false,
      hideLayout: true
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: Login,
    meta: {
      title: '注册',
      requiresAuth: false,
      keepAlive: false,
      hideLayout: true
    }
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: Login,
    meta: {
      title: '忘记密码',
      requiresAuth: false,
      keepAlive: false,
      hideLayout: true
    }
  },
  {
    path: '/admin/banner',
    name: 'BannerManagement',
    component: BannerManagement,
    meta: {
      title: 'Banner管理',
      requiresAuth: true,
      keepAlive: false
    }
  },
  // 重定向路由
  {
    path: '/user/:id',
    redirect: to => {
      return { name: 'Profile', params: { id: to.params.id } }
    }
  },
  {
    path: '/song/:id',
    redirect: to => {
      return { name: 'MusicDetail', params: { id: to.params.id } }
    }
  },
  // 404页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound,
    meta: {
      title: '页面不存在',
      requiresAuth: false,
      keepAlive: false
    }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // 如果有保存的位置，返回到该位置
    if (savedPosition) {
      return savedPosition
    }
    // 如果有锚点，滚动到锚点
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth'
      }
    }
    // 否则滚动到顶部
    return { top: 0 }
  }
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  const title = to.meta.title
  if (title) {
    document.title = `${title} - ${process.env.VUE_APP_TITLE}`
  } else {
    document.title = process.env.VUE_APP_TITLE
  }
  
  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    if (!userStore.isLoggedIn) {
      // 未登录，跳转到登录页
      ElMessage.warning('请先登录')
      next({
        name: 'Login',
        query: { redirect: to.fullPath }
      })
      return
    }
    
    // 已登录但token可能过期，验证token
    if (userStore.token && !userStore.isTokenValid) {
      try {
        // 尝试刷新token
        await userStore.refreshToken()
      } catch (error) {
        // 刷新失败，跳转到登录页
        ElMessage.error('登录已过期，请重新登录')
        userStore.logout()
        next({
          name: 'Login',
          query: { redirect: to.fullPath }
        })
        return
      }
    }
  }
  
  // 如果已登录用户访问登录页，重定向到首页
  if (to.name === 'Login' && userStore.isLoggedIn) {
    const redirect = to.query.redirect
    if (redirect && typeof redirect === 'string') {
      next(redirect)
    } else {
      next({ name: 'Home' })
    }
    return
  }
  
  // 检查路由参数
  if (to.name === 'Profile' && !to.params.id) {
    // 如果访问个人中心但没有指定用户ID，使用当前用户ID
    if (userStore.isLoggedIn) {
      next({
        name: 'Profile',
        params: { id: userStore.user.id }
      })
      return
    } else {
      // 未登录用户访问个人中心，跳转到登录页
      next({
        name: 'Login',
        query: { redirect: to.fullPath }
      })
      return
    }
  }
  
  next()
})

// 全局后置守卫
router.afterEach((to, from) => {
  // 页面切换完成后的处理
  // 可以在这里添加页面访问统计等
  
  // 如果是开发环境，打印路由信息
  if (process.env.NODE_ENV === 'development') {
    console.log(`Route changed: ${from.path} -> ${to.path}`)
  }
})

// 路由错误处理
router.onError((error) => {
  console.error('Router error:', error)
  ElMessage.error('页面加载失败，请刷新重试')
})

export default router

// 导出路由配置供其他地方使用
export { routes }

// 路由工具函数
export const routeUtils = {
  /**
   * 检查当前路由是否需要认证
   */
  requiresAuth(route = router.currentRoute.value) {
    return route.meta?.requiresAuth === true
  },
  
  /**
   * 检查当前路由是否隐藏布局
   */
  hideLayout(route = router.currentRoute.value) {
    return route.meta?.hideLayout === true
  },
  
  /**
   * 检查当前路由是否需要缓存
   */
  keepAlive(route = router.currentRoute.value) {
    return route.meta?.keepAlive === true
  },
  
  /**
   * 获取面包屑导航
   */
  getBreadcrumbs(route = router.currentRoute.value) {
    const breadcrumbs = []
    
    // 根据路由名称生成面包屑
    switch (route.name) {
      case 'Home':
        breadcrumbs.push({ name: '首页', path: '/' })
        break
      case 'Discover':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '发现音乐', path: '/discover' })
        break
      case 'Playlists':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '播放列表', path: '/playlists' })
        break
      case 'PlaylistDetail':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '播放列表', path: '/playlists' })
        breadcrumbs.push({ name: '播放列表详情', path: route.path })
        break
      case 'Favorites':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '我的收藏', path: '/favorites' })
        break
      case 'Profile':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '个人中心', path: route.path })
        break
      case 'Settings':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '个人中心', path: `/profile/${route.params.id || ''}` })
        breadcrumbs.push({ name: '设置', path: '/settings' })
        break
      case 'MusicDetail':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '音乐详情', path: route.path })
        break
      case 'Search':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '搜索', path: '/search' })
        break
      case 'Upload':
        breadcrumbs.push({ name: '首页', path: '/' })
        breadcrumbs.push({ name: '上传音乐', path: '/upload' })
        break
      default:
        breadcrumbs.push({ name: '首页', path: '/' })
    }
    
    return breadcrumbs
  },
  
  /**
   * 跳转到登录页
   */
  toLogin(redirect) {
    router.push({
      name: 'Login',
      query: redirect ? { redirect } : {}
    })
  },
  
  /**
   * 跳转到首页
   */
  toHome() {
    router.push({ name: 'Home' })
  },
  
  /**
   * 返回上一页
   */
  goBack() {
    if (window.history.length > 1) {
      router.go(-1)
    } else {
      router.push({ name: 'Home' })
    }
  }
}