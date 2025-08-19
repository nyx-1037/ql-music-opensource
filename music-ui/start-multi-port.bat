@echo off
chcp 65001
echo ====================================
echo     音乐平台前端多端口启动脚本
echo ====================================
echo.
echo 请选择启动方式：
echo 1. 默认端口 8080
echo 2. 端口 3000
echo 3. 端口 9000
echo 4. 自定义端口
echo 5. 查看当前配置
echo 0. 退出
echo.
set /p choice=请输入选择 (0-5): 

if "%choice%"=="1" (
    echo 正在启动前端服务 (端口: 8080)...
    npm run serve
) else if "%choice%"=="2" (
    echo 正在启动前端服务 (端口: 3000)...
    npm run serve:3000
) else if "%choice%"=="3" (
    echo 正在启动前端服务 (端口: 9000)...
    npm run serve:9000
) else if "%choice%"=="4" (
    set /p custom_port=请输入自定义端口: 
    echo 正在启动前端服务 (端口: %custom_port%)...
    set VUE_APP_DEV_PORT=%custom_port%
    npm run serve
) else if "%choice%"=="5" (
    echo.
    echo 当前配置信息：
    echo - 后端服务地址: http://localhost:8081
    echo - API基础路径: /api
    echo - 代理配置: 已启用
    echo.
    pause
    goto :start
) else if "%choice%"=="0" (
    echo 退出脚本
    exit /b 0
) else (
    echo 无效选择，请重新输入
    pause
    goto :start
)

:start
goto :eof