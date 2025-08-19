<template>
  <div class="upload-page">
    <div class="upload-container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1>上传音乐</h1>
        <p>上传音乐文件到系统中进行管理</p>
      </div>
      
      <!-- 上传步骤 -->
      <el-steps :active="currentStep" align-center class="upload-steps">
        <el-step title="上传文件">
          <template #icon><i class="el-icon-upload"></i></template>
        </el-step>
        <el-step title="填写信息">
          <template #icon><i class="el-icon-edit"></i></template>
        </el-step>
        <el-step title="完成上传">
          <template #icon><i class="el-icon-check"></i></template>
        </el-step>
      </el-steps>
      
      <!-- 步骤内容 -->
      <div class="step-content">
        <!-- 步骤1：上传文件 -->
        <div v-if="currentStep === 0" class="step-upload">
          <div class="upload-area">
            <el-upload
              ref="uploadRef"
              class="music-uploader"
              drag
              :action="uploadUrl"
              :headers="uploadHeaders"
              :before-upload="beforeUpload"
              :on-change="handleFileChange"
              :file-list="fileList"
              :limit="2"
              :accept="'.mp3,.wav,.flac,.m4a'"
              :auto-upload="false"
            >
              <div class="upload-content">
                <i class="el-icon-upload upload-icon"></i>
                <div class="upload-text">
                  <p class="upload-title">点击或拖拽音乐文件到此处</p>
                  <p class="upload-hint">支持 MP3、WAV、FLAC、M4A 格式，文件大小不超过 50MB</p>
                </div>
              </div>
            </el-upload>
            
            <!-- 上传进度 -->
            <div v-if="uploading" class="upload-progress">
              <el-progress
                :percentage="uploadProgress"
                :status="uploadStatus"
                :stroke-width="8"
              />
              <p class="progress-text">{{ uploadProgressText }}</p>
            </div>
            
            <!-- 文件信息 -->
            <div v-if="selectedFile" class="file-info">
              <div class="file-item">
                <i class="el-icon-headset"></i>
                <div class="file-details">
                  <div class="file-name">{{ selectedFile.name }}</div>
                  <div class="file-meta">
                    <span>{{ formatFileSize(selectedFile.size) }}</span>
                    <span v-if="audioInfo.duration">{{ formatDuration(audioInfo.duration) }}</span>
                  </div>
                </div>
                <el-button
                  type="text"
                  @click="removeFile"
                  class="remove-btn"
                >
                  <i class="el-icon-close"></i>
                </el-button>
              </div>
              
              <!-- 音频预览 -->
              <div class="audio-preview">
                <audio
                  ref="audioRef"
                  :src="audioPreviewUrl"
                  controls
                  @loadedmetadata="handleAudioLoaded"
                >
                  您的浏览器不支持音频播放
                </audio>
              </div>
            </div>
          </div>
          
          <div class="step-actions">
            <el-button
              type="primary"
              @click="nextStep"
              :disabled="!selectedFile"
              size="medium"
            >
              下一步
            </el-button>
          </div>
        </div>
        
        <!-- 步骤2：填写信息 -->
        <div v-if="currentStep === 1" class="step-form">
          <el-form
            ref="formRef"
            :model="musicForm"
            :rules="formRules"
            label-width="100px"
            class="music-form"
          >
            <div class="form-section">
              <h3>基本信息</h3>
              
              <el-form-item label="音乐标题" prop="title">
                <el-input
                  v-model="musicForm.title"
                  placeholder="请输入音乐标题"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
              
              <el-form-item label="艺术家" prop="artist">
                <el-input
                  v-model="musicForm.artist"
                  placeholder="请输入艺术家名称"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
              
              <el-form-item label="专辑" prop="album">
                <el-input
                  v-model="musicForm.album"
                  placeholder="请输入专辑名称（可选）"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
              
              <el-form-item label="流派" prop="genre">
                <el-select
                  v-model="musicForm.genre"
                  placeholder="请选择音乐流派"
                  filterable
                  allow-create
                >
                  <el-option
                    v-for="genre in musicGenres"
                    :key="genre"
                    :label="genre"
                    :value="genre"
                  />
                </el-select>
              </el-form-item>
              
              <el-form-item label="发布年份" prop="releaseYear">
                <el-date-picker
                  v-model="musicForm.releaseYear"
                  type="year"
                  placeholder="请选择发布年份"
                  format="yyyy"
                  value-format="yyyy"
                />
              </el-form-item>
            </div>
            
            <div class="form-section">
              <h3>封面图片</h3>
              
              <el-form-item label="封面" prop="cover">
                <div class="cover-upload">
                  <el-upload
                    ref="coverUploadRef"
                    class="cover-uploader"
                    :action="uploadUrl"
                    :before-upload="beforeCoverUpload"
                    :on-change="handleCoverChange"
                    :file-list="[]"
                    :limit="1"
                    :accept="'.jpg,.jpeg,.png,.gif,.webp'"
                    :auto-upload="false"
                    :show-file-list="false"
                  >
                    <div class="cover-preview" v-if="coverPreviewUrl">
                      <img :src="coverPreviewUrl" alt="封面预览" class="cover-image" />
                      <div class="cover-overlay">
                        <el-button
                          type="primary"
                          size="small"
                          @click.stop="selectCover"
                        >
                          更换封面
                        </el-button>
                        <el-button
                          type="danger"
                          size="small"
                          @click.stop="removeCover"
                        >
                          删除封面
                        </el-button>
                      </div>
                    </div>
                    <div class="cover-placeholder" v-else>
                      <i class="el-icon-picture"></i>
                      <div class="placeholder-text">
                        <p>点击上传封面图片</p>
                        <p class="placeholder-hint">支持 JPG、PNG、GIF、WebP 格式，建议尺寸 300x300</p>
                      </div>
                    </div>
                  </el-upload>
                </div>
              </el-form-item>
            </div>
            
            <div class="form-section">
              <h3>描述信息</h3>
              
              <el-form-item label="音乐描述" prop="description">
                <el-input
                  v-model="musicForm.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入音乐描述（可选）"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              
              <el-form-item label="标签" prop="tags">
                <div class="tags-input">
                  <el-tag
                    v-for="tag in musicForm.tags"
                    :key="tag"
                    closable
                    @close="removeTag(tag)"
                    class="tag-item"
                  >
                    {{ tag }}
                  </el-tag>
                  
                  <el-input
                    v-if="tagInputVisible"
                    ref="tagInputRef"
                    v-model="tagInputValue"
                    size="small"
                    class="tag-input"
                    @keyup.enter.native="addTag"
                    @blur="addTag"
                    placeholder="输入标签"
                  />
                  
                  <el-button
                    v-else
                    size="small"
                    @click="showTagInput"
                    class="add-tag-btn"
                  >
                    <i class="el-icon-plus"></i>
                    添加标签
                  </el-button>
                </div>
              </el-form-item>
            </div>
            
            <div class="form-section">
              <h3>权限设置</h3>
              
              <el-form-item label="可见性" prop="isPublic">
                <el-radio-group v-model="musicForm.isPublic">
                  <el-radio :label="1">
                    <div class="radio-content">
                      <div class="radio-title">公开</div>
                      <div class="radio-desc">所有人都可以搜索和播放</div>
                    </div>
                  </el-radio>
                  <el-radio :label="0">
                    <div class="radio-content">
                      <div class="radio-title">私有</div>
                      <div class="radio-desc">仅自己可见</div>
                    </div>
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              
              <el-form-item label="下载权限" prop="allowDownload">
                <el-switch
                  v-model="musicForm.allowDownload"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="允许下载"
                  inactive-text="禁止下载"
                />
              </el-form-item>
              
              <el-form-item label="评论权限" prop="allowComment">
                <el-switch
                  v-model="musicForm.allowComment"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="允许评论"
                  inactive-text="禁止评论"
                />
              </el-form-item>
            </div>
          </el-form>
          
          <div class="step-actions">
            <el-button @click="prevStep" size="medium">上一步</el-button>
            <el-button
              type="primary"
              @click="submitUpload"
              :loading="submitting"
              size="medium"
            >
              提交上传
            </el-button>
          </div>
        </div>
        
        <!-- 步骤3：完成上传 -->
        <div v-if="currentStep === 2" class="step-complete">
          <div class="complete-content">
            <div class="complete-icon">
              <i class="el-icon-circle-check"></i>
            </div>
            
            <h2>上传成功！</h2>
            <p>你的音乐已成功上传到系统中</p>
            
            <div class="upload-result" v-if="uploadResult">
              <div class="result-item">
                <div class="result-cover">
                  <el-image
                    :src="uploadResult.coverUrl || '/img/default-cover.jpg'"
                    fit="cover"
                  >
                    <div slot="error" class="cover-placeholder">
                      <i class="el-icon-headset"></i>
                    </div>
                  </el-image>
                </div>
                
                <div class="result-info">
                  <h3>{{ uploadResult.title }}</h3>
                  <p>{{ uploadResult.artist }}</p>
                  <div class="result-meta">
                    <span>{{ uploadResult.genre }}</span>
                    <span>{{ formatDuration(uploadResult.duration) }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="complete-actions">
              <el-button @click="viewMusicList" type="primary" size="medium">
                查看音乐列表
              </el-button>
              <el-button @click="uploadAnother" size="medium">
                继续上传
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { getToken } from '@/utils/auth'
import axios from 'axios'

export default {
  name: 'MusicUpload',
  data() {
    return {
      // 当前步骤
      currentStep: 0,
      
      // 上传相关
      selectedFile: null,
      fileList: [],
      uploading: false,
      uploadProgress: 0,
      uploadStatus: '',
      audioPreviewUrl: '',
      audioInfo: {},
      
      // 封面图片相关
      selectedCover: null,
      coverPreviewUrl: '',
      
      // 表单相关
      musicForm: {
        title: '',
        artist: '',
        album: '',
        genre: '',
        releaseYear: '',
        description: '',
        tags: [],
        coverUrl: '',
        isPublic: 1,
        allowDownload: 1,
        allowComment: 1
      },
      
      // 标签输入
      tagInputVisible: false,
      tagInputValue: '',
      
      // 提交状态
      submitting: false,
      uploadResult: null,
      
      // 音乐流派选项
      musicGenres: [
        '流行', '摇滚', '民谣', '电子', '古典', '爵士', '蓝调', '乡村',
        '说唱', '金属', '朋克', '雷鬼', '放克', '灵魂乐', '新世纪', '世界音乐'
      ],
      
      // 表单验证规则
      formRules: {
        title: [
          { required: true, message: '请输入音乐标题', trigger: 'blur' },
          { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        artist: [
          { required: true, message: '请输入艺术家名称', trigger: 'blur' },
          { min: 1, max: 50, message: '艺术家名称长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        genre: [
          { required: true, message: '请选择音乐流派', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    // 上传URL
    uploadUrl() {
      const baseApi = process.env.VUE_APP_BASE_API || '/dev-api'
      return baseApi + '/system/music/upload'
    },
    
    // 上传请求头
    uploadHeaders() {
      return {
        'Authorization': 'Bearer ' + getToken()
      }
    },
    
    // 上传进度文本
    uploadProgressText() {
      if (this.uploadStatus === 'success') {
        return '上传完成'
      } else if (this.uploadStatus === 'exception') {
        return '上传失败'
      } else {
        return `上传中... ${this.uploadProgress}%`
      }
    }
  },
  methods: {
    // 文件选择变化处理
    handleFileChange(file) {
      if (file.status === 'ready') {
        const rawFile = file.raw
        const isValidType = ['audio/mpeg', 'audio/wav', 'audio/flac', 'audio/mp4'].includes(rawFile.type)
        const isValidSize = rawFile.size / 1024 / 1024 < 50
        
        if (!isValidType) {
          this.$message.error('只支持 MP3、WAV、FLAC、M4A 格式的音频文件')
          this.$refs.uploadRef.clearFiles()
          return
        }
        
        if (!isValidSize) {
          this.$message.error('文件大小不能超过 50MB')
          this.$refs.uploadRef.clearFiles()
          return
        }
        
        this.selectedFile = rawFile
        
        // 创建预览URL
        if (this.audioPreviewUrl) {
          URL.revokeObjectURL(this.audioPreviewUrl)
        }
        this.audioPreviewUrl = URL.createObjectURL(rawFile)
        
        // 自动填充标题（去掉扩展名）
        if (!this.musicForm.title) {
          this.musicForm.title = rawFile.name.replace(/\.[^/.]+$/, '')
        }
      }
    },
    
    // 上传前检查
    beforeUpload() {
      return false // 阻止自动上传
    },
    
    // 音频加载完成
    handleAudioLoaded() {
      const audio = this.$refs.audioRef
      if (audio) {
        this.audioInfo = {
          duration: audio.duration,
          sampleRate: audio.sampleRate || 44100
        }
      }
    },
    
    // 移除文件
    removeFile() {
      this.selectedFile = null
      this.fileList = []
      this.audioPreviewUrl = ''
      this.audioInfo = {}
      
      if (this.audioPreviewUrl) {
        URL.revokeObjectURL(this.audioPreviewUrl)
      }
    },
    
    // 封面图片上传前验证
    beforeCoverUpload(rawFile) {
      const isImage = /\.(jpg|jpeg|png|gif|webp)$/i.test(rawFile.name)
      const isLt2M = rawFile.size / 1024 / 1024 < 2
      
      if (!isImage) {
        this.$message.error('封面图片只能是 JPG、PNG、GIF、WebP 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('封面图片大小不能超过 2MB!')
        return false
      }
      return false // 阻止自动上传
    },
    
    // 封面图片选择
    handleCoverChange(file) {
      if (file.raw) {
        this.selectedCover = file.raw
        
        // 创建预览URL
        if (this.coverPreviewUrl) {
          URL.revokeObjectURL(this.coverPreviewUrl)
        }
        this.coverPreviewUrl = URL.createObjectURL(file.raw)
      }
    },
    
    // 选择封面
    selectCover() {
      this.$refs.coverUploadRef.$el.querySelector('input').click()
    },
    
    // 移除封面
    removeCover() {
      this.selectedCover = null
      if (this.coverPreviewUrl) {
        URL.revokeObjectURL(this.coverPreviewUrl)
        this.coverPreviewUrl = ''
      }
      this.$refs.coverUploadRef.clearFiles()
    },
    
    // 音乐上传成功
    handleUploadSuccess(response) {
      this.uploading = false
      
      if (response.code === 200) {
        this.uploadStatus = 'success'
        this.uploadResult = response.data
        this.currentStep = 2
        this.$message.success('音乐上传成功')
      } else {
        this.uploadStatus = 'exception'
        this.$message.error('音乐上传失败')
      }
    },
    
    // 音乐上传失败
    handleUploadError() {
      this.uploading = false
      this.uploadStatus = 'exception'
      this.$message.error('音乐上传失败')
    },
    
    // 上传进度
    handleUploadProgress(event) {
      this.uploadProgress = Math.round((event.loaded * 100) / event.total)
    },
    
    // 添加标签
    addTag() {
      const tag = this.tagInputValue.trim()
      
      if (tag && !this.musicForm.tags.includes(tag)) {
        if (this.musicForm.tags.length >= 10) {
          this.$message.warning('最多只能添加10个标签')
          return
        }
        
        this.musicForm.tags.push(tag)
      }
      
      this.tagInputVisible = false
      this.tagInputValue = ''
    },
    
    // 移除标签
    removeTag(tag) {
      const index = this.musicForm.tags.indexOf(tag)
      if (index > -1) {
        this.musicForm.tags.splice(index, 1)
      }
    },
    
    // 显示标签输入
    showTagInput() {
      this.tagInputVisible = true
      this.$nextTick(() => {
        this.$refs.tagInputRef.focus()
      })
    },
    
    // 下一步
    nextStep() {
      if (this.currentStep < 2) {
        this.currentStep++
      }
    },
    
    // 上一步
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    
    // 提交上传
    async submitUpload() {
      try {
        await this.$refs.formRef.validate()
        
        if (!this.selectedFile) {
          this.$message.error('请先选择音乐文件')
          return
        }
        
        this.submitting = true
        this.uploading = true
        this.uploadProgress = 0
        this.uploadStatus = ''
        
        // 创建FormData - 简化版本用于测试
        const formData = new FormData()
        formData.append('file', this.selectedFile)
        formData.append('title', this.musicForm.title)
        formData.append('artist', this.musicForm.artist)
        
        // 只在有值时添加可选字段，减少参数数量
        if (this.musicForm.album && this.musicForm.album.trim()) {
          formData.append('album', this.musicForm.album)
        }
        
        // 添加封面图片
        if (this.selectedCover) {
          formData.append('cover', this.selectedCover)
        }
        
        // 测试：暂时注释掉可选字段，看看是否能解决问题
        /*
        // 添加其他表单字段（只在有值时添加）
        if (this.musicForm.genre && this.musicForm.genre.trim()) {
          formData.append('genre', this.musicForm.genre)
        }
        if (this.musicForm.releaseYear && this.musicForm.releaseYear.trim()) {
          formData.append('releaseYear', this.musicForm.releaseYear)
        }
        if (this.musicForm.description && this.musicForm.description.trim()) {
          formData.append('description', this.musicForm.description)
        }
        if (this.musicForm.tags && this.musicForm.tags.length > 0) {
          formData.append('tags', this.musicForm.tags.join(','))
        }
        */
        
        // 布尔值字段转换为数字格式
        formData.append('isPublic', this.musicForm.isPublic ? '1' : '0')
        formData.append('allowDownload', this.musicForm.allowDownload ? '1' : '0')
        formData.append('allowComment', this.musicForm.allowComment ? '1' : '0')
        
        // 调试：打印FormData内容和字段数量
        console.log('FormData entries:')
        let fieldCount = 0
        for (let [key, value] of formData.entries()) {
          console.log(key, ':', value)
          fieldCount++
        }
        console.log('Total FormData fields:', fieldCount)
        
        // 使用axios直接上传，避免request封装可能的问题
        const response = await axios({
          url: process.env.VUE_APP_BASE_API + '/system/music/upload',
          method: 'post',
          data: formData,
          headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Content-Type': 'multipart/form-data'
          },
          onUploadProgress: (progressEvent) => {
            this.uploadProgress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          }
        })
        
        // 处理上传成功
        this.handleUploadSuccess(response.data)
        
      } catch (error) {
        console.error('上传失败:', error)
        if (error.response) {
          this.$message.error(error.response.data.msg || '上传失败')
        } else {
          this.$message.error('网络错误，请重试')
        }
        this.uploadStatus = 'error'
      } finally {
        this.submitting = false
        this.uploading = false
      }
    },
    
    // 查看音乐列表
    viewMusicList() {
      this.$router.push('/system/music')
    },
    
    // 继续上传
    uploadAnother() {
      // 重置上传状态
      this.selectedFile = null
      this.fileList = []
      this.audioPreviewUrl = ''
      this.audioInfo = {}
      this.currentStep = 0
      this.uploading = false
      this.uploadProgress = 0
      this.uploadStatus = ''
      this.submitting = false
      this.uploadResult = null
      
      // 重置封面图片
      this.selectedCover = null
      if (this.coverPreviewUrl) {
        URL.revokeObjectURL(this.coverPreviewUrl)
        this.coverPreviewUrl = ''
      }
      this.$refs.coverUploadRef.clearFiles()
      
      // 重置表单
      this.musicForm = {
        title: '',
        artist: '',
        album: '',
        genre: '',
        releaseYear: '',
        description: '',
        tags: [],
        coverUrl: '',
        isPublic: 1,
        allowDownload: 1,
        allowComment: 1
      }
      
      // 重置表单验证
      this.$refs.formRef && this.$refs.formRef.resetFields()
    },
    
    // 格式化文件大小
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },
    
    // 格式化时长
    formatDuration(seconds) {
      if (!seconds) return '00:00'
      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    }
  },
  
  beforeDestroy() {
    // 清理URL对象
    if (this.audioPreviewUrl) {
      URL.revokeObjectURL(this.audioPreviewUrl)
    }
    if (this.coverPreviewUrl) {
      URL.revokeObjectURL(this.coverPreviewUrl)
    }
  }
}
</script>

<style scoped>
.upload-page {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.upload-container {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.page-header {
  text-align: center;
  padding: 40px 20px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.page-header h1 {
  margin: 0 0 10px;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.upload-steps {
  padding: 30px 20px;
  background: white;
}

.step-content {
  padding: 20px 40px 40px;
}

/* 上传区域样式 */
.upload-area {
  margin-bottom: 30px;
}

.music-uploader {
  width: 100%;
}

.music-uploader >>> .el-upload {
  width: 100%;
}

.music-uploader >>> .el-upload-dragger {
  width: 100%;
  height: 200px;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  background: #fafafa;
  transition: all 0.3s;
}

.music-uploader >>> .el-upload-dragger:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 20px;
}

.upload-icon {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 16px;
}

.upload-text {
  text-align: center;
}

.upload-title {
  font-size: 16px;
  color: #606266;
  margin: 0 0 8px;
}

.upload-hint {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.upload-progress {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.progress-text {
  text-align: center;
  margin-top: 10px;
  color: #606266;
}

/* 文件信息样式 */
.file-info {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.file-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.file-item i {
  font-size: 24px;
  color: #409eff;
  margin-right: 12px;
}

.file-details {
  flex: 1;
}

.file-name {
  font-size: 16px;
  color: #303133;
  margin-bottom: 4px;
}

.file-meta {
  font-size: 14px;
  color: #909399;
}

.file-meta span {
  margin-right: 15px;
}

.remove-btn {
  color: #f56c6c;
}

.audio-preview {
  width: 100%;
}

.audio-preview audio {
  width: 100%;
}

/* 表单样式 */
.music-form {
  max-width: 600px;
}

.form-section {
  margin-bottom: 30px;
}

.form-section h3 {
  margin: 0 0 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e4e7ed;
  color: #303133;
  font-size: 18px;
}

/* 封面上传样式 */
.cover-upload {
  width: 150px;
}

.cover-uploader >>> .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 150px;
  height: 150px;
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.cover-preview:hover .cover-overlay {
  opacity: 1;
}

.cover-overlay .el-button {
  margin: 5px 0;
}

.cover-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #8c939d;
}

.cover-placeholder i {
  font-size: 28px;
  margin-bottom: 8px;
}

.placeholder-text {
  text-align: center;
}

.placeholder-text p {
  margin: 4px 0;
  font-size: 14px;
}

.placeholder-hint {
  font-size: 12px;
  color: #c0c4cc;
}

/* 标签输入样式 */
.tags-input {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.tag-item {
  margin: 0;
}

.tag-input {
  width: 120px;
}

.add-tag-btn {
  border-style: dashed;
}

/* 单选框样式 */
.radio-content {
  margin-left: 8px;
}

.radio-title {
  font-weight: 500;
  color: #303133;
}

.radio-desc {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

/* 步骤操作样式 */
.step-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.step-actions .el-button {
  margin: 0 10px;
  min-width: 100px;
}

/* 完成页面样式 */
.step-complete {
  text-align: center;
  padding: 40px 20px;
}

.complete-content {
  max-width: 400px;
  margin: 0 auto;
}

.complete-icon {
  font-size: 64px;
  color: #67c23a;
  margin-bottom: 20px;
}

.complete-content h2 {
  margin: 0 0 10px;
  color: #303133;
}

.complete-content p {
  margin: 0 0 30px;
  color: #606266;
}

.upload-result {
  margin: 30px 0;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.result-item {
  display: flex;
  align-items: center;
  text-align: left;
}

.result-cover {
  width: 60px;
  height: 60px;
  margin-right: 15px;
  border-radius: 4px;
  overflow: hidden;
}

.result-cover .el-image {
  width: 100%;
  height: 100%;
}

.result-info h3 {
  margin: 0 0 5px;
  color: #303133;
}

.result-info p {
  margin: 0 0 5px;
  color: #606266;
}

.result-meta {
  font-size: 12px;
  color: #909399;
}

.result-meta span {
  margin-right: 10px;
}

.complete-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.complete-actions .el-button {
  min-width: 120px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .upload-container {
    margin: 10px;
  }
  
  .step-content {
    padding: 20px;
  }
  
  .music-form {
    max-width: 100%;
  }
  
  .complete-actions {
    flex-direction: column;
    align-items: center;
  }
}
</style>