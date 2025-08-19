<template>
  <div class="playlist-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>我的歌单</h1>
      <el-button type="primary" @click="showCreateDialog">
        <el-icon><Plus /></el-icon>
        创建歌单
      </el-button>
    </div>
    


    <!-- 歌单列表 -->
    <div class="playlist-grid" v-loading="loading">
      <div 
        v-for="playlist in playlists" 
        :key="playlist.id" 
        class="playlist-card"
        @click="goToPlaylistDetail(playlist.id)"
      >
        <div class="playlist-cover">
          <img 
            :src="playlist.coverUrl || '/default-playlist.png'" 
            :alt="playlist.name"
            @error="handleImageError"
          />
          <div class="playlist-overlay">
            <el-button 
              type="primary" 
              circle 
              @click.stop="playPlaylist(playlist)"
            >
              <el-icon><VideoPlay /></el-icon>
            </el-button>
          </div>
        </div>
        
        <div class="playlist-info">
          <h3 class="playlist-name">{{ playlist.name }}</h3>
          <p class="playlist-description">{{ playlist.description || '暂无描述' }}</p>
          <div class="playlist-meta">
            <span class="music-count">{{ playlist.musicCount || 0 }}首</span>
            <span class="play-count">播放{{ formatPlayCount(playlist.playCount || 0) }}</span>
          </div>
          <div class="playlist-actions" v-if="playlist.id !== -1">
            <el-button 
              size="small" 
              @click.stop="editPlaylist(playlist)"
            >
              编辑
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click.stop="deletePlaylist(playlist)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && playlists.length === 0" class="empty-state">
      <el-empty description="还没有创建任何歌单">
        <el-button type="primary" @click="showCreateDialog">
          创建第一个歌单
        </el-button>
      </el-empty>
    </div>

    <!-- 创建/编辑歌单对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEditing ? '编辑歌单' : '创建歌单'"
      width="500px"
      @close="resetForm"
    >
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="80px"
      >
        <el-form-item label="歌单名称" prop="name">
          <el-input 
            v-model="form.name" 
            placeholder="请输入歌单名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="歌单描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            placeholder="请输入歌单描述（可选）"
            :rows="3"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="可见性">
          <el-radio-group v-model="form.isPublic">
            <el-radio :label="true">公开</el-radio>
            <el-radio :label="false">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          :loading="submitting" 
          @click="submitForm"
        >
          {{ isEditing ? '保存' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, VideoPlay } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { usePlayerStore } from '@/stores/player'
import {
  getMyPlaylists,
  createPlaylist,
  updatePlaylist,
  deletePlaylist as deletePlaylistAPI,
  getPlaylistMusics
} from '@/api/playlist'

export default {
  name: 'PlaylistPage',
  components: {
    Plus,
    VideoPlay
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const playerStore = usePlayerStore()
    
    // 响应式数据
    const loading = ref(false)
    const playlists = ref([])
    const dialogVisible = ref(false)
    const isEditing = ref(false)
    const submitting = ref(false)
    const formRef = ref(null)
    
    // 表单数据
    const form = reactive({
      id: null,
      name: '',
      description: '',
      isPublic: true
    })
    
    // 表单验证规则
    const rules = {
      name: [
        { required: true, message: '请输入歌单名称', trigger: 'blur' },
        { min: 1, max: 50, message: '歌单名称长度在 1 到 50 个字符', trigger: 'blur' }
      ]
    }
    
    // 获取歌单列表
    const fetchPlaylists = async () => {
      if (!userStore.isLoggedIn) {
        router.push('/login')
        return
      }
      
      try {
        loading.value = true
        const response = await getMyPlaylists()
        playlists.value = Array.isArray(response) ? response : []
      } catch (error) {
        console.error('获取歌单列表失败:', error)
        ElMessage.error('获取歌单列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 显示创建对话框
    const showCreateDialog = () => {
      isEditing.value = false
      dialogVisible.value = true
    }
    
    // 编辑歌单
    const editPlaylist = (playlist) => {
      isEditing.value = true
      form.id = playlist.id
      form.name = playlist.name
      form.description = playlist.description || ''
      form.isPublic = playlist.isPublic
      dialogVisible.value = true
    }
    
    // 重置表单
    const resetForm = () => {
      formRef.value?.resetFields()
      form.id = null
      form.name = ''
      form.description = ''
      form.isPublic = true
      isEditing.value = false
    }
    
    // 提交表单
    const submitForm = async () => {
      try {
        await formRef.value.validate()
        submitting.value = true
        
        const data = {
          name: form.name,
          description: form.description,
          isPublic: form.isPublic
        }
        
        if (isEditing.value) {
          await updatePlaylist(form.id, data)
          ElMessage.success('歌单更新成功')
        } else {
          await createPlaylist(data)
          ElMessage.success('歌单创建成功')
        }
        
        dialogVisible.value = false
        resetForm()
        await fetchPlaylists()
      } catch (error) {
        console.error('操作失败:', error)
        if (error.response?.data?.message) {
          ElMessage.error(error.response.data.message)
        } else {
          ElMessage.error('操作失败，请重试')
        }
      } finally {
        submitting.value = false
      }
    }
    
    // 删除歌单
    const deletePlaylist = async (playlist) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除歌单「${playlist.name}」吗？此操作不可恢复。`,
          '确认删除',
          {
            type: 'warning',
            confirmButtonText: '删除',
            confirmButtonClass: 'el-button--danger'
          }
        )
        
        await deletePlaylistAPI(playlist.id)
        ElMessage.success('歌单删除成功')
        await fetchPlaylists()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          ElMessage.error('删除失败，请重试')
        }
      }
    }
    
    // 播放歌单
    const playPlaylist = async (playlist) => {
      try {
        const response = await getPlaylistMusics(playlist.id)
        const musics = response.data || []
        
        if (musics.length === 0) {
          ElMessage.warning('歌单为空')
          return
        }
        
        playerStore.setPlaylist(musics)
        playerStore.play(musics[0], 0)
        ElMessage.success(`开始播放歌单「${playlist.name}」`)
      } catch (error) {
        console.error('播放失败:', error)
        ElMessage.error('播放失败，请重试')
      }
    }
    
    // 跳转到歌单详情
    const goToPlaylistDetail = (playlistId) => {
      router.push(`/playlist/${playlistId}`)
    }
    
    // 处理图片加载错误
    const handleImageError = (event) => {
      event.target.src = '/default-playlist.png'
    }
    
    // 格式化播放次数
    const formatPlayCount = (count) => {
      if (!count && count !== 0) {
        return '0'
      }
      if (count >= 10000) {
        return `${(count / 10000).toFixed(1)}万`
      }
      return count.toString()
    }
    
    onMounted(() => {
      fetchPlaylists()
    })
    
    return {
      loading,
      playlists,
      dialogVisible,
      isEditing,
      submitting,
      formRef,
      form,
      rules,
      showCreateDialog,
      editPlaylist,
      resetForm,
      submitForm,
      deletePlaylist,
      playPlaylist,
      goToPlaylistDetail,
      handleImageError,
      formatPlayCount
    }
  }
}
</script>

<style scoped>
.playlist-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #333;
}

.playlist-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.playlist-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.playlist-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.playlist-cover {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.playlist-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.playlist-card:hover .playlist-cover img {
  transform: scale(1.05);
}

.playlist-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.playlist-card:hover .playlist-overlay {
  opacity: 1;
}

.playlist-info {
  padding: 16px;
}

.playlist-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.playlist-description {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.playlist-meta {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  font-size: 12px;
  color: #999;
}

.playlist-actions {
  display: flex;
  gap: 8px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .playlist-page {
    padding: 15px;
  }
  
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .page-header h1 {
    text-align: center;
    font-size: 24px;
  }
  
  .playlist-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 15px;
  }
  
  .playlist-cover {
    height: 180px;
  }
}

@media (max-width: 480px) {
  .playlist-grid {
    grid-template-columns: 1fr;
  }
  
  .playlist-card {
    display: flex;
    height: 120px;
  }
  
  .playlist-cover {
    width: 120px;
    height: 120px;
    flex-shrink: 0;
  }
  
  .playlist-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  
  .playlist-actions {
    margin-top: auto;
  }
}
</style>