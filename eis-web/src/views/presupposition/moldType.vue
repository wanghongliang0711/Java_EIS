<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="模具主种类">
        <el-input
          v-model="queryParams.moldType"
          placeholder="请输入模具主种类"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索</el-button>
        <el-button
          v-if="checkPermission(['ROOT','ADMIN'])"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      v-loading="loading"
      :data="moldTypeList"
      row-key="id"
      height="600"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column
        prop="moldType"
        label="模具种类"
        width="300"
        sortable
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.parentId==0 && checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增子类</el-button>
          <el-button
            v-if="checkPermission(['ROOT','ADMIN'])"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-if="checkPermission(['ROOT'])"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改 模具种类 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="模具类型" prop="moldType">
              <el-input v-model="form.moldType" placeholder="请输入模具类型" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listModeType, addModeType, deleteModeType, updateModeType } from '@/api/presupposition'
import checkPermission from '@/utils/permission'
export default {
  name: 'MoldType',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格数据
      moldTypeList: [],
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 弹出层标题
      title: '',
      // 查询参数
      queryParams: {
        moldType: ''
      },
      // 表单校验
      rules: {
        moldType: [
          { required: true, message: '模具类型不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      // eslint-disable-next-line eqeqeq
      if (row.parentId == 0) {
        this.title = '修改模具种类'
      } else {
        this.title = '修改模具子类'
      }
      this.form.id = row.id
      this.form.moldType = row.moldType
      this.form.parentId = row.parentId
      this.open = true
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row.id === undefined) {
        this.title = '添加模具种类'
      } else {
        this.title = '添加模具子类'
        this.form.parentId = row.id
      }
      this.open = true
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateModeType(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '修改成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          } else {
            addModeType(this.form).then(response => {
              if (response.code === '100000') {
                this.$message({ showClose: true, message: '新增成功', type: 'success' })
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除 "' + row.moldType + '" 的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteModeType(row.id)
      }).then(() => {
        this.getList()
        this.$message({ showClose: true, message: '删除成功', type: 'success' })
      }).catch(function() {})
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        moldType: undefined,
        parentId: 0
      }
    },
    /** 查询模具种类 */
    getList() {
      this.loading = true
      listModeType(this.queryParams).then(response => {
        this.moldTypeList = response.data
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
  }

</style>
