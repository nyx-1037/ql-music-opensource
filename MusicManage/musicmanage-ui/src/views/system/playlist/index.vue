<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="歌单名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入歌单名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入创建用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否公开" prop="isPublic">
        <el-input
          v-model="queryParams.isPublic"
          placeholder="请输入是否公开"
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
      <el-form-item label="歌曲数量" prop="musicCount">
        <el-input
          v-model="queryParams.musicCount"
          placeholder="请输入歌曲数量"
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
          v-hasPermi="['system:playlist:add']"
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
          v-hasPermi="['system:playlist:edit']"
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
          v-hasPermi="['system:playlist:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:playlist:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="playlistList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="歌单ID" align="center" prop="id" />
      <el-table-column label="歌单名称" align="center" prop="name" />
      <el-table-column label="歌单描述" align="center" prop="description" />
      <el-table-column label="封面图片URL" align="center" prop="coverUrl" />
      <el-table-column label="创建用户ID" align="center" prop="userId" />
      <el-table-column label="是否公开" align="center" prop="isPublic" />
      <el-table-column label="播放次数" align="center" prop="playCount" />
      <el-table-column label="歌曲数量" align="center" prop="musicCount" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="逻辑删除标志" align="center" prop="isDeleted" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:playlist:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:playlist:remove']"
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

    <!-- 添加或修改歌单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="歌单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入歌单名称" />
        </el-form-item>
        <el-form-item label="歌单描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="封面图片URL" prop="coverUrl">
          <el-input v-model="form.coverUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="创建用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入创建用户ID" />
        </el-form-item>
        <el-form-item label="是否公开" prop="isPublic">
          <el-input v-model="form.isPublic" placeholder="请输入是否公开" />
        </el-form-item>
        <el-form-item label="播放次数" prop="playCount">
          <el-input v-model="form.playCount" placeholder="请输入播放次数" />
        </el-form-item>
        <el-form-item label="歌曲数量" prop="musicCount">
          <el-input v-model="form.musicCount" placeholder="请输入歌曲数量" />
        </el-form-item>
        <el-form-item label="逻辑删除标志" prop="isDeleted">
          <el-input v-model="form.isDeleted" placeholder="请输入逻辑删除标志" />
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
import { listPlaylist, getPlaylist, delPlaylist, addPlaylist, updatePlaylist } from "@/api/system/playlist"

export default {
  name: "Playlist",
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
      // 歌单表格数据
      playlistList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        description: null,
        coverUrl: null,
        userId: null,
        isPublic: null,
        playCount: null,
        musicCount: null,
        status: null,
        isDeleted: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "歌单名称不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "创建用户ID不能为空", trigger: "blur" }
        ],
        isPublic: [
          { required: true, message: "是否公开不能为空", trigger: "blur" }
        ],
        playCount: [
          { required: true, message: "播放次数不能为空", trigger: "blur" }
        ],
        musicCount: [
          { required: true, message: "歌曲数量不能为空", trigger: "blur" }
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
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询歌单列表 */
    getList() {
      this.loading = true
      listPlaylist(this.queryParams).then(response => {
        this.playlistList = response.rows
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
        name: null,
        description: null,
        coverUrl: null,
        userId: null,
        isPublic: null,
        playCount: null,
        musicCount: null,
        status: null,
        createTime: null,
        updateTime: null,
        isDeleted: null
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
      this.title = "添加歌单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPlaylist(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改歌单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlaylist(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPlaylist(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除歌单编号为"' + ids + '"的数据项？').then(function() {
        return delPlaylist(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/playlist/export', {
        ...this.queryParams
      }, `playlist_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
