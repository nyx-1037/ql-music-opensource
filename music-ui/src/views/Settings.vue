<template>
  <div class="settings-page">
    <div class="settings-header">
      <h1>设置</h1>
      <p>管理您的账户设置和偏好</p>
    </div>

    <div class="settings-content">
      <el-tabs v-model="activeTab" tab-position="left" class="settings-tabs">
        <el-tab-pane label="账户设置" name="account">
          <div class="settings-section" v-loading="loading" element-loading-text="加载中...">
            <h3>账户信息</h3>
            
            <!-- 头像设置 -->
            <div class="avatar-section">
              <h4>头像</h4>
              <div class="avatar-setting">
                <el-avatar :size="80" :src="userStore.userAvatar" class="current-avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <div class="avatar-actions">
                  <el-button type="primary" @click="uploadAvatarVisible = true">
                    <el-icon><Camera /></el-icon>
                    更换头像
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 基本信息设置 -->
            <div class="profile-section">
              <h4>基本信息</h4>
              <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="100px" status-icon :validate-on-rule-change="false">
                <el-form-item label="昵称" prop="nickname">
                  <el-input 
                    v-model="editForm.nickname" 
                    placeholder="请输入昵称（2-20个字符）" 
                    style="width: 300px;"
                    maxlength="20"
                    show-word-limit
                  />
                </el-form-item>
                
                <el-form-item label="个人简介" prop="bio">
                  <el-input
                    v-model="editForm.bio"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入个人简介（最多200个字符）"
                    maxlength="200"
                    show-word-limit
                    style="width: 400px;"
                  />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input 
                    v-model="editForm.email" 
                    placeholder="请输入有效的邮箱地址" 
                    style="width: 300px;"
                    type="email"
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="updateProfile" :loading="updating">
                    <el-icon><Check /></el-icon>
                    保存修改
                  </el-button>
                  <el-button @click="confirmResetForm">
                    <el-icon><RefreshLeft /></el-icon>
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 密码设置 -->
            <div class="password-section">
              <h4>密码设置</h4>
              <el-button type="primary" @click="changePasswordVisible = true">
                <el-icon><Lock /></el-icon>
                修改密码
              </el-button>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="隐私设置" name="privacy">
          <div class="settings-section">
            <h3>隐私与安全</h3>
            <p>这里将显示隐私相关的设置选项</p>
            <el-alert
              title="功能开发中"
              type="info"
              description="隐私设置功能正在开发中，敬请期待！"
              show-icon
              :closable="false"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="播放设置" name="playback">
          <div class="settings-section">
            <h3>播放偏好</h3>
            <p>这里将显示播放相关的设置选项</p>
            <el-alert
              title="功能开发中"
              type="info"
              description="播放设置功能正在开发中，敬请期待！"
              show-icon
              :closable="false"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="通知设置" name="notifications">
          <div class="settings-section">
            <h3>通知管理</h3>
            <p>这里将显示通知相关的设置选项</p>
            <el-alert
              title="功能开发中"
              type="info"
              description="通知设置功能正在开发中，敬请期待！"
              show-icon
              :closable="false"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="关于" name="about">
          <div class="settings-section">
            <h3>关于音乐平台</h3>
            <div class="about-info">
              <p><strong>版本：</strong>1.0.0</p>
              <p><strong>开发者：</strong>音乐平台团队</p>
              <p><strong>描述：</strong>一个现代化的音乐分享与播放平台</p>
            </div>
            
            <!-- Copyright -->
            <div class="copyright-info">
              <p>&copy; 2025 Mr.Nie. <span data-i18n-key="footer.rights">保留所有权利</span></p>
              <p><a href="http://beian.miit.gov.cn/" target="_blank" rel="noopener" style="color:grey;text-decoration: none;">赣ICP备2025065901号</a></p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 上传头像对话框 -->
    <el-dialog
      v-model="uploadAvatarVisible"
      title="更换头像"
      width="400px"
    >
      <el-upload
        class="avatar-uploader"
        :auto-upload="false"
        :show-file-list="false"
        :on-change="handleAvatarChange"
        accept="image/*"
        drag
      >
        <el-avatar
          v-if="newAvatarUrl"
          :size="150"
          :src="newAvatarUrl"
          class="avatar-preview"
        />
        <div v-else class="avatar-uploader-icon">
          <el-icon><Plus /></el-icon>
          <div>点击或拖拽上传头像</div>
          <div class="upload-tip">支持 JPG、PNG 格式，文件大小不超过 2MB</div>
        </div>
      </el-upload>
      
      <template #footer>
        <el-button @click="cancelAvatarUpload">取消</el-button>
        <el-button 
          type="primary" 
          @click="updateAvatar" 
          :disabled="!selectedAvatarFile"
          :loading="avatarUploading"
        >
          保存
        </el-button>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="changePasswordVisible"
      title="修改密码"
      width="400px"
      @close="resetPasswordForm"
    >
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="80px" status-icon :validate-on-rule-change="false">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入当前密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="changePasswordVisible = false">取消</el-button>
        <el-button type="primary" @click="updatePassword" :loading="passwordUpdating">
          <el-icon><Check /></el-icon>
          确认修改
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  getUserInfo,
  updateUserInfo,
  uploadAvatar,
  changePassword
} from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  User,
  Camera,
  Plus,
  Lock,
  Check,
  RefreshLeft
} from '@element-plus/icons-vue'

