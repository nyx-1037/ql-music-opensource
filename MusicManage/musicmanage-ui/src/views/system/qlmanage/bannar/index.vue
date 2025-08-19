<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入Banner标题"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="Banner状态" clearable>
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['system:banner:add']"
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
          v-hasPermi="['system:banner:edit']"
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
          v-hasPermi="['system:banner:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:banner:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bannerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Banner ID" align="center" prop="id" />
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="图片" align="center" prop="imageUrl" width="100">
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.imageUrl"
            style="width: 60px; height: 40px"
            :src="scope.row.imageUrl"
            :preview-src-list="[scope.row.imageUrl]"
            fit="cover"
          ></el-image>
          <span v-else>无图片</span>
        </template>
      </el-table-column>
      <el-table-column label="按钮文本" align="center" prop="buttonText" />
      <el-table-column label="动作类型" align="center" prop="actionType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.banner_action_type" :value="scope.row.actionType"/>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="sortOrder" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
            v-hasPermi="['system:banner:edit']"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:banner:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:banner:remove']"
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

    <!-- 添加或修改Banner对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入Banner标题" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入Banner描述" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="upload-demo"
            :action="upload.url"
            :headers="upload.headers"
            :file-list="upload.fileList"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="handleBeforeUpload"
            :limit="1"
            :on-exceed="handleExceed"
            list-type="picture"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="按钮文本" prop="buttonText">
          <el-input v-model="form.buttonText" placeholder="请输入按钮文本" />
        </el-form-item>
        <el-form-item label="动作类型" prop="actionType">
          <el-select v-model="form.actionType" placeholder="请选择动作类型">
            <el-option
              v-for="dict in dict.type.banner_action_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="动作URL" prop="actionUrl">
          <el-input v-model="form.actionUrl" placeholder="请输入动作URL" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listBanner, getBanner, delBanner, addBanner, updateBanner, changeBannerStatus } from "@/api/system/banner"
import { getToken } from "@/utils/auth"

export default {
  name: "Banner",
  dicts: ['banner_action_type'],
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
      // Banner表格数据
      bannerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "Banner标题不能为空", trigger: "blur" }
        ],
        imageUrl: [
          { required: true, message: "Banner图片不能为空", trigger: "blur" }
        ],
        actionType: [
          { required: true, message: "动作类型不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/qlbanner/upload",
        // 上传的文件列表
        fileList: []
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询Banner列表 */
    getList() {
      this.loading = true
      listBanner(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.bannerList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
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
        description: null,
        imageUrl: null,
        buttonText: null,
        actionType: null,
        actionUrl: null,
        sortOrder: 0,
        status: "0",
        remark: null
      }
      this.upload.fileList = []
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加Banner"
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBanner(id).then(response => {
        this.form = response.data
        if (this.form.imageUrl) {
          this.upload.fileList = [{
            name: 'banner.jpg',
            url: this.form.imageUrl
          }]
        }
        this.open = true
        this.title = "修改Banner"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBanner(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBanner(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除Banner编号为"' + ids + '"的数据项？').then(function() {
        return delBanner(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/banner/export', {
        ...this.queryParams
      }, `banner_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleBeforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isJPG) {
        this.$modal.msgError('上传图片只能是 JPG/PNG 格式!')
      }
      if (!isLt10M) {
        this.$modal.msgError('上传图片大小不能超过 10MB!')
      }
      return isJPG && isLt10M
    },
    // 文件个数超出
    handleExceed() {
      this.$modal.msgError("上传文件数量不能超过 1 个!")
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      this.upload.isUploading = false
      if (res.code === 200) {
        this.form.imageUrl = res.data
        this.$modal.msgSuccess(res.msg || "图片上传成功")
      } else {
        this.$modal.msgError(res.msg || "图片上传失败")
      }
    },
    // 上传失败回调
    handleUploadError() {
      this.upload.isUploading = false
      this.$modal.msgError("上传图片失败")
    },
    // Banner状态修改
    handleStatusChange(row) {
      let text = row.status === "1" ? "启用" : "禁用"
      this.$modal.confirm('确认要"' + text + '""' + row.title + '"Banner吗？').then(() => {
        return changeBannerStatus(row.id, row.status)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
        this.getList()
      }).catch(() => {
        // 如果取消或失败，恢复原状态
        row.status = row.status === "1" ? "0" : "1"
      })
    }
  }
}
</script>