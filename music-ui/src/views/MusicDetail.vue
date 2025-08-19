<template>
  <div class="music-detail-page" v-loading="loading">
    <div class="music-detail-container">
      <!-- 音乐信息头部 -->
      <div class="music-header" v-if="musicInfo">
        <div class="music-cover">
          <el-image
            :src="musicInfo.coverUrl || '/img/default-cover.jpg'"
            fit="cover"
            class="cover-img"
          >
            <template #error>
              <div class="cover-placeholder">
                <el-icon><Mic /></el-icon>
              </div>
            </template>
          </el-image>
          
          <div class="play-overlay">
            <el-button
              type="primary"
              circle
              size="large"
              @click="playMusic"
              :loading="playLoading"
            >
              <el-icon><VideoPlay /></el-icon>
            </el-button>
          </div>
        </div>
        
        <div class="music-info">
          <h1 class="music-title">{{ musicInfo.title }}</h1>
          <div class="music-artist">
            <span>艺术家：</span>
            <router-link :to="`/artist/${musicInfo.artistId}`" class="artist-link">
              {{ musicInfo.artist }}
            </router-link>
          </div>
          
          <div class="music-meta">
            <div class="meta-item">
              <span class="meta-label">专辑：</span>
              <span class="meta-value">{{ musicInfo.album || '未知专辑' }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-label">流派：</span>
              <span class="meta-value">{{ musicInfo.genre || '未知' }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-label">时长：</span>
              <span class="meta-value">{{ formatDuration(musicInfo.duration) }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-label">发布时间：</span>
              <span class="meta-value">{{ formatDate(musicInfo.releaseDate) }}</span>
            </div>
          </div>
          
          <div class="music-stats">
            <div class="stat-item">
              <el-icon><Headset /></el-icon>
              <span>{{ formatPlayCount(musicInfo.playCount || 0) }} 次播放</span>
            </div>
            <div class="stat-item">
              <el-icon><Star /></el-icon>
              <span>{{ musicInfo.favoriteCount || 0 }} 次收藏</span>
            </div>
            <div class="stat-item">
              <el-icon><ChatDotRound /></el-icon>
              <span>{{ musicInfo.commentCount || 0 }} 条评论</span>
            </div>
          </div>
          
          <div class="music-actions">
            <el-button
              :type="isFavorited ? 'danger' : 'primary'"
              @click="toggleFavorite"
              :loading="favoriteLoading"
            >
              <el-icon><Star /></el-icon>
              {{ isFavorited ? '取消收藏' : '收藏' }}
            </el-button>
            
            <el-dropdown @command="handleDropdownCommand">
              <el-button>
                <el-icon><Plus /></el-icon>
                添加到播放列表
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="current">
                    <el-icon><List /></el-icon>
                    当前播放列表
                  </el-dropdown-item>
                  <el-dropdown-item command="create">
                    <el-icon><Plus /></el-icon>
                    新建播放列表
                  </el-dropdown-item>
                  <el-dropdown-item divided v-for="playlist in userPlaylists" :key="playlist.id" :command="playlist.id">
                    {{ playlist.name }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            
            <el-button @click="shareMusic">
              <el-icon><Share /></el-icon>
              分享
            </el-button>
            
            <el-button @click="downloadMusic" v-if="musicInfo.allowDownload">
              <el-icon><Download /></el-icon>
              下载
            </el-button>
            
            <el-dropdown @command="handleMoreCommand">
              <el-button>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="report">
                    <el-icon><Warning /></el-icon>
                    举报
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <!-- 歌词和评论标签页 -->
      <div class="music-content">
        <el-tabs v-model="activeTab" class="content-tabs">
          <!-- 歌词 -->
          <el-tab-pane label="歌词" name="lyrics">
            <div class="lyrics-section">
              <div v-if="lyrics" class="lyrics-content">
                <div
                  v-for="(line, index) in lyricsLines"
                  :key="index"
                  class="lyrics-line"
                  :class="{ active: currentLyricIndex === index }"
                >
                  {{ line.text }}
                </div>
              </div>
              
              <div v-else class="no-lyrics">
                <el-icon><Document /></el-icon>
                <p>暂无歌词</p>
              </div>
            </div>
          </el-tab-pane>
          
          <!-- 评论 -->
          <el-tab-pane label="评论" name="comments">
            <div class="comments-section">
              <!-- 检查是否允许评论 -->
              <div v-if="musicInfo && musicInfo.allowComment === 0" class="comment-disabled">
                <el-empty description="该音乐不允许评论" />
              </div>
              
              <div v-else>
                <!-- 评论输入 -->
                <div class="comment-input" v-if="userStore.isLoggedIn">
                  <el-avatar :src="userStore.user?.avatar" size="small">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  
                  <div class="input-area">
                    <el-input
                      v-model="commentText"
                      type="textarea"
                      :rows="3"
                      placeholder="写下你的评论..."
                      maxlength="500"
                      show-word-limit
                    />
                    
                    <div class="input-actions">
                      <el-button @click="commentText = ''">取消</el-button>
                      <el-button
                        type="primary"
                        @click="submitComment"
                        :disabled="!commentText.trim()"
                        :loading="commentLoading"
                      >
                        发表评论
                      </el-button>
                    </div>
                  </div>
                </div>
                
                <div class="login-prompt" v-else>
                  <p>请先登录后发表评论</p>
                  <el-button type="primary" @click="$router.push('/login')">
                    立即登录
                  </el-button>
                </div>
              
              <!-- 评论列表 -->
              <div class="comments-list">
                <div class="comments-header">
                  <h3>全部评论 ({{ comments.length }})</h3>
                  
                  <el-radio-group v-model="commentSort" @change="fetchComments">
                    <el-radio-button label="time">最新</el-radio-button>
                    <el-radio-button label="hot">最热</el-radio-button>
                  </el-radio-group>
                </div>
                
                <div v-if="comments.length > 0">
                  <div
                    v-for="comment in comments"
                    :key="comment.id"
                    class="comment-item"
                  >
                    <el-avatar 
                      :src="comment.avatar" 
                      size="small"
                      class="clickable-avatar"
                      @click="goToUserProfile(comment.userId)"
                    >
                      <el-icon><User /></el-icon>
                    </el-avatar>
                    
                    <div class="comment-content">
                      <div class="comment-header">
                        <span 
                          class="username clickable-username" 
                          @click="goToUserProfile(comment.userId)"
                        >
                          {{ comment.nickname || comment.username || '匿名用户' }}
                        </span>
                        <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
                      </div>
                      
                      <div class="comment-text">{{ comment.content }}</div>
                      
                      <div class="comment-actions">
                        <el-button
                          link
                          size="small"
                          @click="toggleCommentLike(comment)"
                          :class="{ liked: comment.isLiked }"
                        >
                          <el-icon><Star /></el-icon>
                          {{ comment.likeCount || 0 }}
                        </el-button>
                        
                        <el-button
                          link
                          size="small"
                          @click="replyToComment(comment)"
                        >
                          <el-icon><ChatDotRound /></el-icon>
                          回复
                        </el-button>
                        
                        <el-dropdown
                          v-if="userStore.user && (userStore.user.id === comment.userId || userStore.user.role === 'admin')"
                          @command="(cmd) => handleCommentCommand(cmd, comment)"
                        >
                          <el-button link size="small">
                            <el-icon><MoreFilled /></el-icon>
                          </el-button>
                          <template #dropdown>
                            <el-dropdown-menu>
                              <el-dropdown-item command="delete">
                                <el-icon><Delete /></el-icon>
                                删除
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                      </div>
                      
                      <!-- 回复列表 -->
                      <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
                        <div
                          v-for="reply in comment.replies"
                          :key="reply.id"
                          class="reply-item"
                        >
                          <el-avatar 
                            :src="reply.avatar" 
                            size="small"
                            class="clickable-avatar"
                            @click="goToUserProfile(reply.userId)"
                          >
                            <el-icon><User /></el-icon>
                          </el-avatar>
                          
                          <div class="reply-content">
                            <div class="reply-header">
                              <span 
                                class="username clickable-username" 
                                @click="goToUserProfile(reply.userId)"
                              >
                                {{ reply.nickname || reply.username || '匿名用户' }}
                              </span>
                              <span class="reply-time">{{ formatDate(reply.createdAt) }}</span>
                            </div>
                            
                            <div class="reply-text">{{ reply.content }}</div>
                          </div>
                        </div>
                      </div>
                      
                      <!-- 回复输入 -->
                      <div v-if="replyingTo === comment.id" class="reply-input">
                        <el-input
                          v-model="replyText"
                          type="textarea"
                          :rows="2"
                          :placeholder="`回复 ${comment.nickname || comment.username || '用户'}...`"
                          maxlength="200"
                        />
                        
                        <div class="reply-actions">
                          <el-button @click="cancelReply">取消</el-button>
                          <el-button
                            type="primary"
                            @click="submitReply(comment.id)"
                            :disabled="!replyText.trim()"
                            :loading="replyLoading"
                          >
                            回复
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <el-empty v-else description="暂无评论" />
                
                  <!-- 加载更多 -->
                  <div class="load-more" v-if="hasMoreComments">
                    <el-button @click="loadMoreComments" :loading="loadingMore">
                      加载更多评论
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          
          <!-- 相关推荐 -->
          <el-tab-pane label="相关推荐" name="related">
            <div class="related-section">
              <div v-if="relatedMusic.length > 0" class="related-list">
                <div
                  v-for="(music, index) in relatedMusic"
                  :key="music.id"
                  class="related-item"
                  @click="$router.push(`/music/${music.id}`)"
                >
                  <div class="related-cover">
                    <el-image
                      :src="music.coverUrl || '/img/default-cover.jpg'"
                      fit="cover"
                    >
                      <template #error>
                        <div class="cover-placeholder">
                          <el-icon><Mic /></el-icon>
                        </div>
                      </template>
                    </el-image>
                    
                    <div class="play-btn">
                      <el-button
                        type="primary"
                        circle
                        size="small"
                        @click.stop="playRelatedMusic(music, index)"
                      >
                        <el-icon><VideoPlay /></el-icon>
                      </el-button>
                    </div>
                  </div>
                  
                  <div class="related-info">
                    <div class="related-title">{{ music.title }}</div>
                    <div class="related-artist">{{ music.artist }}</div>
                    <div class="related-stats">
                      <span>{{ formatPlayCount(music.playCount || 0) }} 次播放</span>
                      <span>{{ formatDuration(music.duration) }}</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <el-empty v-else description="暂无相关推荐" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 创建播放列表对话框 -->
    <el-dialog
      v-model="createPlaylistVisible"
      title="创建播放列表"
      width="500px"
      @close="resetCreateForm"
    >
      <el-form :model="createForm" :rules="createRules" ref="createFormRef" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="createForm.name" placeholder="请输入播放列表名称" />
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="createForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入播放列表描述"
          />
        </el-form-item>
        
        <el-form-item label="可见性" prop="isPublic">
          <el-radio-group v-model="createForm.isPublic">
            <el-radio :label="true">公开</el-radio>
            <el-radio :label="false">私有</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="createPlaylistVisible = false">取消</el-button>
        <el-button type="primary" @click="createPlaylist" :loading="creating">
          创建并添加
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePlayerStore } from '@/stores/player'
import { useUserStore } from '@/stores/user'
import {
  getMusicDetail,
  getMusicUrl,
  getRecommendMusic
} from '@/api/music'
import {
  addFavorite,
  removeFavorite,
  isFavorite
} from '@/api/favorite'
import {
  getMyPlaylists,
  createPlaylist as createPlaylistAPI,
  addMusicToPlaylist
} from '@/api/playlist'
import {
  getMusicComments,
  addComment,
  deleteComment,
  likeComment,
  unlikeComment,
  getCommentReplies
} from '@/api/comment'

import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Mic,
  VideoPlay,
  Star,
  Plus,
  Share,
  Download,
  MoreFilled,
  Warning,
  ArrowDown,
  List,
  Headset,
  ChatDotRound,
  Document,
  User,
  Delete
} from '@element-plus/icons-vue'
import dayjs from 'dayjs'

export default {
  name: 'MusicDetail',
  components: {
    Mic,
    VideoPlay,
    Star,
    Plus,
    Share,
    Download,
    MoreFilled,
    Warning,
    ArrowDown,
    List,
    Headset,
    ChatDotRound,
    Document,
    User,
    Delete
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const playerStore = usePlayerStore()
    const userStore = useUserStore()
    
    // 数据
    const musicInfo = ref(null)
    const loading = ref(false)
    const playLoading = ref(false)
    const activeTab = ref('lyrics')
    
    // 收藏状态
    const isFavorited = ref(false)
    const favoriteLoading = ref(false)
    
    // 歌词
    const lyrics = ref('')
    const lyricsLines = ref([])
    const currentLyricIndex = ref(-1)
    
    // 评论
    const comments = ref([])
    const commentText = ref('')
    const commentSort = ref('time')
    const commentLoading = ref(false)
    const hasMoreComments = ref(false)
    const loadingMore = ref(false)
    const commentPage = ref(1)
    
    // 回复
    const replyingTo = ref(null)
    const replyText = ref('')
    const replyLoading = ref(false)
    
    // 相关推荐
    const relatedMusic = ref([])
    
    // 用户播放列表
    const userPlaylists = ref([])
    
    // 创建播放列表
    const createPlaylistVisible = ref(false)
    const createForm = ref({
      name: '',
      description: '',
      isPublic: true
    })
    const createRules = {
      name: [
        { required: true, message: '请输入播放列表名称', trigger: 'blur' },
        { min: 1, max: 50, message: '名称长度在 1 到 50 个字符', trigger: 'blur' }
      ]
    }
    const createFormRef = ref()
    const creating = ref(false)
    
    // 计算属性
    const musicId = computed(() => route.params.id)
    
    // 获取音乐详情
    const fetchMusicDetail = async () => {
      try {
        loading.value = true
        
        const response = await getMusicDetail(musicId.value)
        musicInfo.value = response
        
        // 检查收藏状态
        if (userStore.isLoggedIn) {
          checkFavorite()
        }
        
        // 获取歌词
        fetchLyrics()
        
        // 获取相关推荐
        fetchRelatedMusic()
      } catch (error) {
        console.error('获取音乐详情失败:', error)
        ElMessage.error('获取音乐详情失败')
        router.push('/music')
      } finally {
        loading.value = false
      }
    }
    
    // 检查收藏状态
    const checkFavorite = async () => {
      if (!userStore.isLoggedIn || !musicInfo.value) {
        isFavorited.value = false
        return
      }
      
      try {
        const response = await isFavorite(musicInfo.value.id)
        isFavorited.value = response || false
      } catch (error) {
        console.error('检查收藏状态失败:', error)
        isFavorited.value = false
      }
    }
    
    // 获取歌词
    const fetchLyrics = async () => {
      try {
        // TODO: 实现获取歌词功能
        lyrics.value = '暂无歌词'
        
        if (lyrics.value) {
          // 解析歌词
          lyricsLines.value = lyrics.value.split('\n').map((line, index) => ({
            text: line.trim(),
            time: index * 3 // 简单的时间计算，实际应该解析LRC格式
          }))
        }
      } catch (error) {
        console.error('获取歌词失败:', error)
      }
    }
    
    // 获取评论
    const fetchComments = async (reset = true) => {
      if (!musicInfo.value) return
      
      try {
        if (reset) {
          commentPage.value = 1
          comments.value = []
        }
        
        commentLoading.value = true
        
        const params = {
          page: commentPage.value,
          limit: 10,
          sortType: commentSort.value === 'hot' ? 'hot' : 'time'
        }
        
        const response = await getMusicComments(musicInfo.value.id, params)
        
        if (reset) {
          comments.value = response.records || []
        } else {
          comments.value.push(...(response.records || []))
        }
        
        hasMoreComments.value = response.current < response.pages
        
        // 为每个评论获取回复
        for (const comment of comments.value) {
          if (comment.replyCount > 0) {
            await fetchCommentReplies(comment)
          }
        }
      } catch (error) {
        console.error('获取评论失败:', error)
        ElMessage.error('获取评论失败')
      } finally {
        commentLoading.value = false
      }
    }
    
    // 获取评论回复
    const fetchCommentReplies = async (comment) => {
      try {
        const response = await getCommentReplies(comment.id, {
          current: 1,
          size: 5
        })
        comment.replies = response.records || []
      } catch (error) {
        console.error('获取回复失败:', error)
        comment.replies = []
      }
    }
    
    // 获取相关推荐
    const fetchRelatedMusic = async () => {
      try {
        const response = await getRecommendMusic({
          page: 1,
          limit: 10
        })
        
        // 处理响应数据
        let musicList = []
        if (response) {
          if (response.records) {
            musicList = response.records
          } else if (response.data && response.data.records) {
            musicList = response.data.records
          } else if (Array.isArray(response)) {
            musicList = response
          }
        }
        
        // 过滤掉当前音乐
        relatedMusic.value = musicList.filter(music => 
          music && music.id && music.id !== musicInfo.value?.id
        ).slice(0, 8) // 最多显示8首相关音乐
      } catch (error) {
        console.error('获取相关推荐失败:', error)
        relatedMusic.value = []
      }
    }
    
    // 获取用户播放列表
    const fetchUserPlaylists = async () => {
      if (!userStore.isLoggedIn) {
        userPlaylists.value = []
        return
      }
      
      try {
        const response = await getMyPlaylists()
        userPlaylists.value = response || []
      } catch (error) {
        console.error('获取用户歌单失败:', error)
        userPlaylists.value = []
      }
    }
    
    // 播放音乐
    const playMusic = () => {
      if (!musicInfo.value) return
      
      playLoading.value = true
      
      setTimeout(() => {
        playerStore.play(musicInfo.value, 0)
        playLoading.value = false
        ElMessage.success('开始播放')
      }, 500)
    }
    
    // 切换收藏状态
    const toggleFavorite = async () => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      if (!musicInfo.value) {
        ElMessage.error('音乐信息不存在')
        return
      }
      
      try {
        favoriteLoading.value = true
        
        if (isFavorited.value) {
          // 取消收藏
          await removeFavorite(musicInfo.value.id)
          isFavorited.value = false
          ElMessage.success('已取消收藏')
          
          // 更新音乐信息中的收藏数
          if (musicInfo.value.favoriteCount > 0) {
            musicInfo.value.favoriteCount--
          }
        } else {
          // 添加收藏
          await addFavorite(musicInfo.value.id)
          isFavorited.value = true
          ElMessage.success('收藏成功')
          
          // 更新音乐信息中的收藏数
          musicInfo.value.favoriteCount = (musicInfo.value.favoriteCount || 0) + 1
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        ElMessage.error('操作失败，请重试')
      } finally {
        favoriteLoading.value = false
      }
    }
    
    // 处理下拉菜单命令
    const handleDropdownCommand = (command) => {
      if (command === 'current') {
        playerStore.addToPlaylist(musicInfo.value)
        ElMessage.success('已添加到当前播放列表')
      } else if (command === 'create') {
        createPlaylistVisible.value = true
      } else {
        // 添加到指定播放列表
        addToPlaylist(command)
      }
    }
    
    // 添加到歌单
    const addToPlaylist = async (playlistId) => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      if (!musicInfo.value) {
        ElMessage.error('音乐信息不存在')
        return
      }
      
      try {
        await addMusicToPlaylist(playlistId, musicInfo.value.id)
        ElMessage.success('已添加到歌单')
      } catch (error) {
        console.error('添加到歌单失败:', error)
        if (error.response?.status === 409) {
          ElMessage.warning('该音乐已在歌单中')
        } else {
          ElMessage.error('添加失败，请重试')
        }
      }
    }
    
    // 分享音乐
    const shareMusic = () => {
      const url = window.location.href
      
      if (navigator.share) {
        navigator.share({
          title: musicInfo.value.title,
          text: `分享音乐：${musicInfo.value.title} - ${musicInfo.value.artist}`,
          url: url
        })
      } else {
        // 复制到剪贴板
        navigator.clipboard.writeText(url).then(() => {
          ElMessage.success('链接已复制到剪贴板')
        })
      }
    }
    
    // 下载音乐
    const downloadMusic = async () => {
      try {
        const response = await getMusicUrl(musicId.value)
        const musicUrl = response.url
        
        if (!musicUrl) {
          ElMessage.error('无法获取音乐下载链接')
          return
        }
        
        // 在新标签页中打开音乐URL，浏览器会自动下载
        window.open(musicUrl, '_blank')
        
        ElMessage.success('正在跳转下载页面')
      } catch (error) {
        console.error('获取下载链接失败:', error)
        ElMessage.error('获取下载链接失败')
      }
    }
    
    // 处理更多命令
    const handleMoreCommand = (command) => {
      if (command === 'report') {
        ElMessage.info('举报功能开发中')
      }
    }
    
    // 提交评论
    const submitComment = async () => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      if (!commentText.value.trim()) {
        ElMessage.warning('请输入评论内容')
        return
      }
      
      if (!musicInfo.value) {
        ElMessage.error('音乐信息不存在')
        return
      }
      
      try {
        commentLoading.value = true
        
        const commentData = {
          musicId: musicInfo.value.id,
          content: commentText.value.trim()
        }
        
        await addComment(commentData)
        
        ElMessage.success('评论发表成功')
        commentText.value = ''
        
        // 重新获取评论列表
        await fetchComments(true)
      } catch (error) {
        console.error('发表评论失败:', error)
        if (error.response?.data?.message) {
          ElMessage.error(error.response.data.message)
        } else {
          ElMessage.error('发表评论失败，请重试')
        }
      } finally {
        commentLoading.value = false
      }
    }
    
    // 回复评论
    const replyToComment = (comment) => {
      replyingTo.value = comment.id
      replyText.value = ''
    }
    
    // 取消回复
    const cancelReply = () => {
      replyingTo.value = null
      replyText.value = ''
    }
    
    // 提交回复
    const submitReply = async (parentId) => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      if (!replyText.value.trim()) {
        ElMessage.warning('请输入回复内容')
        return
      }
      
      if (!musicInfo.value) {
        ElMessage.error('音乐信息不存在')
        return
      }
      
      try {
        replyLoading.value = true
        
        const replyData = {
          musicId: musicInfo.value.id,
          content: replyText.value.trim(),
          parentId: parentId
        }
        
        await addComment(replyData)
        
        ElMessage.success('回复发表成功')
        cancelReply()
        
        // 重新获取评论列表
        await fetchComments(true)
      } catch (error) {
        console.error('发表回复失败:', error)
        if (error.response?.data?.message) {
          ElMessage.error(error.response.data.message)
        } else {
          ElMessage.error('发表回复失败，请重试')
        }
      } finally {
        replyLoading.value = false
      }
    }
    
    // 切换评论点赞
    const toggleCommentLike = async (comment) => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      try {
        if (comment.isLiked) {
          // 取消点赞
          await unlikeComment(comment.id)
          comment.isLiked = false
          comment.likeCount = Math.max(0, (comment.likeCount || 0) - 1)
          ElMessage.success('已取消点赞')
        } else {
          // 点赞
          await likeComment(comment.id)
          comment.isLiked = true
          comment.likeCount = (comment.likeCount || 0) + 1
          ElMessage.success('点赞成功')
        }
      } catch (error) {
        console.error('点赞操作失败:', error)
        if (error.response?.data?.message) {
          ElMessage.error(error.response.data.message)
        } else {
          ElMessage.error('操作失败，请重试')
        }
      }
    }
    
    // 处理评论命令
    const handleCommentCommand = async (command, comment) => {
      if (command === 'delete') {
        try {
          await ElMessageBox.confirm('确定要删除这条评论吗？', '确认删除', {
            type: 'warning'
          })
          
          await deleteComment(comment.id)
          ElMessage.success('评论删除成功')
          
          // 重新获取评论列表
          await fetchComments(true)
        } catch (error) {
          if (error !== 'cancel') {
            console.error('删除失败:', error)
            if (error.response?.data?.message) {
              ElMessage.error(error.response.data.message)
            } else {
              ElMessage.error('删除失败，请重试')
            }
          }
        }
      }
    }
    
    // 加载更多评论
    const loadMoreComments = async () => {
      loadingMore.value = true
      commentPage.value++
      await fetchComments(false)
      loadingMore.value = false
    }
    
    // 播放相关音乐
    const playRelatedMusic = (music, index) => {
      playerStore.setPlaylist(relatedMusic.value)
      playerStore.play(music, index)
    }
    
    // 重置创建表单
    const resetCreateForm = () => {
      createFormRef.value?.resetFields()
    }
    
    // 创建歌单
    const createPlaylist = async () => {
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
      }
      
      try {
        await createFormRef.value.validate()
        creating.value = true
        
        // 创建歌单
        const response = await createPlaylistAPI({
          name: createForm.value.name,
          description: createForm.value.description,
          isPublic: createForm.value.isPublic
        })
        
        const newPlaylist = response.data
        
        // 添加音乐到新创建的歌单
        if (musicInfo.value) {
          await addMusicToPlaylist(newPlaylist.id, musicInfo.value.id)
        }
        
        // 更新用户歌单列表
        await fetchUserPlaylists()
        
        createPlaylistVisible.value = false
        resetCreateForm()
        ElMessage.success('歌单创建成功，音乐已添加')
      } catch (error) {
        console.error('创建失败:', error)
        if (error.response?.data?.message) {
          ElMessage.error(error.response.data.message)
        } else {
          ElMessage.error('创建失败，请重试')
        }
      } finally {
        creating.value = false
      }
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
    
    // 格式化时长
    const formatDuration = (seconds) => {
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
    }
    
    // 格式化日期
    const formatDate = (date) => {
      return dayjs(date).format('YYYY-MM-DD')
    }
    
    // 监听路由变化
    watch(
      () => route.params.id,
      (newId) => {
        if (newId) {
          fetchMusicDetail()
          // 重置状态
          activeTab.value = 'lyrics'
          comments.value = []
          commentPage.value = 1
        }
      },
      { immediate: true }
    )
    
    // 监听标签页变化
    watch(activeTab, (newTab) => {
      if (newTab === 'comments' && comments.value.length === 0) {
        fetchComments()
      }
    })
    
    // 监听播放器状态更新歌词
    watch(
      () => playerStore.currentTime,
      (currentTime) => {
        if (lyricsLines.value.length > 0) {
          const index = lyricsLines.value.findIndex(line => line.time > currentTime)
          currentLyricIndex.value = index > 0 ? index - 1 : lyricsLines.value.length - 1
        }
      }
    )
    
    // 跳转到用户主页
    const goToUserProfile = (userId) => {
      if (userId) {
        router.push(`/user/${userId}`)
      }
    }
    
    onMounted(() => {
      fetchUserPlaylists()
    })
    
    return {
      musicInfo,
      loading,
      playLoading,
      activeTab,
      isFavorited,
      favoriteLoading,
      lyrics,
      lyricsLines,
      currentLyricIndex,
      comments,
      commentText,
      commentSort,
      commentLoading,
      hasMoreComments,
      loadingMore,
      replyingTo,
      replyText,
      replyLoading,
      relatedMusic,
      userPlaylists,
      createPlaylistVisible,
      createForm,
      createRules,
      createFormRef,
      creating,
      userStore,
      playMusic,
      toggleFavorite,
      handleDropdownCommand,
      shareMusic,
      downloadMusic,
      handleMoreCommand,
      fetchComments,
      fetchCommentReplies,
      submitComment,
      replyToComment,
      cancelReply,
      submitReply,
      toggleCommentLike,
      handleCommentCommand,
      loadMoreComments,
      playRelatedMusic,
      createPlaylist,
      resetCreateForm,
      formatPlayCount,
      formatDuration,
      formatDate,
      goToUserProfile
    }
  }
}
</script>

