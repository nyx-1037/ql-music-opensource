<template>
  <div class="banner-management">
    <div class="header">
      <h2>Banner管理</h2>
      <el-button type="primary" @click="showCreateDialog">添加Banner</el-button>
    </div>

    <!-- Banner列表 -->
    <el-table :data="banners" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" width="150" />
      <el-table-column prop="description" label="描述" width="200" show-overflow-tooltip />
      <el-table-column label="图片" width="120">
        <template #default="scope">
          <el-image
            :src="scope.row.imageUrl"
            :preview-src-list="[scope.row.imageUrl]"
            style="width: 80px; height: 45px"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column prop="buttonText" label="按钮文字" width="120" />
      <el-table-column prop="actionType" label="动作类型" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.actionType === 'external' ? 'warning' : 'info'">
            {{ scope.row.actionType === 'external' ? '外部链接' : '内部跳转' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="link" label="跳转链接" width="200" show-overflow-tooltip />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="editBanner(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteBanner(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 创建/编辑Banner对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="bannerForm" :rules="rules" ref="bannerFormRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="bannerForm.title" placeholder="请输入Banner标题" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="bannerForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入Banner描述"
          />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <div class="upload-container">
            <el-upload
              class="banner-uploader"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
            >
              <img v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" class="banner-image" />
              <el-icon v-else class="banner-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">支持JPG、PNG、GIF、WEBP格式，文件大小不超过10MB</div>
          </div>
        </el-form-item>
        <el-form-item label="按钮文字" prop="buttonText">
          <el-input v-model="bannerForm.buttonText" placeholder="请输入按钮文字" />
        </el-form-item>
        <el-form-item label="动作类型" prop="actionType">
          <el-radio-group v-model="bannerForm.actionType">
            <el-radio label="internal">内部跳转</el-radio>
            <el-radio label="external">外部链接</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="跳转链接" prop="link">
          <el-input v-model="bannerForm.link" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="bannerForm.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="bannerForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import bannerApi from '@/api/banner'
import { getToken } from '@/utils/auth'

// 响应式数据
const banners = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('添加Banner')
const submitting = ref(false)
const bannerFormRef = ref()

// Banner表单数据
const bannerForm = reactive({
  id: null,
  title: '',
  description: '',
  imageUrl: '',
  buttonText: '',
  actionType: 'internal',
  link: '',
  sortOrder: 0,
  status: 1
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入Banner标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入Banner描述', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传Banner图片', trigger: 'change' }],
  buttonText: [{ required: true, message: '请输入按钮文字', trigger: 'blur' }],
  actionType: [{ required: true, message: '请选择动作类型', trigger: 'change' }],
  link: [{ required: true, message: '请输入跳转链接', trigger: 'blur' }],
  sortOrder: [{ required: true, message: '请输入排序值', trigger: 'blur' }]
}

// 上传配置
const uploadUrl = ref('/api/upload/banner')
const uploadHeaders = ref({
  'Authorization': `Bearer ${getToken()}`
})

// 获取Banner列表
const fetchBanners = async () => {
  loading.value = true
  try {
    const response = await bannerApi.getAllBanners()
    banners.value = response.data || []
  } catch (error) {
    console.error('获取Banner列表失败:', error)
    ElMessage.error('获取Banner列表失败')
  } finally {
    loading.value = false
  }
}

// 显示创建对话框
const showCreateDialog = () => {
  dialogTitle.value = '添加Banner'
  resetForm()
  dialogVisible.value = true
}

// 编辑Banner
const editBanner = (banner) => {
  dialogTitle.value = '编辑Banner'
  Object.assign(bannerForm, banner)
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  Object.assign(bannerForm, {
    id: null,
    title: '',
    description: '',
    imageUrl: '',
    buttonText: '',
    actionType: 'internal',
    link: '',
    sortOrder: 0,
    status: 1
  })
  if (bannerFormRef.value) {
    bannerFormRef.value.clearValidate()
  }
}

// 提交表单
const submitForm = async () => {
  if (!bannerFormRef.value) return
  
  try {
    await bannerFormRef.value.validate()
    submitting.value = true
    
    if (bannerForm.id) {
      // 更新Banner
      await bannerApi.updateBanner(bannerForm.id, bannerForm)
      ElMessage.success('Banner更新成功')
    } else {
      // 创建Banner
      await bannerApi.createBanner(bannerForm)
      ElMessage.success('Banner创建成功')
    }
    
    dialogVisible.value = false
    await fetchBanners()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 删除Banner
const deleteBanner = async (banner) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除Banner "${banner.title}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await bannerApi.deleteBanner(banner.id)
    ElMessage.success('删除成功')
    await fetchBanners()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 状态变更
const handleStatusChange = async (banner) => {
  try {
    await bannerApi.updateBannerStatus(banner.id, banner.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('状态更新失败:', error)
    ElMessage.error('状态更新失败')
    // 恢复原状态
    banner.status = banner.status === 1 ? 0 : 1
  }
}

// 上传前验证
const beforeUpload = (file) => {
  const isValidType = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'].includes(file.type)
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isValidType) {
    ElMessage.error('只支持JPG、PNG、GIF、WEBP格式的图片')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过10MB')
    return false
  }
  return true
}

// 上传成功
const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    bannerForm.imageUrl = response.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

// 上传失败
const handleUploadError = () => {
  ElMessage.error('图片上传失败')
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 组件挂载时获取数据
onMounted(() => {
  fetchBanners()
})
</script>

<style scoped>
.banner-management {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  color: #333;
}

.upload-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.banner-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.banner-uploader:hover {
  border-color: #409eff;
}

.banner-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 200px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  display: block;
}

.banner-image {
  width: 200px;
  height: 120px;
  object-fit: cover;
  display: block;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.4;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

:deep(.el-upload:hover) {
  border-color: #409eff;
}
</style>