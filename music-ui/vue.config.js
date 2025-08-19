const { defineConfig } = require('@vue/cli-service')

// 获取后端服务地址，支持环境变量配置
const getBackendUrl = () => {
  // 优先使用环境变量中的后端地址
  if (process.env.VUE_APP_BACKEND_URL) {
    return process.env.VUE_APP_BACKEND_URL
  }
  // 默认使用localhost:8081
  return 'http://localhost:8081'
}

module.exports = defineConfig({
  transpileDependencies: true,
  // 配置公共路径，支持部署到子目录
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  devServer: {
    // 开发服务器端口，支持环境变量配置
    port: process.env.VUE_APP_DEV_PORT || 8080,
    host: '0.0.0.0', // 允许外部访问
    // 代理配置 - 将/api请求代理到后端服务
    proxy: {
      '/api': {
        target: getBackendUrl(),
        changeOrigin: true,
        secure: false, // 如果是https接口，需要配置这个参数
        logLevel: 'debug', // 显示代理日志
        pathRewrite: {
          // 保持/api前缀，因为后端接口就是以/api开头
          '^/api': '/api'
        },
        // 代理错误处理
        onError: (err, req, res) => {
          console.error('代理错误:', err)
        },
        // 代理请求日志
        onProxyReq: (proxyReq, req, res) => {
          console.log('代理请求:', req.method, req.url, '->', getBackendUrl() + req.url)
        }
      }
    }
  },
  chainWebpack: config => {
    config.plugin('define').tap(definitions => {
      Object.assign(definitions[0], {
        __VUE_OPTIONS_API__: 'true',
        __VUE_PROD_DEVTOOLS__: 'false',
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: 'false'
      })
      return definitions
    })
  }
})