<style scoped>
.music-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 音乐信息头部 */
.music-header {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.music-cover {
  position: relative;
  width: 250px;
  height: 250px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.cover-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 60px;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.music-cover:hover .play-overlay {
  opacity: 1;
}

.music-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.music-title {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 15px 0;
  line-height: 1.2;
}

.music-artist {
  font-size: 18px;
  color: #606266;
  margin-bottom: 20px;
}

.artist-link {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.artist-link:hover {
  text-decoration: underline;
}

.music-meta {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-label {
  font-weight: 500;
  color: #909399;
  margin-right: 8px;
  min-width: 80px;
}

.meta-value {
  color: #303133;
}

.music-stats {
  display: flex;
  gap: 30px;
  margin-bottom: 25px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #606266;
  font-size: 14px;
}

.music-actions {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

/* 内容标签页 */
.music-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.content-tabs {
  min-height: 500px;
}

.content-tabs :deep(.el-tabs__content) {
  padding: 30px;
}

/* 歌词区域 */
.lyrics-section {
  max-height: 600px;
  overflow-y: auto;
}

.lyrics-content {
  line-height: 2;
  font-size: 16px;
}

.lyrics-line {
  padding: 8px 0;
  color: #606266;
  transition: color 0.3s;
}

.lyrics-line.active {
  color: #409eff;
  font-weight: 500;
}

.no-lyrics {
  text-align: center;
  padding: 60px 0;
  color: #909399;
}

.no-lyrics .el-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

/* 评论区域 */
.comment-input {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.input-area {
  flex: 1;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.login-prompt {
  text-align: center;
  padding: 30px;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 30px;
}

.comments-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.comments-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.comment-item {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 8px;
}

.username {
  font-weight: 500;
  color: #303133;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.comment-text {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 10px;
}

.comment-actions {
  display: flex;
  gap: 15px;
}

.comment-actions .el-button.liked {
  color: #f56c6c;
}

/* 可点击的用户头像和用户名 */
.clickable-avatar {
  cursor: pointer;
  transition: opacity 0.2s;
}

.clickable-avatar:hover {
  opacity: 0.8;
}

.clickable-username {
  cursor: pointer;
  transition: color 0.2s;
}

.clickable-username:hover {
  color: #409eff;
  text-decoration: underline;
}

.replies-list {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #e4e7ed;
}

.reply-item {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 5px;
}

.reply-time {
  font-size: 12px;
  color: #909399;
}

.reply-text {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}

.reply-input {
  margin-top: 15px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.load-more {
  text-align: center;
  margin-top: 20px;
}

/* 相关推荐 */
.related-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.related-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.related-item:hover {
  background-color: #f5f7fa;
}

.related-cover {
  position: relative;
  width: 60px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
}

.play-btn {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.related-item:hover .play-btn {
  opacity: 1;
}

.related-info {
  flex: 1;
  min-width: 0;
}

.related-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.related-artist {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.related-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #c0c4cc;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-detail-page {
    padding: 10px;
  }
  
  .music-header {
    flex-direction: column;
    text-align: center;
    padding: 20px;
  }
  
  .music-cover {
    width: 200px;
    height: 200px;
    margin: 0 auto;
  }
  
  .music-title {
    font-size: 24px;
  }
  
  .music-meta {
    grid-template-columns: 1fr;
  }
  
  .music-stats {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .music-actions {
    justify-content: center;
  }
  
  .content-tabs :deep(.el-tabs__content) {
    padding: 20px;
  }
  
  .related-list {
    grid-template-columns: 1fr;
  }
  
  .comment-input {
    flex-direction: column;
  }
}

@media (max-width: 576px) {
  .music-cover {
    width: 150px;
    height: 150px;
  }
  
  .music-title {
    font-size: 20px;
  }
  
  .music-actions {
    flex-direction: column;
  }
  
  .comments-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
}
</style>