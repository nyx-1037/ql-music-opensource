<template>
  <div class="login-page">
    <div class="login-container">
      <!-- 左侧装饰区域 -->
      <div class="login-decoration">
        <div class="decoration-content">
          <div class="logo-section">
            <div class="logo">
              <el-icon size="48"><Mic /></el-icon>
            </div>
            <h1 class="app-name">七洛音乐</h1>
            <p class="app-slogan">发现好音乐，分享好心情</p>
          </div>
          
          <div class="features">
            <div class="feature-item">
              <el-icon><Headset /></el-icon>
              <span>海量音乐库</span>
            </div>
            <div class="feature-item">
              <el-icon><Star /></el-icon>
              <span>个性化推荐</span>
            </div>
            <div class="feature-item">
              <el-icon><Share /></el-icon>
              <span>社交分享</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧登录表单 -->
      <div class="login-form-section">
        <div class="form-container">
          <div class="form-header">
            <h2>{{ isLogin ? '登录' : '注册' }}</h2>
            <p>{{ isLogin ? '欢迎回来！' : '加入我们，开启音乐之旅' }}</p>
          </div>
          
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            class="login-form"
            @submit.prevent="handleSubmit"
          >
            <!-- 用户名/邮箱 -->
            <el-form-item prop="username">
              <el-input
                v-model="form.username"
                :placeholder="isLogin ? '用户名/邮箱' : '用户名'"
                size="large"
                prefix-icon="User"
                clearable
              />
            </el-form-item>
            
            <!-- 邮箱（仅注册时显示） -->
            <el-form-item v-if="!isLogin" prop="email">
              <el-input
                v-model="form.email"
                placeholder="邮箱地址"
                size="large"
                prefix-icon="Message"
                clearable
              />
            </el-form-item>
            
            <!-- 密码 -->
            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="密码"
                size="large"
                prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>
            
            <!-- 确认密码（仅注册时显示） -->
            <el-form-item v-if="!isLogin" prop="confirmPassword">
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="确认密码"
                size="large"
                prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>
            
            <!-- 昵称（注册时显示） -->
            <el-form-item v-if="!isLogin" prop="nickname">
              <el-input
                v-model="form.nickname"
                placeholder="昵称（可选）"
                size="large"
                prefix-icon="Avatar"
                clearable
              />
            </el-form-item>
            
            <!-- 记住我/忘记密码 -->
            <div class="form-options" v-if="isLogin">
              <el-checkbox v-model="form.remember">记住我</el-checkbox>
              <el-button link @click="showForgotPassword = true">
                忘记密码？
              </el-button>
            </div>
            
            <!-- 用户协议（注册时显示） -->
            <div class="form-agreement" v-if="!isLogin">
              <el-checkbox v-model="form.agreement" :rules="agreementRules">
                我已阅读并同意
                <el-button link @click="showTerms = true">用户协议</el-button>
                和
                <el-button link @click="showPrivacy = true">隐私政策</el-button>
              </el-checkbox>
            </div>
            
            <!-- 提交按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="submit-btn"
                :loading="loading"
                @click="handleSubmit"
              >
                {{ isLogin ? '登录' : '注册' }}
              </el-button>
            </el-form-item>
          </el-form>
          
          <!-- 第三方登录 -->
          <div class="social-login" v-if="isLogin">
            <div class="divider">
              <span>或使用以下方式登录</span>
            </div>
            
            <div class="social-buttons">
              <el-button circle @click="socialLogin('qq')">
                <el-icon><ChatDotRound /></el-icon>
              </el-button>
              <el-button circle @click="socialLogin('wechat')">
                <el-icon><ChatRound /></el-icon>
              </el-button>
              <el-button circle @click="socialLogin('weibo')">
                <el-icon><Share /></el-icon>
              </el-button>
            </div>
          </div>
          
          <!-- 切换登录/注册 -->
          <div class="form-switch">
            <span>{{ isLogin ? '还没有账号？' : '已有账号？' }}</span>
            <el-button link @click="toggleMode">
              {{ isLogin ? '立即注册' : '立即登录' }}
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 忘记密码对话框 -->
    <el-dialog
      v-model="showForgotPassword"
      title="找回密码"
      width="400px"
      @close="resetForgotForm"
    >
      <el-form
        ref="forgotFormRef"
        :model="forgotForm"
        :rules="forgotRules"
        label-width="80px"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="forgotForm.email"
            placeholder="请输入注册邮箱"
            prefix-icon="Message"
          />
        </el-form-item>
        
        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-input">
            <el-input
              v-model="forgotForm.captcha"
              placeholder="验证码"
              prefix-icon="Key"
            />
            <el-button
              @click="sendForgotCaptcha"
              :disabled="forgotCaptchaDisabled"
              :loading="sendingForgotCaptcha"
            >
              {{ forgotCaptchaText }}
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="forgotForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="forgotForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showForgotPassword = false">取消</el-button>
        <el-button type="primary" @click="resetPassword" :loading="resetting">
          重置密码
        </el-button>
      </template>
    </el-dialog>
    
    <!-- 用户协议对话框 -->
    <el-dialog v-model="showTerms" title="用户协议" width="600px">
      <div class="agreement-content">
        <h3>用户协议</h3>
        <p>欢迎使用音乐平台！在使用我们的服务之前，请仔细阅读以下用户协议...</p>
        <p>1. 服务条款：用户在使用本平台时，应遵守相关法律法规...</p>
        <p>2. 用户权利：用户有权享受平台提供的音乐服务...</p>
        <p>3. 用户义务：用户应当合法使用平台服务，不得从事违法活动...</p>
        <p>4. 知识产权：平台上的音乐作品受版权保护...</p>
        <p>5. 免责声明：平台对用户使用服务产生的损失不承担责任...</p>
      </div>
    </el-dialog>
    
    <!-- 隐私政策对话框 -->
    <el-dialog v-model="showPrivacy" title="隐私政策" width="600px">
      <div class="agreement-content">
        <h3>隐私政策</h3>
        <p>我们非常重视您的隐私保护，本政策说明我们如何收集、使用和保护您的个人信息...</p>
        <p>1. 信息收集：我们会收集您提供的注册信息...</p>
        <p>2. 信息使用：我们使用您的信息来提供更好的服务...</p>
        <p>3. 信息保护：我们采用安全措施保护您的个人信息...</p>
        <p>4. 信息共享：我们不会向第三方出售您的个人信息...</p>
        <p>5. Cookie使用：我们使用Cookie来改善用户体验...</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { register } from '@/api/auth'
