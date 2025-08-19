# music-ui

## 项目安装
```bash
npm install
```

## 开发环境启动

### 默认端口启动 (8080)
```bash
npm run serve
```

### 多端口启动
```bash
# 端口 3000
npm run serve:3000

# 端口 9000
npm run serve:9000

# 自定义端口
VUE_APP_DEV_PORT=4000 npm run serve
```

### 使用启动脚本 (Windows)
```bash
# 运行多端口启动脚本
start-multi-port.bat
```

## 生产环境构建
```bash
# 生产环境构建
npm run build

# 开发模式构建
npm run build:dev

# 本地预览构建结果
npm run preview
```

## 代码检查
```bash
npm run lint
```

## 部署配置

### 环境变量说明
- `VUE_APP_API_BASE_URL`: API基础路径
- `VUE_APP_BACKEND_URL`: 后端服务地址（开发环境代理用）
- `VUE_APP_DEV_PORT`: 开发服务器端口

### 代理配置
开发环境下，所有 `/api` 请求会自动代理到 `http://localhost:8081`

详细部署说明请参考 [DEPLOYMENT.md](./DEPLOYMENT.md)

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
