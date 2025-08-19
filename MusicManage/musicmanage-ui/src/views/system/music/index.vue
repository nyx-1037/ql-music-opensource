<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="艺术家" prop="artist">
        <el-input
          v-model="queryParams.artist"
          placeholder="请输入艺术家"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="专辑" prop="album">
        <el-input
          v-model="queryParams.album"
          placeholder="请输入专辑"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时长" prop="duration">
        <el-input
          v-model="queryParams.duration"
          placeholder="请输入时长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原始文件名" prop="originalFileName">
        <el-input
          v-model="queryParams.originalFileName"
          placeholder="请输入原始文件名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件大小" prop="fileSize">
        <el-input
          v-model="queryParams.fileSize"
          placeholder="请输入文件大小"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上传用户ID" prop="uploadUserId">
        <el-input
          v-model="queryParams.uploadUserId"
          placeholder="请输入上传用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="播放次数" prop="playCount">
        <el-input
          v-model="queryParams.playCount"
          placeholder="请输入播放次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="逻辑删除标志" prop="isDeleted">
        <el-input
          v-model="queryParams.isDeleted"
          placeholder="请输入逻辑删除标志"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="音乐流派" prop="genre">
        <el-input
          v-model="queryParams.genre"
          placeholder="请输入音乐流派"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布年份" prop="releaseYear">
        <el-input
          v-model="queryParams.releaseYear"
          placeholder="请输入发布年份"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否公开(0:私有 1:公开)" prop="isPublic">
        <el-input
          v-model="queryParams.isPublic"
          placeholder="请输入是否公开(0:私有 1:公开)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否允许下载(0:不允许 1:允许)" prop="allowDownload">
        <el-input
          v-model="queryParams.allowDownload"
          placeholder="请输入是否允许下载(0:不允许 1:允许)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否允许评论(0:不允许 1:允许)" prop="allowComment">
        <el-input
          v-model="queryParams.allowComment"
          placeholder="请输入是否允许评论(0:不允许 1:允许)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:music:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:music:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:music:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:music:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="musicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="音乐ID" align="center" prop="id" />
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="艺术家" align="center" prop="artist" />
      <el-table-column label="专辑" align="center" prop="album" />
      <el-table-column label="时长" align="center" prop="duration" />
      <el-table-column label="音频文件" align="center" width="120">
        <template slot-scope="scope">
          <el-dropdown trigger="click" v-if="scope.row.filePath">
            <el-button type="primary" size="mini" icon="el-icon-video-play">
              播放 <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <audio controls style="width: 200px;">
                  <source :src="scope.row.filePath" type="audio/mpeg">
                  您的浏览器不支持音频播放
                </audio>
              </el-dropdown-item>
              <el-dropdown-item divided>
                <span style="font-size: 12px; color: #909399;">文件名: {{ scope.row.fileName }}</span>
              </el-dropdown-item>
              <el-dropdown-item>
                <span style="font-size: 12px; color: #909399;">原始名: {{ scope.row.originalFileName }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span v-else style="color: #C0C4CC;">无文件</span>
        </template>
      </el-table-column>
      <el-table-column label="文件大小" align="center" prop="fileSize" width="100">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.fileSize) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center" prop="fileType" width="80" />
      <el-table-column label="上传用户ID" align="center" prop="uploadUserId" width="100" />
      <el-table-column label="播放次数" align="center" prop="playCount" width="80" />
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"

            @change="handleStatusChange(scope.row)"
            v-hasPermi="['system:music:edit']"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="音乐流派" align="center" prop="genre" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="发布年份" align="center" prop="releaseYear" width="80" />
      <el-table-column label="音乐描述" align="center" prop="description" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="标签" align="center" prop="tags" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="公开状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isPublic"
            :active-value="1"
            :inactive-value="0"

            @change="handlePublicChange(scope.row)"
            v-hasPermi="['system:music:edit']"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="允许下载" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.allowDownload"
            :active-value="1"
            :inactive-value="0"

            @change="handleDownloadChange(scope.row)"
            v-hasPermi="['system:music:edit']"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="允许评论" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.allowComment"
            :active-value="1"
            :inactive-value="0"

            @change="handleCommentChange(scope.row)"
            v-hasPermi="['system:music:edit']"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="封面图片" align="center" width="120">
        <template slot-scope="scope">
          <el-dropdown trigger="click" v-if="scope.row.coverUrl">
            <el-button type="success" size="mini" icon="el-icon-picture">
              查看 <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-align: center; padding: 10px;">
                  <img :src="scope.row.coverUrl" alt="封面图片" style="max-width: 200px; max-height: 200px; border-radius: 4px;" @error="handleImageError">
                </div>
              </el-dropdown-item>
              <el-dropdown-item divided>
                <el-button type="text" size="mini" @click="previewImage(scope.row.coverUrl)">全屏预览</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span v-else style="color: #C0C4CC;">无封面</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:music:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:music:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改音乐对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="艺术家" prop="artist">
          <el-input v-model="form.artist" placeholder="请输入艺术家" />
        </el-form-item>
        <el-form-item label="专辑" prop="album">
          <el-input v-model="form.album" placeholder="请输入专辑" />
        </el-form-item>
        <el-form-item label="时长" prop="duration">
          <el-input v-model="form.duration" placeholder="请输入时长" />
        </el-form-item>
        <el-form-item label="文件路径" prop="filePath">
          <el-input v-model="form.filePath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名" />
        </el-form-item>
        <el-form-item label="原始文件名" prop="originalFileName">
          <el-input v-model="form.originalFileName" placeholder="请输入原始文件名" />
        </el-form-item>
        <el-form-item label="文件大小" prop="fileSize">
          <el-input v-model="form.fileSize" placeholder="请输入文件大小" />
        </el-form-item>
        <el-form-item label="上传用户ID" prop="uploadUserId">
          <el-input v-model="form.uploadUserId" placeholder="请输入上传用户ID" />
        </el-form-item>
        <el-form-item label="播放次数" prop="playCount">
          <el-input v-model="form.playCount" placeholder="请输入播放次数" />
        </el-form-item>
        <el-form-item label="逻辑删除标志" prop="isDeleted">
          <el-input v-model="form.isDeleted" placeholder="请输入逻辑删除标志" />
        </el-form-item>
        <el-form-item label="音乐流派" prop="genre">
          <el-input v-model="form.genre" placeholder="请输入音乐流派" />
        </el-form-item>
        <el-form-item label="发布年份" prop="releaseYear">
          <el-input v-model="form.releaseYear" placeholder="请输入发布年份" />
        </el-form-item>
        <el-form-item label="音乐描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="标签(逗号分隔)" prop="tags">
          <el-input v-model="form.tags" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否公开(0:私有 1:公开)" prop="isPublic">
          <el-input v-model="form.isPublic" placeholder="请输入是否公开(0:私有 1:公开)" />
        </el-form-item>
        <el-form-item label="是否允许下载(0:不允许 1:允许)" prop="allowDownload">
          <el-input v-model="form.allowDownload" placeholder="请输入是否允许下载(0:不允许 1:允许)" />
        </el-form-item>
        <el-form-item label="是否允许评论(0:不允许 1:允许)" prop="allowComment">
          <el-input v-model="form.allowComment" placeholder="请输入是否允许评论(0:不允许 1:允许)" />
        </el-form-item>
        <el-form-item label="封面图片URL" prop="coverUrl">
          <el-input v-model="form.coverUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMusic, getMusic, delMusic, addMusic, updateMusic } from "@/api/system/music"