import { ElMessage } from 'element-plus'
import {
  Mic,
  Headset,
  Star,
  Share,
  ChatDotRound,
  ChatRound
} from '@element-plus/icons-vue'

export default {
  name: 'LoginPage',
  components: {
    Mic,
    Headset,
    Star,
    Share,
    ChatDotRound,
    ChatRound
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userStore = useUserStore()
    
    // 表单模式
    const isLogin = ref(true)
    
    // 表单数据
    const form = ref({
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      nickname: '',
      remember: false,
      agreement: false
    })
    
    // 表单引用
    const formRef = ref()
    
    // 加载状态
    const loading = ref(false)
    
    // 验证码相关（仅用于忘记密码）
    
    // 忘记密码
    const showForgotPassword = ref(false)
    const forgotForm = ref({
      email: '',
      captcha: '',
      newPassword: '',
      confirmPassword: ''
    })
    const forgotFormRef = ref()
    const resetting = ref(false)
    const sendingForgotCaptcha = ref(false)
    const forgotCaptchaCountdown = ref(0)
    const forgotCaptchaTimer = ref(null)
    
    // 协议对话框
    const showTerms = ref(false)
    const showPrivacy = ref(false)
    
    // 表单验证规则
    const rules = computed(() => {
      const baseRules = {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ]
      }
      
      if (!isLogin.value) {
        baseRules.email = [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
        
        baseRules.confirmPassword = [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== form.value.password) {
                callback(new Error('两次输入密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
        
        baseRules.nickname = [
          { max: 20, message: '昵称长度不能超过20个字符', trigger: 'blur' }
        ]
      }
      
      return baseRules
    })
    
    // 忘记密码验证规则
    const forgotRules = {
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      captcha: [
        { required: true, message: '请输入验证码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== forgotForm.value.newPassword) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }
    
    // 协议验证规则
    const agreementRules = [
      {
        validator: (rule, value, callback) => {
          if (!value) {
            callback(new Error('请同意用户协议和隐私政策'))
          } else {
            callback()
          }
        },
        trigger: 'change'
      }
    ]
    
    // 忘记密码验证码文本
    
    const forgotCaptchaText = computed(() => {
      return forgotCaptchaCountdown.value > 0 ? `${forgotCaptchaCountdown.value}s` : '发送验证码'
    })
    
    const forgotCaptchaDisabled = computed(() => {
      return forgotCaptchaCountdown.value > 0 || !forgotForm.value.email
    })
    
    // 切换登录/注册模式
    const toggleMode = () => {
      isLogin.value = !isLogin.value
      resetForm()
    }
    
    // 重置表单
    const resetForm = () => {
      form.value = {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        remember: false,
        agreement: false
      }
      formRef.value?.resetFields()
    }
    
    // 重置忘记密码表单
    const resetForgotForm = () => {
      forgotForm.value = {
        email: '',
        captcha: '',
        newPassword: '',
        confirmPassword: ''
      }
      forgotFormRef.value?.resetFields()
    }
    
    // 发送忘记密码验证码功能
    
    // 发送忘记密码验证码
    const sendForgotCaptcha = async () => {
      if (!forgotForm.value.email) {
        ElMessage.warning('请先输入邮箱地址')
        return
      }
      
      try {
        sendingForgotCaptcha.value = true
        
        // TODO: 实现发送验证码功能
        ElMessage.info('验证码功能暂未实现')
        
        ElMessage.success('验证码已发送')
        startForgotCaptchaCountdown()
      } catch (error) {
        console.error('发送验证码失败:', error)
        ElMessage.error('发送验证码失败')
      } finally {
        sendingForgotCaptcha.value = false
      }
    }
    
    // 忘记密码验证码倒计时
    
    // 开始忘记密码验证码倒计时
    const startForgotCaptchaCountdown = () => {
      forgotCaptchaCountdown.value = 60
      forgotCaptchaTimer.value = setInterval(() => {
        forgotCaptchaCountdown.value--
        if (forgotCaptchaCountdown.value <= 0) {
          clearInterval(forgotCaptchaTimer.value)
        }
      }, 1000)
    }
    
    // 提交表单
    const handleSubmit = async () => {
      try {
        await formRef.value.validate()
        
        if (!isLogin.value && !form.value.agreement) {
          ElMessage.warning('请同意用户协议和隐私政策')
          return
        }
        
        loading.value = true
        
        if (isLogin.value) {
          // 登录
          const loginData = {
            username: form.value.username,
            password: form.value.password
          }
          await userStore.login(loginData)
          
          ElMessage.success('登录成功')
          
          // 跳转到目标页面或首页
          const redirect = route.query.redirect || '/'
          router.push(redirect)
        } else {
          // 注册
          await register({
            username: form.value.username,
            email: form.value.email,
            password: form.value.password,
            nickname: form.value.nickname
          })
          
          ElMessage.success('注册成功，请登录')
          
          // 切换到登录模式
          isLogin.value = true
          form.value.password = ''
          form.value.confirmPassword = ''
          form.value.nickname = ''
        }
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error(error.message || '操作失败')
      } finally {
        loading.value = false
      }
    }
    
    // 重置密码
    const resetPassword = async () => {
      try {
        await forgotFormRef.value.validate()
        
        resetting.value = true
        
        await resetPassword({
          email: forgotForm.value.email,
          captcha: forgotForm.value.captcha,
          newPassword: forgotForm.value.newPassword
        })
        
        ElMessage.success('密码重置成功，请使用新密码登录')
        
        showForgotPassword.value = false
        resetForgotForm()
      } catch (error) {
        console.error('重置密码失败:', error)
        ElMessage.error('重置密码失败')
      } finally {
        resetting.value = false
      }
    }
    
    // 第三方登录
    const socialLogin = (provider) => {
      ElMessage.info(`${provider} 登录功能开发中`)
    }
    
    // 检查登录状态
    onMounted(() => {
      // 初始化用户状态（恢复"记住我"的登录状态）
      userStore.initUserState()
      
      if (userStore.isLoggedIn) {
        const redirect = route.query.redirect || '/'
        router.push(redirect)
      }
    })
    
    return {
      isLogin,
      form,
      formRef,
      loading,
      rules,
      // 移除注册验证码相关
      showForgotPassword,
      forgotForm,
      forgotFormRef,
      forgotRules,
      resetting,
      sendingForgotCaptcha,
      forgotCaptchaText,
      forgotCaptchaDisabled,
      showTerms,
      showPrivacy,
      agreementRules,
      toggleMode,
      resetForm,
      resetForgotForm,
      // 移除注册验证码发送功能
      sendForgotCaptcha,
      handleSubmit,
      resetPassword,
      socialLogin
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(45deg, #ff6b6b 0%, #4ecdc4 25%, #45b7d1 50%, #96ceb4 75%, #feca57 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 左侧装饰区域 */
.login-decoration {
  flex: 1;
  background: linear-gradient(45deg, #667eea 0%, #764ba2 25%, #f093fb 50%, #f5576c 75%, #4facfe 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
}

.login-decoration::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="%23ffffff" opacity="0.1"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>') repeat;
  opacity: 0.3;
}

.decoration-content {
  text-align: center;
  z-index: 1;
  position: relative;
}

.logo-section {
  margin-bottom: 60px;
}

.logo {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  backdrop-filter: blur(10px);
}

.app-name {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 10px 0;
  letter-spacing: 2px;
}

.app-slogan {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 16px;
  opacity: 0.9;
}

/* 右侧表单区域 */
.login-form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.form-container {
  width: 100%;
  max-width: 400px;
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 10px 0;
}

.form-header p {
  color: #909399;
  margin: 0;
  font-size: 14px;
}

.login-form {
  margin-bottom: 30px;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.captcha-input {
  display: flex;
  gap: 10px;
}

.captcha-input .el-input {
  flex: 1;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.form-agreement {
  margin-bottom: 20px;
  text-align: center;
}

.form-agreement .el-checkbox {
  font-size: 14px;
  color: #606266;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
}

/* 第三方登录 */
.social-login {
  margin-bottom: 30px;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e4e7ed;
}

.divider span {
  background: white;
  padding: 0 15px;
  color: #909399;
  font-size: 14px;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.social-buttons .el-button {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 1px solid #e4e7ed;
}

/* 表单切换 */
.form-switch {
  text-align: center;
  color: #606266;
  font-size: 14px;
  padding: 20px 0;
  margin-top: 10px;
}

.form-switch .el-button {
  font-size: 14px;
  padding: 8px 12px;
  margin-left: 5px;
  min-height: 32px;
}

/* 协议内容 */
.agreement-content {
  max-height: 400px;
  overflow-y: auto;
  line-height: 1.6;
  color: #606266;
}

.agreement-content h3 {
  color: #303133;
  margin-bottom: 15px;
}

.agreement-content p {
  margin-bottom: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-page {
    padding: 10px 20px 100px 20px;
    min-height: 100vh;
  }
  
  .login-container {
    flex-direction: column;
    max-width: 400px;
    min-height: auto;
  }
  
  .login-decoration {
    padding: 40px 20px;
  }
  
  .logo-section {
    margin-bottom: 30px;
  }
  
  .app-name {
    font-size: 24px;
  }
  
  .features {
    flex-direction: row;
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .feature-item {
    flex-direction: column;
    gap: 5px;
    font-size: 14px;
  }
  
  .login-form-section {
    padding: 30px 20px;
  }
  
  .form-header h2 {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .login-page {
    padding: 10px 15px 100px 15px;
    min-height: 100vh;
  }
  
  .login-container {
    border-radius: 15px;
  }
  
  .login-decoration {
    padding: 30px 15px;
  }
  
  .app-name {
    font-size: 20px;
  }
  
  .login-form-section {
    padding: 20px 15px 30px 15px;
  }
  
  .captcha-input {
    flex-direction: column;
  }
  
  .form-options {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
  
  .social-buttons {
    gap: 10px;
  }
  
  .social-buttons .el-button {
    width: 40px;
    height: 40px;
  }
  
  /* 移动端表单切换优化 */
  .form-switch {
    padding: 25px 0 15px 0;
    margin-top: 15px;
    border-top: 1px solid #f0f0f0;
    background: #fafafa;
    margin-left: -15px;
    margin-right: -15px;
    padding-left: 15px;
    padding-right: 15px;
  }
  
  .form-switch .el-button {
    padding: 10px 16px;
    min-height: 40px;
    font-size: 15px;
    font-weight: 500;
  }
}
</style>