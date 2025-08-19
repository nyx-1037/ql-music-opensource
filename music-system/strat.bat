@echo off
REM 文件名: strat.bat
REM Spring Boot 项目启动脚本 (Windows)

echo ========================================
echo 七洛音乐平台系统启动脚本
echo ========================================

REM 1. 清理项目
echo [1/4] 正在清理项目...
call mvn clean
if %errorlevel% neq 0 (
    echo 错误：项目清理失败！
    pause
    exit /b 1
)

REM 2. 打包项目（跳过测试）
echo [2/4] 正在打包项目...
call mvn package -DskipTests
if %errorlevel% neq 0 (
    echo 错误：项目打包失败！
    pause
    exit /b 1
)

REM 3. 查找生成的JAR文件
echo [3/4] 正在查找JAR文件...
set JAR_FILE=
for /f "delims=" %%i in ('dir /b target\*.jar 2^>nul ^| findstr /v sources') do (
    set JAR_FILE=target\%%i
    goto :found
)

if "%JAR_FILE%"=="" (
    echo 错误：未找到可执行的JAR文件！
    echo 请确保项目已正确打包。
    pause
    exit /b 1
)

:found
echo 找到可执行JAR: %JAR_FILE%

REM 4. 启动Spring Boot应用
echo [4/4] 正在启动Spring Boot应用...
echo ========================================
echo 应用启动中，请稍候...
echo 启动完成后可通过浏览器访问系统
echo ========================================
java -jar "%JAR_FILE%"

REM 如果应用异常退出，暂停以查看错误信息
if %errorlevel% neq 0 (
    echo.
    echo 应用启动失败或异常退出！
    pause
)