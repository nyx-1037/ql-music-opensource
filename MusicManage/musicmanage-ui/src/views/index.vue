<template>
  <div class="app-container dashboard">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :xs="12" :sm="6" :md="6" :lg="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon user-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-info">
              <div class="card-title">总用户数</div>
              <div class="card-value">{{ overviewData.totalUsers || 0 }}</div>
              <div class="card-change">今日新增: {{ overviewData.todayUsers || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon music-icon">
              <i class="el-icon-headset"></i>
            </div>
            <div class="card-info">
              <div class="card-title">总音乐数</div>
              <div class="card-value">{{ overviewData.totalMusic || 0 }}</div>
              <div class="card-change">今日新增: {{ overviewData.todayMusic || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon playlist-icon">
              <i class="el-icon-menu"></i>
            </div>
            <div class="card-info">
              <div class="card-title">总歌单数</div>
              <div class="card-value">{{ overviewData.totalPlaylists || 0 }}</div>
              <div class="card-change">播放次数: {{ overviewData.totalPlays || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon favorite-icon">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="card-info">
              <div class="card-title">总收藏数</div>
              <div class="card-value">{{ overviewData.totalFavorites || 0 }}</div>
              <div class="card-change">关注数: {{ overviewData.totalFollows || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 用户注册趋势 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>用户注册趋势</span>
            <el-button type="text" @click="refreshUserStats">刷新</el-button>
          </div>
          <div ref="userTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <!-- 音乐分类分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>音乐分类分布</span>
            <el-button type="text" @click="refreshMusicStats">刷新</el-button>
          </div>
          <div ref="categoryChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <!-- 播放趋势 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>播放趋势</span>
            <el-button type="text" @click="refreshPlayStats">刷新</el-button>
          </div>
          <div ref="playTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <!-- 收藏趋势 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>收藏趋势</span>
            <el-button type="text" @click="refreshFavoriteStats">刷新</el-button>
          </div>
          <div ref="favoriteTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <!-- 热门音乐排行 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>热门音乐排行</span>
            <el-button type="text" @click="refreshPlayStats">刷新</el-button>
          </div>
          <div ref="hotMusicChart" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <!-- 最受关注用户 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>最受关注用户</span>
            <el-button type="text" @click="refreshFollowStats">刷新</el-button>
          </div>
          <div ref="popularUsersChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getUserStats, getMusicStats, getPlayStats, getFavoriteStats, getPlaylistStats, getCategoryStats, getFollowStats, getOverviewStats } from '@/api/system/dashboard'

export default {
  name: "Index",
  data() {
    return {
      // 概览数据
      overviewData: {},
      // 图表实例
      charts: {},
      // 加载状态
      loading: false
    }
  },
  mounted() {
    this.initDashboard()
  },
  beforeDestroy() {
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    // 初始化仪表板
    async initDashboard() {
      this.loading = true
      try {
        await this.loadOverviewData()
        await this.initCharts()
      } catch (error) {
        console.error('初始化仪表板失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载概览数据
    async loadOverviewData() {
      const response = await getOverviewStats()
      this.overviewData = response.data
    },

    // 初始化所有图表
    async initCharts() {
      await Promise.all([
        this.initUserTrendChart(),
        this.initCategoryChart(),
        this.initPlayTrendChart(),
        this.initFavoriteTrendChart(),
        this.initHotMusicChart(),
        this.initPopularUsersChart()
      ])
    },

    // 用户注册趋势图表
    async initUserTrendChart() {
      const response = await getUserStats()
      const data = response.data.userTrend || []
      
      const chart = echarts.init(this.$refs.userTrendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '新增用户',
          type: 'line',
          data: data.map(item => item.count),
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      chart.setOption(option)
      this.charts.userTrend = chart
    },

    // 音乐分类分布图表
    async initCategoryChart() {
      const response = await getMusicStats()
      const data = response.data.categoryDistribution || []
      
      const chart = echarts.init(this.$refs.categoryChart)
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          name: '音乐分类',
          type: 'pie',
          radius: '50%',
          data: data.map(item => ({
            value: item.count,
            name: item.name
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      chart.setOption(option)
      this.charts.category = chart
    },

    // 播放趋势图表
    async initPlayTrendChart() {
      const response = await getPlayStats()
      const data = response.data.playTrend || []
      
      const chart = echarts.init(this.$refs.playTrendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '播放次数',
          type: 'bar',
          data: data.map(item => item.count),
          itemStyle: {
            color: '#67C23A'
          }
        }]
      }
      chart.setOption(option)
      this.charts.playTrend = chart
    },

    // 收藏趋势图表
    async initFavoriteTrendChart() {
      const response = await getFavoriteStats()
      const data = response.data.favoriteTrend || []
      
      const chart = echarts.init(this.$refs.favoriteTrendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '收藏次数',
          type: 'line',
          data: data.map(item => item.count),
          smooth: true,
          itemStyle: {
            color: '#E6A23C'
          }
        }]
      }
      chart.setOption(option)
      this.charts.favoriteTrend = chart
    },

    // 热门音乐排行图表
    async initHotMusicChart() {
      const response = await getPlayStats()
      const data = response.data.hotMusic || []
      
      const chart = echarts.init(this.$refs.hotMusicChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.map(item => `${item.title} - ${item.artist}`)
        },
        series: [{
          name: '播放次数',
          type: 'bar',
          data: data.map(item => item.play_count),
          itemStyle: {
            color: '#F56C6C'
          }
        }]
      }
      chart.setOption(option)
      this.charts.hotMusic = chart
    },

    // 最受关注用户图表
    async initPopularUsersChart() {
      const response = await getFollowStats()
      const data = response.data.popularUsers || []
      
      const chart = echarts.init(this.$refs.popularUsersChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.map(item => item.nickname || item.username)
        },
        series: [{
          name: '关注数',
          type: 'bar',
          data: data.map(item => item.follower_count),
          itemStyle: {
            color: '#909399'
          }
        }]
      }
      chart.setOption(option)
      this.charts.popularUsers = chart
    },

    // 刷新用户统计
    async refreshUserStats() {
      await this.initUserTrendChart()
      this.$message.success('用户统计数据已刷新')
    },

    // 刷新音乐统计
    async refreshMusicStats() {
      await this.initCategoryChart()
      this.$message.success('音乐统计数据已刷新')
    },

    // 刷新播放统计
    async refreshPlayStats() {
      await Promise.all([
        this.initPlayTrendChart(),
        this.initHotMusicChart()
      ])
      this.$message.success('播放统计数据已刷新')
    },

    // 刷新收藏统计
    async refreshFavoriteStats() {
      await this.initFavoriteTrendChart()
      this.$message.success('收藏统计数据已刷新')
    },

    // 刷新关注统计
    async refreshFollowStats() {
      await this.initPopularUsersChart()
      this.$message.success('关注统计数据已刷新')
    }
  }
}
</script>
                <style scoped>
.dashboard {
  padding: 20px;
}

.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.overview-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.card-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.chart-container {
  height: 400px;
  width: 100%;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-card .el-card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.refresh-btn {
  padding: 5px 10px;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chart-container {
    height: 300px;
  }
  
  .card-icon {
    font-size: 36px;
  }
  
  .card-value {
    font-size: 20px;
  }
}

/* 加载状态 */
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}
</style>


