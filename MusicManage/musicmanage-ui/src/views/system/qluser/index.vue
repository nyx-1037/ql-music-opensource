<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable>
           <el-option label="停用" :value="0"></el-option>
           <el-option label="正常" :value="1"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          placeholder="请输入昵称"
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
          v-hasPermi="['system:qluser:add']"
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
          v-hasPermi="['system:qluser:edit']"
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
          v-hasPermi="['system:qluser:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:qluser:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="id" width="80" />
      <el-table-column label="用户名" align="center" prop="username" width="150" />
      <el-table-column label="邮箱" align="center" prop="email" width="200" />
      <el-table-column label="昵称" align="center" prop="nickname" width="150" />
      <el-table-column label="头像" align="center" prop="avatarUrl" width="100">
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.avatarUrl" :src="scope.row.avatarUrl" size="small"></el-avatar>
          <el-avatar v-else icon="el-icon-user-solid" size="small"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="账号状态" align="center" prop="status" width="150">
         <template slot-scope="scope">
           <el-switch
             v-model="scope.row.status"
             :active-value="1"
             :inactive-value="0"
             active-text="正常"
             inactive-text="停用"
             @change="handleStatusChange(scope.row)"
             v-hasPermi="['system:qluser:edit']"
           ></el-switch>
         </template>
       </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:qluser:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:qluser:remove']"
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

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
           <el-input 
             v-model="form.password" 
             placeholder="请输入密码" 
             type="password" 
             maxlength="20" 
             show-password 
             class="password-input"
           >
             <template slot="suffix">
               <el-tooltip content="密码必须包含大小写字母和数字，长度6-20位" placement="top">
                 <i class="el-icon-question"></i>
               </el-tooltip>
             </template>
           </el-input>
         </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="头像URL" prop="avatarUrl">
          <el-input v-model="form.avatarUrl" placeholder="请输入头像URL" maxlength="255">
            <template slot="append">
              <el-button @click="handleAvatarPreview" :disabled="!form.avatarUrl">预览</el-button>
            </template>
          </el-input>
          <div v-if="form.avatarUrl" class="avatar-preview">
             <el-avatar :size="50" :src="form.avatarUrl" @error="handleAvatarError">
               <i class="el-icon-user-solid"></i>
             </el-avatar>
             <span style="margin-left: 10px; color: #909399; font-size: 12px;">头像预览</span>
           </div>
        </el-form-item>
        <el-form-item label="账号状态" prop="status">
           <el-radio-group v-model="form.status">
             <el-radio :label="1">正常</el-radio>
             <el-radio :label="0">停用</el-radio>
           </el-radio-group>
         </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.avatar-preview {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.avatar-preview .el-avatar {
  border: 2px solid #e4e7ed;
  transition: border-color 0.3s;
}

.avatar-preview .el-avatar:hover {
  border-color: #409eff;
}

.status-tag {
  cursor: default;
}

.search-form .el-form-item {
  margin-bottom: 10px;
}

.table-container {
  margin-top: 20px;
}

.dialog-form .el-form-item {
  margin-bottom: 20px;
}

.password-input .el-input__suffix {
  cursor: pointer;
}

.toolbar {
  margin-bottom: 15px;
}

.toolbar .el-button {
  margin-right: 10px;
}

.toolbar .el-button:last-child {
  margin-right: 0;
}

@media (max-width: 768px) {
  .toolbar .el-button {
    margin-bottom: 10px;
    width: 100%;
  }
  
  .search-form .el-col {
    margin-bottom: 10px;
  }
}
</style>

<script>
import { listQlUser, getQlUser, delQlUser, addQlUser, updateQlUser, exportQlUser } from "@/api/system/qluser"

export default {
  name: "QlUser",
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
      // 用户表格数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        email: null,
        nickname: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
          { min: 2, max: 20, message: "用户名长度在 2 到 20 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 6, max: 20, message: '密码长度必须介于 6 和 20 之间', trigger: 'blur' },
          { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{6,}$/, message: '密码必须包含大小写字母和数字', trigger: 'blur' }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
        ],
        nickname: [
          { max: 30, message: "昵称长度不能超过 30 个字符", trigger: "blur" }
        ],
        status: [
          { required: true, message: "账号状态不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listQlUser(this.queryParams).then(response => {
        this.userList = response.rows
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
        username: null,
        password: null,
        email: null,
        nickname: null,
        avatarUrl: null,
        status: 1,
        createTime: null,
        updateTime: null,
        isDeleted: 0
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
      this.title = "添加用户"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getQlUser(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改用户"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateQlUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addQlUser(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户编号为"' + ids + '"的数据项？').then(function() {
        return delQlUser(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/qluser/export', {
         ...this.queryParams
       }, `qluser_${new Date().getTime()}.xlsx`)
     },
     /** 头像预览 */
     handleAvatarPreview() {
       if (this.form.avatarUrl) {
         this.$alert(`<img src="${this.form.avatarUrl}" style="max-width: 300px; max-height: 300px;">`, '头像预览', {
           dangerouslyUseHTMLString: true,
           showConfirmButton: false,
           showClose: true
         })
       }
     },
     /** 头像加载错误处理 */
       handleAvatarError() {
         this.$message.warning('头像加载失败，请检查URL是否正确')
       },
       /** 用户状态修改 */
        handleStatusChange(row) {
          let text = row.status === 1 ? "启用" : "停用";
          this.$modal.confirm('确认要"' + text + '""' + row.username + '"用户吗？').then(() => {
            return updateQlUser({
              id: row.id,
              status: row.status
            });
          }).then(() => {
            this.$modal.msgSuccess(text + "成功");
          }).catch(() => {
            row.status = row.status === 0 ? 1 : 0;
          });
        }
  }
}
</script>