export default {
  name: "Music",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 音乐表格数据
      musicList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        artist: null,
        album: null,
        duration: null,
        filePath: null,
        fileName: null,
        originalFileName: null,
        fileSize: null,
        fileType: null,
        uploadUserId: null,
        playCount: null,
        status: null,
        isDeleted: null,
        genre: null,
        releaseYear: null,
        description: null,
        tags: null,
        isPublic: null,
        allowDownload: null,
        allowComment: null,
        coverUrl: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        artist: [
          { required: true, message: "艺术家不能为空", trigger: "blur" }
        ],
        filePath: [
          { required: true, message: "文件路径不能为空", trigger: "blur" }
        ],
        fileName: [
          { required: true, message: "文件名不能为空", trigger: "blur" }
        ],
        originalFileName: [
          { required: true, message: "原始文件名不能为空", trigger: "blur" }
        ],
        fileSize: [
          { required: true, message: "文件大小不能为空", trigger: "blur" }
        ],
        fileType: [
          { required: true, message: "文件类型不能为空", trigger: "change" }
        ],
        uploadUserId: [
          { required: true, message: "上传用户ID不能为空", trigger: "blur" }
        ],
        playCount: [
          { required: true, message: "播放次数不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        isDeleted: [
          { required: true, message: "逻辑删除标志不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询音乐列表 */
    getList() {
      this.loading = true
      listMusic(this.queryParams).then(response => {
        this.musicList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        artist: null,
        album: null,
        duration: null,
        filePath: null,
        fileName: null,
        originalFileName: null,
        fileSize: null,
        fileType: null,
        uploadUserId: null,
        playCount: null,
        status: null,
        createTime: null,
        updateTime: null,
        isDeleted: null,
        genre: null,
        releaseYear: null,
        description: null,
        tags: null,
        isPublic: null,
        allowDownload: null,
        allowComment: null,
        coverUrl: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加音乐"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMusic(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改音乐"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMusic(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMusic(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除音乐编号为"' + ids + '"的数据项？').then(function() {
        return delMusic(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/music/export', {
        ...this.queryParams
      }, `music_${new Date().getTime()}.xlsx`)
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.status === 1 ? "启用" : "禁用";
      this.$modal.confirm('确认要"' + text + '""' + row.title + '"音乐吗？').then(() => {
        return updateMusic(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {
        row.status = row.status === 0 ? 1 : 0;
      });
    },
    /** 公开状态修改 */
    handlePublicChange(row) {
      let text = row.isPublic === 1 ? "公开" : "私有";
      this.$modal.confirm('确认要将"' + row.title + '"设置为"' + text + '"吗？').then(() => {
        return updateMusic(row);
      }).then(() => {
        this.$modal.msgSuccess("设置成功");
      }).catch(() => {
        row.isPublic = row.isPublic === 0 ? 1 : 0;
      });
    },
    /** 下载权限修改 */
    handleDownloadChange(row) {
      let text = row.allowDownload === 1 ? "允许" : "禁止";
      this.$modal.confirm('确认要"' + text + '""' + row.title + '"的下载功能吗？').then(() => {
        return updateMusic(row);
      }).then(() => {
        this.$modal.msgSuccess("设置成功");
      }).catch(() => {
        row.allowDownload = row.allowDownload === 0 ? 1 : 0;
      });
    },
    /** 评论权限修改 */
    handleCommentChange(row) {
      let text = row.allowComment === 1 ? "允许" : "禁止";
      this.$modal.confirm('确认要"' + text + '""' + row.title + '"的评论功能吗？').then(() => {
        return updateMusic(row);
      }).then(() => {
        this.$modal.msgSuccess("设置成功");
      }).catch(() => {
        row.allowComment = row.allowComment === 0 ? 1 : 0;
      });
    },
    /** 格式化文件大小 */
    formatFileSize(size) {
      if (!size) return '0 B';
      const units = ['B', 'KB', 'MB', 'GB'];
      let index = 0;
      while (size >= 1024 && index < units.length - 1) {
        size /= 1024;
        index++;
      }
      return Math.round(size * 100) / 100 + ' ' + units[index];
    },
    /** 图片加载错误处理 */
    handleImageError(event) {
      event.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjY0IiBoZWlnaHQ9IjY0IiBmaWxsPSIjRjVGNUY1Ii8+CjxwYXRoIGQ9Ik0yMCAyMEg0NFY0NEgyMFYyMFoiIHN0cm9rZT0iI0QzRDNEMyIgc3Ryb2tlLXdpZHRoPSIyIiBmaWxsPSJub25lIi8+CjxjaXJjbGUgY3g9IjI4IiBjeT0iMjgiIHI9IjMiIGZpbGw9IiNEM0QzRDMiLz4KPHBhdGggZD0iTTIwIDM2TDI4IDI4TDM2IDM2TDQ0IDI4VjQ0SDIwVjM2WiIgZmlsbD0iI0QzRDNEMyIvPgo8L3N2Zz4K';
      event.target.alt = '图片加载失败';
    },
    /** 图片预览 */
    previewImage(url) {
      this.$alert(`<img src="${url}" style="max-width: 100%; max-height: 80vh;" alt="封面预览">`, '封面预览', {
        dangerouslyUseHTMLString: true,
        showConfirmButton: false,
        showClose: true,
        customClass: 'image-preview-dialog'
      });
    }
  }
}
</script>

<style scoped>
/* 音频播放器样式优化 */
.el-dropdown-menu .el-dropdown-menu__item {
  padding: 8px 16px;
}

/* 文件信息样式 */
.file-info {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

/* Switch开关样式优化 */
.el-switch {
  margin: 0;
}

.el-switch__label {
  font-size: 12px;
}

/* 封面图片样式 */
.cover-image {
  max-width: 200px;
  max-height: 200px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.cover-image:hover {
  transform: scale(1.05);
}

/* 音频控件样式 */
audio {
  outline: none;
}

audio::-webkit-media-controls-panel {
  background-color: #f5f5f5;
}
</style>

<style>
/* 全局样式 - 图片预览对话框 */
.image-preview-dialog {
  text-align: center;
}

.image-preview-dialog .el-message-box__content {
  padding: 20px;
}

.image-preview-dialog .el-message-box__message {
  margin: 0;
}

.image-preview-dialog .el-message-box {
  max-width: 90vw;
  max-height: 90vh;
}
</style>