export default {
  name: 'UserSettings',
  components: {
    User,
    Camera,
    Plus,
    Lock,
    Check,
    RefreshLeft
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    // 数据
    const activeTab = ref('account')
    const userInfo = ref(null)
    const loading = ref(false)
    
    // 编辑资料
    const editForm = ref({
      nickname: '',
      bio: '',
      email: ''
    })
    const editRules = {
      nickname: [
        { required: true, message: '请输入昵称', trigger: 'blur' },
        { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      bio: [
        { max: 200, message: '个人简介不能超过200个字符', trigger: 'blur' }
      ],
      email: [
        {
          validator: (rule, value, callback) => {
            if (value && value.trim() !== '') {
              const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
              if (!emailRegex.test(value)) {
                callback(new Error('请输入正确的邮箱地址'))
              } else {
                callback()
              }
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }
    const editFormRef = ref()
    const updating = ref(false)
    
    // 上传头像
    const uploadAvatarVisible = ref(false)
    const newAvatarUrl = ref('')
    const avatarUploading = ref(false)
    const selectedAvatarFile = ref(null)
    
    // 修改密码
    const changePasswordVisible = ref(false)
    const passwordForm = ref({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    const passwordRules = {
      oldPassword: [
        { required: true, message: '请输入当前密码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== passwordForm.value.newPassword) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }
    const passwordFormRef = ref()
    const passwordUpdating = ref(false)
    
    // 获取用户信息
    const fetchUserInfo = async () => {
      try {
        loading.value = true
        // 如果没有用户信息，直接获取当前用户信息
        const response = await getUserInfo()
        
        if (response) {
          userInfo.value = response
          
          // 初始化编辑表单
          editForm.value = {
            nickname: response?.nickname || response?.username || '',
            bio: response?.bio || '',
            email: response?.email || ''
          }
        } else {
          throw new Error('获取用户信息失败：响应数据为空')
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        const errorMessage = error.response?.data?.message || error.message || '获取用户信息失败'
        ElMessage.error(errorMessage)
        
        // 如果获取失败，可能是token过期，建议重新登录
        if (error.response?.status === 401) {
          ElMessage.warning('登录已过期，请重新登录')
          setTimeout(() => {
            userStore.logout()
          }, 2000)
        }
      } finally {
        loading.value = false
      }
    }
    
    // 更新资料
    const updateProfile = async () => {
      try {
        await editFormRef.value.validate()
        updating.value = true
        
        await updateUserInfo(editForm.value)
        
        // 更新本地用户信息
        userInfo.value = {
          ...userInfo.value,
          ...editForm.value
        }
        
        // 更新用户store
        userStore.updateUser(editForm.value)
        
        ElMessage.success('个人信息更新成功')
      } catch (error) {
        console.error('更新失败:', error)
        ElMessage.error('更新失败: ' + (error.response?.data?.message || error.message))
      } finally {
        updating.value = false
      }
    }
    
    // 头像文件选择
    const handleAvatarChange = (file) => {
      const isImage = file.raw.type.startsWith('image/')
      const isLt2M = file.raw.size / 1024 / 1024 < 2
      
      if (!isImage) {
        ElMessage.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!')
        return false
      }
      
      selectedAvatarFile.value = file.raw
      
      // 创建预览URL
      const reader = new FileReader()
      reader.onload = (e) => {
        newAvatarUrl.value = e.target.result
      }
      reader.readAsDataURL(file.raw)
    }
    
    // 取消头像上传
    const cancelAvatarUpload = () => {
      uploadAvatarVisible.value = false
      newAvatarUrl.value = ''
      selectedAvatarFile.value = null
    }
    
    // 更新头像
    const updateAvatar = async () => {
      if (!selectedAvatarFile.value) {
        ElMessage.error('请选择头像文件')
        return
      }
      
      try {
        avatarUploading.value = true
        
        // 调用头像上传API
        const response = await uploadAvatar(selectedAvatarFile.value)
        
        // 更新用户信息 - 后端返回的是avatarUrl字段
        const avatarUrl = response.data
        
        // 确保userInfo.value存在
        if (!userInfo.value) {
          userInfo.value = {}
        }
        userInfo.value.avatarUrl = avatarUrl
        
        // 更新用户store
        userStore.updateUser({ avatarUrl: avatarUrl })
        
        uploadAvatarVisible.value = false
        newAvatarUrl.value = ''
        selectedAvatarFile.value = null
        ElMessage.success('头像更新成功')
      } catch (error) {
        console.error('头像更新失败:', error)
        ElMessage.error('头像更新失败: ' + (error.response?.data?.message || error.message))
      } finally {
        avatarUploading.value = false
      }
    }
    
    // 重置密码表单
    const resetPasswordForm = () => {
      passwordFormRef.value?.resetFields()
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
    
    // 修改密码
    const updatePassword = async () => {
      try {
        await passwordFormRef.value.validate()
        passwordUpdating.value = true
        
        const formData = new FormData()
        formData.append('oldPassword', passwordForm.value.oldPassword)
        formData.append('newPassword', passwordForm.value.newPassword)
        
        await changePassword(formData)
        
        changePasswordVisible.value = false
        resetPasswordForm()
        ElMessage.success('密码修改成功，请重新登录')
        
        // 密码修改成功后，建议用户重新登录
        setTimeout(() => {
          userStore.logout()
          router.push('/login')
        }, 2000)
      } catch (error) {
        console.error('密码修改失败:', error)
        const errorMessage = error.response?.data?.message || error.message || '修改密码失败'
        ElMessage.error(errorMessage)
        
        // 如果是旧密码错误，清空旧密码字段
        if (error.response?.data?.message?.includes('密码错误') || error.response?.data?.message?.includes('旧密码')) {
          passwordForm.value.oldPassword = ''
          passwordFormRef.value?.validateField('oldPassword')
        }
      } finally {
        passwordUpdating.value = false
      }
    }
    
    // 确认重置表单
    const confirmResetForm = () => {
      ElMessageBox.confirm(
        '确定要重置表单吗？所有未保存的修改将会丢失。',
        '确认重置',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        resetEditForm()
        ElMessage.success('表单已重置')
      }).catch(() => {
        // 用户取消操作
      })
    }
    
    // 重置编辑表单
    const resetEditForm = () => {
      if (userInfo.value) {
        editForm.value = {
          nickname: userInfo.value.nickname || userInfo.value.username || '',
          bio: userInfo.value.bio || '',
          email: userInfo.value.email || ''
        }
      }
      editFormRef.value?.clearValidate()
    }
    
    // 组件挂载时获取用户信息
    onMounted(() => {
      if (userStore.user) {
        fetchUserInfo()
      }
    })
    
    // 监听用户登录状态变化
    watch(
      () => userStore.user,
      (newUser) => {
        if (newUser) {
          fetchUserInfo()
        }
      }
    )

    return {
      userStore,
      activeTab,
      userInfo,
      loading,
      editForm,
      editRules,
      editFormRef,
      updating,
      uploadAvatarVisible,
      newAvatarUrl,
      avatarUploading,
      selectedAvatarFile,
      changePasswordVisible,
      passwordForm,
      passwordRules,
      passwordFormRef,
      passwordUpdating,
      updateProfile,
      resetEditForm,
      confirmResetForm,
      handleAvatarChange,
      cancelAvatarUpload,
      updateAvatar,
      resetPasswordForm,
      updatePassword
    }
  }
}
</script>

<style scoped>
.settings-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.settings-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.settings-header h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.settings-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.settings-content {
  min-height: 500px;
}

.settings-tabs {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.settings-section {
  padding: 20px;
}

.settings-section h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.settings-section p {
  margin: 0 0 20px 0;
  color: #606266;
  line-height: 1.6;
}

.about-info {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 6px;
  margin-top: 20px;
}

.about-info p {
  margin: 0 0 10px 0;
  color: #303133;
}

.about-info p:last-child {
  margin-bottom: 0;
}

.about-info strong {
  color: #409eff;
}

.copyright-info {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  margin-top: 20px;
  text-align: center;
  border-top: 1px solid #e4e7ed;
}

.copyright-info p {
  margin: 5px 0;
  color: #606266;
  font-size: 12px;
}

.copyright-info a {
  color: #909399;
  text-decoration: none;
}

.copyright-info a:hover {
  color: #409eff;
}

:deep(.el-tabs--left .el-tabs__nav-wrap) {
  background: #fafafa;
}

:deep(.el-tabs--left .el-tabs__item) {
  text-align: left;
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
}

:deep(.el-tabs--left .el-tabs__item.is-active) {
  background: #fff;
  border-right: 2px solid #409eff;
}
</style>